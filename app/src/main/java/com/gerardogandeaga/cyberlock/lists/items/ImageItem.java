package com.gerardogandeaga.cyberlock.lists.items;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.objects.Media;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-12
 */
public class ImageItem extends AbstractItem<ImageItem, ImageItem.ViewHolder> {
    private Media mMedia;

    public ImageItem(Media media) {
        this.mMedia = media;
    }

    public Media getMedia() {
        return mMedia;
    }

    @Override
    public int getType() {
        return R.id.fastadapter_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_image;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    protected class ViewHolder extends FastItemAdapter.ViewHolder<ImageItem> {
        @BindView(R.id.container) ConstraintLayout Container;
        @BindView(R.id.imgImage)  ImageView Image;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(@NonNull ImageItem item, @NonNull List<Object> payloads) {
            // add image to the image view
            Glide.with(App.getContext()).load(item.getMedia().getImageBitmap()).into(Image);
        }

        @Override
        public void unbindView(@NonNull ImageItem item) {
            // remove image from view
            GlideApp.with(App.getContext()).clear(Image);
        }
    }
}
