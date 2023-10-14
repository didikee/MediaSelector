package com.github.imageselect;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.androidx.picker.MediaFolder;
import com.github.imageselect.empty.EmptyLayoutHolder;

import java.util.ArrayList;

/**
 * description:
 */
public interface MediaSelectProvider {

    void init(@NonNull Context context);
    @WorkerThread
    ArrayList<MediaFolder> loadMedias(@NonNull LoadMediaActivity mediaActivity);

    EmptyLayoutHolder setEmpty();

    @Nullable
    Context getContext();

    String getBottomActionButtonText();
}
