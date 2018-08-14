package com.gerardogandeaga.cyberlock;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author gerardogandeaga
 * created on 2018-08-13
 */
public class PermissionRequester {

    public static void requestReadExternalStorage(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                1
        );
    }

    public static boolean canReadExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
