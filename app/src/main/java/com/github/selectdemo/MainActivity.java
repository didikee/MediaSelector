package com.github.selectdemo;

import android.os.Bundle;
import android.view.View;


import com.github.imageselect.ImageSelector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * description:
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View btCustomMimeTypes = findViewById(R.id.bt_custom_mimetypes);
        btCustomMimeTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMimeTypes();
            }
        });
    }

    private void customMimeTypes() {
        ImageSelector.Options options = new ImageSelector.Options()
                .targetMimetypes(new String[]{
                        "image/heic", "image/heif"
                }).maxSelectCount(99)
                .showDetail(true);
        new ImageSelector().start(this, options);
    }
}
