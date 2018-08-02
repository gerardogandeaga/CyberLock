package com.gerardogandeaga.cyberlock;

import android.os.CountDownTimer;

import com.gerardogandeaga.cyberlock.SecureActivity;
import com.gerardogandeaga.cyberlock.activity.AuthenticationActivity;

/**
 * @author gerardogandeaga
 * created on 2018-07-29
 *
 * handles application locking
 *
 * def:
 * (1) suspended app : when the cyberlock is no longer the main view. user could have pressed menu button
 * power button, or left application in background.
 *
 * todo create pause and start listeners
 */
public class LockManager {
    private boolean mIsLocked;
    private CountDownTimer mLockTimer;

    public LockManager() {
        mLockTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mIsLocked = true;
                System.out.println("app locked!");
            }
        };
    }

    // activity event functions

    /**
     * handles auto logout events when starting the activity
     * either:
     * heading to login screen or..
     * continuing
     */
    public void onStart(SecureActivity activity) {
        mLockTimer.cancel();
        if (mIsLocked) {
            // go to login
            activity.startIntent(AuthenticationActivity.class);
        }
    }

    /**
     * starts the logout timer
     */
    public void onPause(SecureActivity activity) {
        // if user has suspended app (see def 1)
        if (!activity.isFinishing()) {
            mLockTimer.start();
        }
    }
}
