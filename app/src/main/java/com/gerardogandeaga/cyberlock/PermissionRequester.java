package com.gerardogandeaga.cyberlock;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author gerardogandeaga
 * created on 2018-08-13
 */
public class PermissionRequester {
    public static void requestReadExternalStorage(Activity activity, String permission) {
        ActivityCompat.requestPermissions(
                activity,
                new String[] { permission },
                1
        );
    }

    public static boolean canReadExternalStorage(Activity activity, String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
