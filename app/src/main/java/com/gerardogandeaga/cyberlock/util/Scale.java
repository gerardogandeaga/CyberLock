package com.gerardogandeaga.cyberlock.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.gerardogandeaga.cyberlock.App;

/**
 * @author gerardogandeaga
 * created on 2018-08-01
 */
public class Scale {

    public static float convertDpToPixel(float dp) {
        Resources resources = App.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px) {
        Resources resources = App.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
