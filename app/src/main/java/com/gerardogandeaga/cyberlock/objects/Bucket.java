package com.gerardogandeaga.cyberlock.objects;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 */
public class Bucket {
    private int mSize;
    private String mName;
    private String mCoverUri;

    public Bucket(String bucketName, String coverUri) {
        this.mName = bucketName;
        this.mCoverUri = coverUri;
    }

    // setters

    public Bucket withSize(int size) {
        this.mSize = size;
        return this;
    }

    public Bucket withName(String name) {
        this.mName = name;
        return this;
    }

    public Bucket withCoverUri(String coverUri) {
        this.mCoverUri = coverUri;
        return this;
    }

    // getters

    public int getSize() {
        return mSize;
    }

    public String getName() {
        return mName;
    }

    public String getCoverUri() {
        return mCoverUri;
    }

    // overrides

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bucket && mName.equals(((Bucket) obj).getName());
    }

    @Override
    public String toString() {
        return "bucket name : " + mName;
    }
}
