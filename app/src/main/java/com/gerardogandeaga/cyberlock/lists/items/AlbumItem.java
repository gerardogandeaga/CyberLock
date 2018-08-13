package com.gerardogandeaga.cyberlock.lists.items;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-12
 */
public class AlbumItem extends AbstractItem<AlbumItem, AlbumItem.ViewHolder> {
    private Bucket mBucket;

    public AlbumItem(Bucket bucket) {
        this.mBucket = bucket;
    }

    public Bucket getBucket() {
        return mBucket;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_album;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    protected class ViewHolder extends FastItemAdapter.ViewHolder<AlbumItem> {
        @NonNull private View mView;
        @NonNull private Context mContext;

        @BindView(R.id.container) ConstraintLayout Container;
        @BindView(R.id.imgImage)  ImageView Image;
        @BindView(R.id.tvTitle)   TextView Name;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mView = view;
            this.mContext = mView.getContext();
        }

        @Override
        public void bindView(@NonNull AlbumItem item, @NonNull List<Object> payloads) {
            Name.setText(item.getBucket().getName());
            // add image to the image view
            GlideApp.with(mContext).load(item.getBucket().getCoverImageUri()).into(Image);
        }

        @Override
        public void unbindView(@NonNull AlbumItem item) {
            Name.setText(null);
            // remove image from view
            GlideApp.with(mContext).clear(Image);
        }
    }
}
