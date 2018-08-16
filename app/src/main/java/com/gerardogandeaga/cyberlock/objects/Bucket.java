package com.gerardogandeaga.cyberlock.objects;

/**
 * @author gerardogandeaga
 * created on 2018-08-03
 */
public class Bucket {
    private int mSize;
    private String mName;
    private String mCoverImageUri;

    public Bucket withSize(int size) {
        this.mSize = size;
        return this;
    }

    public Bucket withName(String name) {
        this.mName = name;
        return this;
    }

    public Bucket withCoverImageUri(String coverImageUri) {
        this.mCoverImageUri = coverImageUri;
        return this;
    }

    public int getSize() {
        return mSize;
    }

    public String getName() {
        return mName;
    }

    public String getCoverImageUri() {
        return mCoverImageUri;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Bucket && mName.equals(((Bucket) obj).getName());
    }

    @Override
    public String toString() {
        return mName;
    }
}
