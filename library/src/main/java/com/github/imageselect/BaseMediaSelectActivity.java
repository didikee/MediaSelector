package com.github.imageselect;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * description:
 */
public class BaseMediaSelectActivity extends AppCompatActivity {


    public void hideActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && supportActionBar.isShowing()) {
            supportActionBar.hide();
        }
    }

}
