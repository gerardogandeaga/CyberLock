package com.gerardogandeaga.cyberlock.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.util.Res;
import com.gerardogandeaga.cyberlock.util.Scale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-08-13
 */
public class ImagePreviewDialog {
    private Activity mActivity;

    // multiple methods to load the image in the dialog preview
    private String mImageUri;
    private Drawable mImageDrawable;
    private Bitmap mImageBitmap;

    //
    private int mDialogWidth;
    private int mDialogHeight;

    public ImagePreviewDialog(Activity activity, String uriSource) {
        this(activity);
        this.mImageUri = uriSource;
    }

    public ImagePreviewDialog(Activity activity, Drawable drawableSource) {
        this(activity);
        this.mImageDrawable = drawableSource;
    }

    public ImagePreviewDialog(Activity activity, Bitmap bitmapSource) {
        this(activity);
        this.mImageBitmap = bitmapSource;
    }

    private ImagePreviewDialog(Activity activity) {
        this.mActivity = activity;
    }

    @BindView(R.id.container) RelativeLayout Container;
    @BindView(R.id.imgImage)  ImageView Image;

    public void showDialog() throws NullPointerException {
        Dialog dialog = new Dialog(mActivity); // <- wraps around content view
        View view = View.inflate(mActivity, R.layout.dialog_image_preview, null);
        // bind to view
        ButterKnife.bind(this, view);
        Container.setBackgroundColor(Res.getColour(R.color.black));
        Image.setMaxWidth((int) (Scale.getScreenWidth(mActivity) * 0.90));
        Image.setMaxHeight((int) (Scale.getScreenHeight(mActivity) * 0.90)); // when in portrait the height cannot be more than 75% the screen size

        // load image into imageview
        if (mImageUri != null) {
            Glide.with(mActivity).load(mImageUri).into(Image);
        } else
        if (mImageDrawable != null) {
            Glide.with(mActivity).load(mImageUri).into(Image);
        } else
        if (mImageBitmap != null) {
            Glide.with(mActivity).load(mImageBitmap).into(Image);
        } else {
            throw new NullPointerException("could not load image, all sources are null");
        }

        // logic
        dialog.setContentView(view);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Glide.with(mActivity).clear(Image); // clear memory
            }
        });

        dialog.create();
        dialog.show();
    }
}
