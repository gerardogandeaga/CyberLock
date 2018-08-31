package com.gerardogandeaga.cyberlock.objects;

import com.gerardogandeaga.cyberlock.objects.savable.SavableObject;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 */
public class Bucket extends SavableObject {
    private int mSize;
    private int mId;
    private String mName;
    private String mCoverUri;

    /**
     * creating new bucket for import activity
     */
    public Bucket(String bucketName, String coverUri) {
        this.mName = bucketName;
        this.mCoverUri = coverUri;
    }

    /**
     * creating bucket from the db
     */
    public Bucket(int id, String bucketName, String coverUri) {
        this(bucketName, coverUri);
        this.mId = id;
    }

    // setters

    public Bucket withSize(int size) {
        this.mSize = size;
        return this;
    }

    public Bucket withId(int id) {
        this.mId = id;
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

    public int getId() {
        return mId;
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
