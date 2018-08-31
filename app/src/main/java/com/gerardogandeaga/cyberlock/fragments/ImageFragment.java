package com.gerardogandeaga.cyberlock.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gerardogandeaga.cyberlock.activity.ImageGalleryActivity;
import com.gerardogandeaga.cyberlock.lists.decorations.ImageItemDecoration;
import com.gerardogandeaga.cyberlock.lists.items.ImageItem;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.store.Cache;
import com.gerardogandeaga.cyberlock.util.Scale;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 *
 * image gallery from the sqlcipher db
 */
public class ImageFragment extends Fragment {
    public static final String TAG = "ImageFragment";

    private ImageGalleryActivity mGalleryActivity;

    private Bucket mSelectedBucket; // selected bucket to filter images
    private List<Image> mImageList;
    private FastItemAdapter<ImageItem> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mGalleryActivity = ((ImageGalleryActivity) getActivity());

        // set bucket
        this.mSelectedBucket = mGalleryActivity.getSelectedBucket();

        // inits and requests images
        this.mImageList = Cache.ImageCache.requestImages(mSelectedBucket.getName());
    }

    /**
     * create recycler view and configure its item adapter
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(mGalleryActivity));
        final int numColumns = (int) displayWidth / 120;

        // setup recycler view
        RecyclerView imageGallery = new RecyclerView(mGalleryActivity);
        imageGallery.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageGallery.setLayoutManager(new GridLayoutManager(mGalleryActivity, numColumns));
        imageGallery.addItemDecoration(new ImageItemDecoration(numColumns, 4));

        // setup adapter
        this.mAdapter = new FastItemAdapter<>();
        mAdapter.setHasStableIds(true);

        // attach adapter
        imageGallery.setAdapter(mAdapter);

        // add items
        List<ImageItem> imageItems = new ImageItem.Builder().buildItems(mImageList);
        mAdapter.add(imageItems);

        return imageGallery;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
