package com.gerardogandeaga.cyberlock.store;

import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.store.database.DBImageAccessor;
import com.gerardogandeaga.cyberlock.store.database.DBImageBucketAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-25
 *
 * caching data from db reduces need for constantly retreiving data from the db
 */
public class Cache {

    /**
     * manages getting image data from db
     */
    public static class ImageCache {
        private static HashMap<String, List<Image>> AlbumCache;
        private static List<Bucket> BucketCache;
        private static List<Image> ImageCache;

        /**
         * updates cache if BucketCache are null
         * @return list of all buckets on phone
         */
        public static List<Bucket> requestAlbums() {
            if (BucketCache == null || BucketCache.size() == 0) {
                updateCaches(); // update
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
                updateCaches(); // update
            }

            return AlbumCache.get(selectionKey);
        }

        public static void updateCaches() {
            updateImageCache();
            updateBucketCache();
        }

        private static void updateImageCache() {
            // get all buckets from db
            DBImageBucketAccessor bucketAccessor = DBImageBucketAccessor.getInstance();
            bucketAccessor.openReadable();
            BucketCache = bucketAccessor.getAllItems();
            bucketAccessor.close();

            // get all images from db
            DBImageAccessor imageAccessor = DBImageAccessor.getInstance();
            imageAccessor.openReadable();
            ImageCache = imageAccessor.getAllItems();
            imageAccessor.close();
        }

        private static void updateBucketCache() {
            AlbumCache = new HashMap<>();
            AlbumCache.put(null, ImageCache); // null key = all images

            // add image lists to AlbumCache sorted by album
            for (Bucket bucket : BucketCache) {
                List<Image> imageList = new ArrayList<>();

                // create lists with specific buckets
                for (int i = 0; i < ImageCache.size(); i++) {
                    Image image = ImageCache.get(i);
                    if (image.getCurrentBucket().equals(bucket.getName())) {
                        imageList.add(image);
                    }
                }

                // add list to map with the key being the bucket name
                AlbumCache.put(bucket.getName(), imageList);
            }
        }
    }
}
