package com.github.imageselect;

import android.os.AsyncTask;

import com.androidx.picker.MediaFolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

/**
 * description:  加载
 */
public abstract class AbsLoadMedia extends AsyncTask<Void, Void, ArrayList<MediaFolder>> {
    private WeakReference<LoadMediaActivity> weakReference;

    public AbsLoadMedia(@NonNull LoadMediaActivity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    protected ArrayList<MediaFolder> doInBackground(Void... voids) {
        LoadMediaActivity loadMediaActivity = weakReference.get();
        if (loadMediaActivity == null) {
            return null;
        }
        return loadMedias();
    }

    @Nullable
    @WorkerThread
    protected abstract ArrayList<MediaFolder> loadMedias();

    @Override
    protected void onPostExecute(ArrayList<MediaFolder> mediaFolders) {
        super.onPostExecute(mediaFolders);
        LoadMediaActivity loadMediaActivity = weakReference.get();
        if (loadMediaActivity != null) {
            loadMediaActivity.onLoadFinished(mediaFolders);
        }
    }
}
