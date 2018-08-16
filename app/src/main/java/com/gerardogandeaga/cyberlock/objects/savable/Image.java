package com.gerardogandeaga.cyberlock.objects.savable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author gerardogandeaga
 * created on 2018-08-16
 */
public class Image extends SavableObject {
    private Bitmap mImageBitmap;

    public Image(
            long created,
            byte[] imageBytes
    ) {
        setTimeCreated(created);
        this.mImageBitmap = getImageBitmap(imageBytes);
    }

    private Bitmap getImageBitmap(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
