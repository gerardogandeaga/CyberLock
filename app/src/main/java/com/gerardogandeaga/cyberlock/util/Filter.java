package com.gerardogandeaga.cyberlock.util;

import android.graphics.PorterDuff;
import android.support.annotation.ColorRes;
import android.view.Menu;

/**
 * @author gerardogandeaga
 * created on 2018-08-08
 */
public class Filter {

    public static void filterMenu(Menu menu, @ColorRes int colorFilter) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().mutate().setColorFilter(Res.getColour(colorFilter), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
