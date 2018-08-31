package com.gerardogandeaga.cyberlock.objects.savable;

import android.graphics.Bitmap;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.GlideApp;

import java.util.concurrent.ExecutionException;

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
    private String mCurrentUri; // new uri in app
    // old
    private String mOriginalBucket;
    private String mOriginalUri;

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
            String currentUri,
            String originalBucket,
            String originalUri
    ) {
        this.mId = id;
        this.mDisplayName = displayName;
        this.mCurrentBucket = currentBucket;
        this.mCurrentUri = currentUri;
        this.mOriginalBucket = originalBucket;
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

    public Image withCurrentUri(String uri) {
        this.mCurrentUri = uri;
        return this;
    }

    public Image withOriginalBucket(String bucket) {
        this.mOriginalBucket = bucket;
        return this;
    }

    public Image withOriginalUri(String uri) {
        this.mOriginalUri = uri;
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

    public String getCurrentUri() {
        return mCurrentUri;
    }

    public String getOriginalBucket() {
        return mOriginalBucket;
    }

    public String getOriginalUri() {
        return mOriginalUri;
    }

    public Bitmap getImageBitmap() throws ExecutionException, InterruptedException {
        return GlideApp.with(App.getContext())
                       .asBitmap()
                       .load(mCurrentUri)
                       .submit()
                       .get();
    }

    // overrides

    @Override
    public String toString() {
        return "id < " + mId + " > |" + " name < " + mDisplayName + " > |" + " c_bucket < " + mCurrentBucket + " > ";
    }
}
