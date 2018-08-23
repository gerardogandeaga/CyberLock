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
import com.gerardogandeaga.cyberlock.database.DatabaseOpenHelper;
import com.gerardogandeaga.cyberlock.fragments.ImageListViewerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 */
public class ImageViewerActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)   Toolbar mToolbar;
    @BindView(R.id.container) FrameLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_only);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Media Viewer");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new ImageListViewerFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        finish();
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
