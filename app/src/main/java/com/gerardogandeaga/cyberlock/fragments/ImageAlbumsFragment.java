package com.gerardogandeaga.cyberlock.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.activity.ImageImportActivity;
import com.gerardogandeaga.cyberlock.activity.MenuActivity;
import com.gerardogandeaga.cyberlock.interfaces.OnButtonPressedListener;
import com.gerardogandeaga.cyberlock.lists.decorations.ImageItemDecoration;
import com.gerardogandeaga.cyberlock.lists.items.BucketItem;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.store.Cache;
import com.gerardogandeaga.cyberlock.util.Filter;
import com.gerardogandeaga.cyberlock.util.Scale;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-24
 */
public class ImageAlbumsFragment extends Fragment implements OnButtonPressedListener {
    public static final String TAG = "ImageAlbumsFragment";

    public interface OnBucketSelectListener {
        void onBucketSelected(Bucket bucket);
    }
    private OnBucketSelectListener mOnBucketSelectListener;
    private AppCompatActivity mActivity;

    private List<Bucket> mAlbumsList;
    private FastItemAdapter<BucketItem> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.mActivity = (AppCompatActivity) getActivity();
        try {
            this.mOnBucketSelectListener = (OnBucketSelectListener) mActivity;
        } catch (ClassCastException e) {
            Log.e(TAG, "could not cast activity to OnBucketSelectListener");
        }

        // request buckets
        this.mAlbumsList = Cache.ImageCache.requestAlbums();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(mActivity));
        final int numColumns = (int) displayWidth / 120;

        // setup recycler view
        RecyclerView albumGallery = new RecyclerView(mActivity);
        albumGallery.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        albumGallery.setLayoutManager(new GridLayoutManager(mActivity, numColumns));
        albumGallery.addItemDecoration(new ImageItemDecoration(numColumns, 4));

        // setup adapter
        this.mAdapter = new FastItemAdapter<>();
        mAdapter.setHasStableIds(true);
        // on click listener which changes the album to the one clicked
        mAdapter.withOnClickListener(new OnClickListener<BucketItem>() {
            @Override
            public boolean onClick(@Nullable View v, @NonNull IAdapter<BucketItem> adapter, @NonNull BucketItem item, int position) {
                showSelectedAlbumImages(item.getBucket());
                return false;
            }
        });

        // attach adapter
        albumGallery.setAdapter(mAdapter);

        // add items
        List<BucketItem> bucketItemList = new BucketItem.Builder().buildItems(mAlbumsList);
        mAdapter.add(bucketItemList);

        return albumGallery;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_albums, menu);
        Filter.filterMenu(menu, R.color.white);

        mActivity.getSupportActionBar().setTitle("Albums");
    }

    /**
     * starts the {@link ImageImportActivity.ImageFragment} asking it
     * to only display images that belong to the passed in bucket
     * @param bucket filter for which images to show
     */
    private void showSelectedAlbumImages(Bucket bucket) {
        mOnBucketSelectListener.onBucketSelected(bucket);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_create_album:
                createBucket();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(mActivity, MenuActivity.class);
        startActivity(i);
        mActivity.finish();
    }

    private void createBucket() {
        // pre-make text input
        final EditText name = new EditText(mActivity);
        name.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        name.setTextSize(14f);
        // create dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle("New Album");
        dialog.setView(name);
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mActivity, name.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
