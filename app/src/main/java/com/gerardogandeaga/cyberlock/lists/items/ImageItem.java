package com.gerardogandeaga.cyberlock.lists.items;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-12
 *
 * creates image views for adapter for the cyber lock image gallery.
 * this class is depended on images that are pulled from tthe sqlcipher db
 */
public class ImageItem extends AbstractItem<ImageItem, ImageItem.ViewHolder> {
    private Image mImage;

    public ImageItem(Image image) {
        this.mImage = image;
    }

    public Image getImage() {
        return mImage;
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
            Glide.with(App.getContext()).load(item.getImage().getCurrentUri()).into(Image); // load image from bitmap source
        }

        @Override
        public void unbindView(@NonNull ImageItem item) {
            // remove image from view
            GlideApp.with(App.getContext()).clear(Image);
        }
    }

    @Override
    public String toString() {
        return mImage.getCurrentUri();
    }

    /**
     * builds image items from images to use in an adapter
     */
    public static class ItemBuilder {

        public static ImageItem buildItem(Image image) {
            return new ImageItem(image);
        }

        public static List<ImageItem> buildItems(List<Image> imageList) {
            List<ImageItem> imageItems = new ArrayList<>();

            for (Image image : imageList) {
                imageItems.add(buildItem(image));
            }

            return imageItems;
        }
    }
}
