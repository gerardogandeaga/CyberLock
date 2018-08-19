package com.gerardogandeaga.cyberlock.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gerardogandeaga.cyberlock.storage.database.DBImageAccessor;
import com.gerardogandeaga.cyberlock.lists.decorations.ImageItemDecoration;
import com.gerardogandeaga.cyberlock.lists.items.ImageItem;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.gerardogandeaga.cyberlock.util.Scale;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.ArrayList;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 *
 * image gallery from the sqlcipher db
 */
public class ImageListViewerFragment extends Fragment {
    private static final String TAG = "ImageListViewerFragment";

    private AppCompatActivity mActivity;

    private ArrayList<Image> mImages;
    private FastItemAdapter<ImageItem> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = ((AppCompatActivity) getActivity());

        // pull images from database
        DBImageAccessor DBAccessor = DBImageAccessor.getInstance();
        this.mImages = DBAccessor.getImages();
    }

    /**
     * create recycler view and configure its item adapter
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(mActivity));
        final int numColumns = (int) displayWidth / 120;

        // setup recycler view
        RecyclerView imageViewRecyclerView = new RecyclerView(mActivity);
        imageViewRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageViewRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, numColumns));
        imageViewRecyclerView.addItemDecoration(new ImageItemDecoration(numColumns, 4));

        // setup adapter
        this.mAdapter = new FastItemAdapter<>();
        mAdapter.setHasStableIds(true);

        // add items
        ArrayList<ImageItem> imageItems = ImageItem.ItemBuilder.buildItems(mImages);
        mAdapter.add(imageItems);

        // attach adapter
        imageViewRecyclerView.setAdapter(mAdapter);

        return imageViewRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
