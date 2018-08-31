package com.gerardogandeaga.cyberlock;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author gerardogandeaga
 * created on 2018-07-29
 *
 * SecureActivity acts like almost an overlay activity that will run auto log out
 * action and traffic timers. this activit handles complex background logic and general process for activity
 * while children classes focus on user interaction and views
 *
 * todo implement permission requester
 * todo auto logout
 * todo back pressed manager - stack?
 * todo implement in all activities
 */
public abstract class SecureActivity extends AppCompatActivity {
    private static final String TAG = "SecureActivity";

    // only created once
    private static final LockManager LOCK_MANAGER = new LockManager();
    private static final IntentStack INTENT_STACK = new IntentStack(); // intent history only allows you to go backwards not forward

    private static Intent NewIntent;

    // views
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // views and create

    /**
     * makes sure that every activity will init the toolbar.
     */
    protected abstract void bindView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // activity states

    @Override
    public void onBackPressed() {
        prevIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        LOCK_MANAGER.onStart(this);
    }

    @Override
    protected void onPause() {
//        LOCK_MANAGER.onPause(this);
        super.onPause();
    }

    // intents

    /**
     * inits the NewIntent which resembles the future activity
     * @param cls class Activity
     */
    protected void newIntent(Class<?> cls) {
        NewIntent = new Intent(this, cls);
    }

    /**
     * starts the NewIntent with already initialized intent
     */
    protected void startIntent() {
        startActivity(NewIntent);
        NewIntent = null;
    }

    /**
     * inits intent
     * @param cls class Activity
     */
    protected void startIntent(Class<?> cls) {
        newIntent(cls);
        startIntent();
    }

    /**
     * go to last activity there exists
     */
    private void prevIntent() {
        startActivity(INTENT_STACK.pop());
        NewIntent = null;
    }

    // action bars

    /**
     * initializes the support action bar
     */
    protected void initActionBar() {
        setSupportActionBar(mToolbar);
    }

    protected void setActionBar(String title, String subTitle, Drawable icon) {
        if (getSupportActionBar() == null) {
            initActionBar();
        }

        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setActionBarTitle(title);
        setActionBarSubTitle(subTitle);
        setActionBarIcon(icon);
    }

    protected void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected void setActionBarSubTitle(String subTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    protected void setActionBarIcon(Drawable icon) {
        if (getSupportActionBar() != null) {
            // no icon
            getSupportActionBar().setHomeButtonEnabled(icon != null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(icon != null);
            getSupportActionBar().setHomeAsUpIndicator(icon);
        }
    }

    /**
     * IntentStack is an object that keeps a list of intents (Activities) that the user has traversed through.
     * this class controls how the back buttons react when the specified action is to go to the previous screen.
     */
    private static class IntentStack {
        private ArrayList<Intent> mIntentArrayList;

        IntentStack() {
            mIntentArrayList = new ArrayList<>();
        }

        /**
         * pushes intent to the stack
         */
        public void push(Intent intent) {
            // don't want more than one instance of the intent in the stack
            if (!mIntentArrayList.contains(intent)) {
                mIntentArrayList.add(intent);
            }
        }

        /**
         * pops intent from the stack
         */
        private Intent pop() {
            if (!mIntentArrayList.isEmpty()) {
                int i = mIntentArrayList.size() - 1;
                Intent intent = mIntentArrayList.get(i);
                mIntentArrayList.remove(i); // pop from list
                return intent;
            }
            return null;
        }
    }
}
