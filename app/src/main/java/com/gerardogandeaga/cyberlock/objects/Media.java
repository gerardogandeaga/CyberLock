package com.gerardogandeaga.cyberlock.objects;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 */
public class Media {
    private String mId;
    private String mUri;
    private String mBucketName;

    public Media withId(String id) {
        this.mId = id;
        return this;
    }

    public Media withAlbum(String bucketName) {
        this.mBucketName = bucketName;
        return this;
    }

    public Media withUri(String uri) {
        this.mUri = uri;
        return this;
    }

    public String getId() {
        return mId;
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
