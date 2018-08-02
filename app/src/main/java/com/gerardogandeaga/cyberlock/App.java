package com.gerardogandeaga.cyberlock;

import android.app.Application;
import android.content.Context;

/**
 * @author gerardogandeaga
 * created on 2018-07-30
 *
 * App class preserves constants that do not need to be initiliazed more than once across application life cycles,
 * such as easy contexts and single db inits
 */
public class App extends Application {
    private static Context Context;

    @Override
    public void onCreate() {
        super.onCreate();

        Context = this;
    }

    public static Context getContext() {
        return Context;
    }
}
