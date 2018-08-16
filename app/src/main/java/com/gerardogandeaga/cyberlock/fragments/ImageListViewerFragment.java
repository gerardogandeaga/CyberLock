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

import com.gerardogandeaga.cyberlock.lists.decorations.ImageItemDecoration;
import com.gerardogandeaga.cyberlock.lists.items.ImageItem;
import com.gerardogandeaga.cyberlock.util.Scale;
import com.mikepenz.fastadapter.FastAdapter;

/**
 * @author gerardogandeaga
 * created on 2018-08-14
 *
 * image gallery from the sqlcipher db
 */
public class ImageListViewerFragment extends Fragment {
    //

    //
    private AppCompatActivity mActivity;

    private FastAdapter<ImageItem> mAdapter;
    private RecyclerView mImageViewRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = ((AppCompatActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final double displayWidth = Scale.convertPixelsToDp(Scale.getScreenWidth(mActivity));
        final int numColumns = (int) displayWidth / 120;

        // setup recycler view
        this.mImageViewRecyclerView = new RecyclerView(mActivity);
        mImageViewRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mImageViewRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, numColumns));
        mImageViewRecyclerView.addItemDecoration(new ImageItemDecoration(numColumns, 4));

        // setup adapter
        this.mAdapter = new FastAdapter<>();
        mAdapter.setHasStableIds(true);

        // attach adapter
        mImageViewRecyclerView.setAdapter(mAdapter);

        // todo pull bitmap images from sqlcipher database and add to adapter

        return mImageViewRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
