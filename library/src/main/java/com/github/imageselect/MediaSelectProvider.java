package com.github.imageselect;

import android.content.Context;
import android.view.View;

import com.androidx.picker.MediaFolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

/**
 * description:
 */
public interface MediaSelectProvider {

    void init(@NonNull Context context);
    @WorkerThread
    ArrayList<MediaFolder> loadMedias(@NonNull LoadMediaActivity mediaActivity);

    View getEmptyLayout(Context context);

    @Nullable
    Context getContext();

    String getBottomActionButtonText();
}
