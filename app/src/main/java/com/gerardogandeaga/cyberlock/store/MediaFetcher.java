package com.gerardogandeaga.cyberlock.store;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.savable.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 *
 * handles transactions between images databases in phones and application.
 * this class's primary fucntion is caching images and buckets for performance.
 */
public class MediaFetcher {
    private static final String TAG = "MediaFetcher";

    private static final Uri SD_IMAGES_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    // information about the images that will be retrieved
    private static final String[] PROJECTION = {
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
    };

    // cache data:
    // data we do not want to have to keep requesting from phone, so we only update these vars when needed.
    // this also saves memory and time by only having one instance of the data
    private static List<Bucket> BucketCache;
    private static List<Image> ImageCache;

    private static HashMap<String, List<Image>> AlbumCache;

    /**
     * updates cache if BucketCache are null
     * @return list of all buckets on phone
     */
    public static List<Bucket> requestAlbums() {
        if (BucketCache == null || BucketCache.size() == 0) {
            cacheData(); // update
        }
        return BucketCache;
    }

    /**
     * updates cache if images are null
     * @param selectionKey name of the album to which the arrays are stored under in the HashMap
     * @return list of all intended images with respective selection args
     */
    public static List<Image> requestImages(String selectionKey) {
        if (AlbumCache == null || ImageCache == null || ImageCache.size() == 0) {
            cacheData(); // update
        }

        return AlbumCache.get(selectionKey);
    }

    /**
     * retrieves all uri to all images on phone, all bucket names for buckets and image ids.
     * then image and album objects are created to be stored into the caches images and buckets
     */
    private static void cacheData() {
        // retrieve all required columns
        Cursor cursor = App.getContext().getContentResolver().query(
                SD_IMAGES_URI,
                PROJECTION, // which columns to return
                null, // which rows to return (all rows)
                null, // selection args
                MediaStore.Images.Media.DATE_TAKEN // ordering
        );

        // time to cache images and information
        if (cursor != null && cursor.getCount() > 0) {
            // init cache arrays
            BucketCache = new ArrayList<>();
            ImageCache = new ArrayList<>();

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);

                int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID); // image id
                int displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME); // image name
                int uriColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA); // uri
                int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME); // bucket
                // string values
                String id = cursor.getString(idColumn);
                String displayName = cursor.getString(displayNameColumn);
                String uri = cursor.getString(uriColumn);
                String bucketName = cursor.getString(bucketNameColumn);

                // create image and bucket
                Image image = new Image(id, displayName, bucketName, uri);

//                System.out.println(image);

                Bucket bucket = new Bucket(bucketName, uri);

                // add images to cache
                ImageCache.add(i, image);

                // update bucket cache
                addToBucketCache(bucket);
            }
            // the cursor should be freed up
            cursor.close();
        }

        // we reverse it to match the order of the phone
        Collections.reverse(ImageCache);

        // setup albums for fast access
        setupImageAlbumsHashMap();

        // update sizes
        updateBucketSizes();
    }

    private static void addToBucketCache(Bucket bucket) {
        // BucketCache
        // we only want one instance of the bucket
        if (!BucketCache.contains(bucket)) {
            BucketCache.add(bucket);
        } else {
            // if the bucket already exists then we just need to update the cover image
            for (Bucket item : BucketCache) {
                // find and replace uri
                if (bucket.equals(item)) {
                    item.withCoverUri(bucket.getCoverUri());
                }
            }
        }
    }

    /**
     * sets sizes of the buckets
     */
    private static void updateBucketSizes() {
        for (Bucket bucket : BucketCache) {
            bucket.withSize(AlbumCache.get(bucket.getName()).size());
        }
    }

    private static void setupImageAlbumsHashMap() {
        AlbumCache = new HashMap<>();
        AlbumCache.put(null, ImageCache); // null key = all images

        // add image to map according to the album name
        for (Bucket bucket : BucketCache) {
            List<Image> imageList = new ArrayList<>();

            // build the mediaArray with matching bucket
            for (int i = 0; i < ImageCache.size(); i++) {
                Image image = ImageCache.get(i);
                if (image.getCurrentBucket().equals(bucket.getName())) {
                    imageList.add(image);
                }
            }

            // add array to map with bucket name being the key
            AlbumCache.put(bucket.getName(), imageList);
        }
    }
}
