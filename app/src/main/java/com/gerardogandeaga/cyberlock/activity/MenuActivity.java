package com.gerardogandeaga.cyberlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gerardogandeaga.cyberlock.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnImportActivity) public void importActivity() {
        Intent i = new Intent(this, ImageImportActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.btnViewerActivity) public void viewerActivity() {
        Intent i = new Intent(this, ImageGalleryActivity.class);
        startActivity(i);
        finish();
    }

}
