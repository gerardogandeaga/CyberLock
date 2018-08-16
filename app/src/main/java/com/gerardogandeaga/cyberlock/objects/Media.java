package com.gerardogandeaga.cyberlock.objects;

import android.graphics.Bitmap;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 */
public class Media {
    private String mUri;
    private Bitmap mImageBitmap;
    private String mBucketName;

    public Media withAlbum(String bucketName) {
        this.mBucketName = bucketName;
        return this;
    }

    public Media withUri(String uri) {
        this.mUri = uri;
        return this;
    }

    public Media withImageBitmap(Bitmap bitmapSource) {
        this.mImageBitmap = bitmapSource;
        return this;
    }

    public String getBucketName() {
        return mBucketName;
    }

    public String getUri() {
        return mUri;
    }

    public Bitmap getImageBitmap() {
        return mImageBitmap;
    }

    @Override
    public String toString() {
        return "bucket -> " + mBucketName + " | address ->  " + mUri;
    }
}
