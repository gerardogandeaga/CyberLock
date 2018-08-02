package com.gerardogandeaga.cyberlock.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.gerardogandeaga.cyberlock.GalleryViewerFragment;
import com.gerardogandeaga.cyberlock.R;

import butterknife.BindView;

/**
 * @author gerardogandeaga
 * created on 2018-07-28
 */
public class GalleryActivity extends AppCompatActivity {
    @BindView(R.id.container)
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_only);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new GalleryViewerFragment());
        fragmentTransaction.commit();
    }
}
