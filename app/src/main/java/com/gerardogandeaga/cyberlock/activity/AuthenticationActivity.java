package com.gerardogandeaga.cyberlock.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gerardogandeaga.cyberlock.R;
import com.gerardogandeaga.cyberlock.SecureActivity;

import butterknife.ButterKnife;

/**
 * @author gerardogandeaga
 * created on 2018-07-31
 */
enum AuthState {
    LOGIN,
    REGISTER
}
public class AuthenticationActivity extends SecureActivity {
    private AuthState mAuthState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_only);
    }

    @Override
    protected void bindView() {
        ButterKnife.bind(this);
    }
}
