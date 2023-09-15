package com.github.imageselect;

import android.content.ContentResolver;

import com.androidx.picker.MediaFolder;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 *
 * description: 
 */
public interface LoadMediaActivity {
    ContentResolver getContentResolver();

    void onPreLoad();

    void onLoadFinished(@Nullable ArrayList<MediaFolder> allMediaFolders);
}
