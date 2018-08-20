package com.gerardogandeaga.cyberlock.lists.items;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gerardogandeaga.cyberlock.App;
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
public class ImportAlbumItem extends AbstractItem<ImportAlbumItem, ImportAlbumItem.ViewHolder> {
    private Bucket mBucket;

    public ImportAlbumItem(Bucket bucket) {
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
        return R.layout.item_import_album;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    protected class ViewHolder extends FastItemAdapter.ViewHolder<ImportAlbumItem> {
        @BindView(R.id.container) ConstraintLayout Container;
        @BindView(R.id.imgImage)  ImageView Image;
        @BindView(R.id.tvTitle)   TextView Name;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(@NonNull ImportAlbumItem item, @NonNull List<Object> payloads) {
            Name.setText(item.getBucket().getName());
            // add image to the image view
            Glide.with(App.getContext()).load(item.getBucket().getCoverUri()).into(Image);
        }

        @Override
        public void unbindView(@NonNull ImportAlbumItem item) {
            Name.setText(null);
            // remove image from view
            Glide.with(App.getContext()).clear(Image);
        }
    }
}
