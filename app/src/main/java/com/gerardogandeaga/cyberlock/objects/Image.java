package com.gerardogandeaga.cyberlock.objects;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 */
public class Image {
    private String mUri;
    private String mBucketName;

    public Image withAlbum(String bucketName) {
        mBucketName = bucketName;
        return this;
    }

    public Image withUri(String uri) {
        mUri = uri;
        return this;
    }

    public String getBucketName() {
        return mBucketName;
    }

    public String getUri() {
        return mUri;
    }

    @Override
    public String toString() {
        return "bucket -> " + mBucketName + " | address ->  " + mUri;
    }
}
