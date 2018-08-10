package com.gerardogandeaga.cyberlock.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.gerardogandeaga.cyberlock.App;

/**
 * @author gerardogandeaga
 * created on 2018-08-04
 */
public class Res {

    public static int getColour(@ColorRes int colourResId) {
        return App.getContext().getColor(colourResId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableResId) {
        return App.getContext().getDrawable(drawableResId);
    }
}
