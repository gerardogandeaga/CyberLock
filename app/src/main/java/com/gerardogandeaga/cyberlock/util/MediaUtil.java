package com.gerardogandeaga.cyberlock.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * @author gerardogandeaga
 * created on 2018-08-16
 */
public class MediaUtil {

    public static Bitmap createBitmap(String uriSource) {
        return null;
    }

    public static Bitmap createBitmap(byte[] byteSource) {
        return BitmapFactory.decodeByteArray(byteSource, 0, byteSource.length);
    }

    public static byte[] createBytes(Bitmap bitmapSource) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        bitmapSource.compress(Bitmap.CompressFormat.PNG, 0, outStream);
        return outStream.toByteArray();
    }
}
