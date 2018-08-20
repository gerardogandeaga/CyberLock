package com.gerardogandeaga.cyberlock.lists.items;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.GlideApp;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.objects.savable.Image;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-12
 */
public class ImportImageItem extends AbstractItem<ImportImageItem, ImportImageItem.ViewHolder> {
    private Image mImage;

    public ImportImageItem(Image image) {
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
        return R.layout.item_import_image;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }

    protected class ViewHolder extends FastItemAdapter.ViewHolder<ImportImageItem> {
        @BindView(R.id.container) ConstraintLayout Container;
        @BindView(R.id.imgImage)  ImageView Image;
        @BindView(R.id.checkbox)  CheckBox Selected;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(@NonNull ImportImageItem item, @NonNull List<Object> payloads) {
            Selected.setChecked(item.isSelected());
            // add image to the image view
            GlideApp.with(App.getContext())
                    .load(item.getImage().getCurrentUri())
                    .into(Image);
        }

        @Override
        public void unbindView(@NonNull ImportImageItem item) {
            // remove image from view
            GlideApp.with(App.getContext())
                    .clear(Image);
        }
    }
}
