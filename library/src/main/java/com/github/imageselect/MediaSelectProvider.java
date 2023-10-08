package com.github.imageselect;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.androidx.picker.MediaFolder;
import com.github.imageselect.empty.EmptyLayoutHolder;

import java.util.ArrayList;

/**
 * description:
 */
public interface MediaSelectProvider {
    @WorkerThread
    ArrayList<MediaFolder> loadMedias(@NonNull LoadMediaActivity mediaActivity);

    EmptyLayoutHolder setEmpty();
}
