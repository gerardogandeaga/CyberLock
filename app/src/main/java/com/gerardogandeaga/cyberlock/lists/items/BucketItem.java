package com.gerardogandeaga.cyberlock.lists.items;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gerardogandeaga.cyberlock.App;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.lists.ItemBuiler;
import com.gerardogandeaga.cyberlock.objects.Bucket;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-12
 */
public class BucketItem extends AbstractItem<BucketItem, BucketItem.ViewHolder> {
    private Bucket mBucket;

    public BucketItem(Bucket bucket) {
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

    protected class ViewHolder extends FastItemAdapter.ViewHolder<BucketItem> {
        @BindView(R.id.container) ConstraintLayout Container;
        @BindView(R.id.frame)     CardView Frame;
        @BindView(R.id.imgImage)  ImageView Image;
        @BindView(R.id.tvTitle)   TextView Name;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindView(@NonNull BucketItem item, @NonNull List<Object> payloads) {
            // container background
            UIUtils.setBackground(Frame, R.drawable.bk_import_album);

            Bucket bucket = item.getBucket();
            // if bucket name is null then it will refer to all images
            Name.setText(bucket.getName() != null ? bucket.getName() : "All Images");
            // add image to the image view
            Glide.with(App.getContext()).load(item.getBucket().getCoverUri()).into(Image);
        }

        @Override
        public void unbindView(@NonNull BucketItem item) {
            Name.setText(null);
            // remove image from view
            Glide.with(App.getContext()).clear(Image);
        }
    }

    /**
     * builds image items from images to use in an adapter
     */
    public static class Builder implements ItemBuiler<BucketItem, Bucket> {

        @Override
        public BucketItem buildItem(Bucket bucket) {
            return new BucketItem(bucket);
        }

        @Override
        public List<BucketItem> buildItems(List<Bucket> buckets) {
            List<BucketItem> allItems = new ArrayList<>();

            // add the rest of the saved albums
            for (Bucket bucket : buckets) {
                allItems.add(buildItem(bucket));
            }

            return allItems;
        }
    }
}
