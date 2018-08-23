package com.gerardogandeaga.cyberlock.objects.savable;

import android.graphics.Bitmap;

/**
 * @author gerardogandeaga
 * created on 2018-08-16
 */
public class Image extends SavableObject {
    // stored data
    private String mId; // image id
    private String mDisplayName; // image display name
    // current
    private String mCurrentBucket; // album in app
    private String mCurrentPath; // new path in app
    private String mCurrentUri; // new uri in app
    // old
    private String mOriginalBucket;
    private String mOriginalPath;
    private String mOriginalUri;
    //
    private Bitmap mImageBitmap; // cached images in app

    /**
     * building an import image
     */
    public Image(String id, String displayName, String bucket, String uri) {
        this.mId = id;
        this.mDisplayName = displayName;
        this.mCurrentBucket = bucket;
        this.mCurrentUri = uri;
    }

    /**
     * building a new image object straight out the db
     */
    public Image(
            String id,
            String displayName,
            String currentBucket,
            String currentPath,
            String currentUri,
            String originalBucket,
            String originalPath,
            String originalUri
    ) {
        this.mId = id;
        this.mDisplayName = displayName;
        this.mCurrentBucket = currentBucket;
        this.mCurrentPath = currentPath;
        this.mCurrentUri = currentUri;
        this.mOriginalBucket = originalBucket;
        this.mOriginalPath = originalPath;
        this.mOriginalUri = originalUri;
    }

    // setters

    public Image withId(String id) {
        this.mId = id;
        return this;
    }

    public Image withDisplayName(String displayName) {
        this.mDisplayName = displayName;
        return this;
    }

    public Image withCurrentBucket(String bucket) {
        this.mCurrentBucket = bucket;
        return this;
    }

    public Image withCurrentPath(String path) {
        this.mCurrentPath = path;
        return this;
    }

    public Image withCurrentUri(String uri) {
        this.mCurrentUri = uri;
        return this;
    }

    public Image withOriginalBucket(String bucket) {
        this.mOriginalBucket = bucket;
        return this;
    }

    public Image withOriginalPath(String path) {
        this.mOriginalPath = path;
        return this;
    }

    public Image withOriginalUri(String uri) {
        this.mOriginalUri = uri;
        return this;
    }

    public Image withImageBitmap(Bitmap bitmap) {
        this.mImageBitmap = bitmap;
        return this;
    }

    // getters

    public String getId() {
        return mId;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getCurrentBucket() {
        return mCurrentBucket;
    }

    public String getCurrentPath() {
        return mCurrentPath;
    }

    public String getCurrentUri() {
        return mCurrentUri;
    }

    public String getOriginalBucket() {
        return mOriginalBucket;
    }

    public String getOriginalPath() {
        return mOriginalPath;
    }

    public String getOriginalUri() {
        return mOriginalUri;
    }

    public Bitmap getImageBitmap() {
        return mImageBitmap;
    }

    // overrides

    @Override
    public String toString() {
        return "id < " + mId + " > |" + " name < " + mDisplayName + " > |" + " c_bucket < " + mCurrentBucket + " > ";
    }
}
