package com.gerardogandeaga.cyberlock.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.fragments.ImageAlbumsFragment;
import com.gerardogandeaga.cyberlock.fragments.ImageFragment;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.store.Cache;
import com.gerardogandeaga.cyberlock.store.database.DatabaseOpenHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 */
public class ImageGalleryActivity extends AppCompatActivity implements ImageAlbumsFragment.OnBucketSelectListener {
    private static FragmentManager Manager;
    private Bucket mSelectedAlbum;

    @BindView(R.id.toolbar)   Toolbar mToolbar;
    @BindView(R.id.container) FrameLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_only);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init image caches
        Cache.ImageCache.updateCaches();

        Manager = getFragmentManager();

        // init fragment, start in album
        switchFragments(ImageAlbumsFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * switches between album and ImageArrayList fragments throught tags
     * @param fragTag name of fragment to start
     */
    public static void switchFragments(String fragTag) {
        int container = R.id.container;

        FragmentTransaction transaction = Manager.beginTransaction();

        switch (fragTag) {
            case ImageAlbumsFragment.TAG:
                transaction.replace(container, new ImageAlbumsFragment());
                break;
            case ImageFragment.TAG:
                transaction.replace(container, new ImageFragment());
                break;
        }

        transaction.commit();
    }

    // bucket selection and listening

    @Override
    public void onBucketSelected(Bucket bucket) {
        setSelectedBucket(bucket);
    }

    /**
     * albums is changed by {@link ImageAlbumsFragment}
     * @param selectedAlbum image album
     */
    public void setSelectedBucket(Bucket selectedAlbum) {
        this.mSelectedAlbum = selectedAlbum;
    }

    /**
     * only accessed by {@link com.gerardogandeaga.cyberlock.fragments.ImageFragment}
     * @return image albums
     */
    public Bucket getSelectedBucket() {
        return mSelectedAlbum;
    }

    // temp functions for testing

    /**
     * verifies that a sql db exists
     * @return if sql file exists then return true else false
     */
    private boolean databaseExist() {
        return getDatabasePath(DatabaseOpenHelper.DATABASE).exists();
    }
}
