package com.gerardogandeaga.cyberlock.objects.savable;

import android.graphics.Bitmap;

/**
 * @author gerardogandeaga
 * created on 2018-08-16
 */
public class Image extends SavableObject {
    private String mBucket; // album in app
    private String mId; // image name
    private String mPath; // new path in app
    private String mUri; // new uri in app
    private Bitmap mImageBitmap; // cached images in app
    // old
    private String mOldBucket;
    private String mOldPath;
    private String mOldUri;

    public Image(
            long timeCreated,
            String bucket,
            String id,
            String path,
            String uri
    ) {
        setTimeCreated(timeCreated);
        this.mBucket = bucket;
        this.mId = id;
        this.mPath = path;
        this.mUri = uri;
    }

    public String getBucket() {
        return mBucket;
    }

    public String getId() {
        return mId;
    }

    public String getPath() {
        return mPath;
    }

    public String getUri() {
        return mUri;
    }

    public Bitmap getImageBitmap() {
        return mImageBitmap;
    }
}
