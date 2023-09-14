package com.github.imageselect;

import android.content.ContentResolver;
import android.os.AsyncTask;

import com.androidx.picker.MediaFolder;
import com.androidx.picker.MediaLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 *
 * description:  加载
 */
public class LoadMediaTask extends AsyncTask<Void, Void, ArrayList<MediaFolder>> {
    private WeakReference<LoadMediaActivity> weakReference;
    private boolean loadVideo = false;

    public LoadMediaTask(@NonNull LoadMediaActivity activity) {
        this.weakReference = new WeakReference<>(activity);
        this.loadVideo = false;
    }

    public LoadMediaTask(@NonNull LoadMediaActivity activity, boolean loadVideo) {
        this.weakReference = new WeakReference<>(activity);
        this.loadVideo = loadVideo;
    }

    @Override
    protected ArrayList<MediaFolder> doInBackground(Void... voids) {
        LoadMediaActivity loadMediaActivity = weakReference.get();
        if (loadMediaActivity == null) {
            return null;
        }
        ContentResolver contentResolver = loadMediaActivity.getContentResolver();
        if (loadVideo) {
            return new MediaLoader.Builder(contentResolver)
                    .setOrder("date_added DESC")
                    .ofVideo()
                    .getFolders(null);
        } else {
            return new MediaLoader.Builder(contentResolver)
                    .setOrder("date_added DESC")
                    .ofImage()
                    .getFolders(null);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MediaFolder> mediaFolders) {
        super.onPostExecute(mediaFolders);
        LoadMediaActivity loadMediaActivity = weakReference.get();
        if (loadMediaActivity != null) {
            loadMediaActivity.onLoadFinished(mediaFolders);
        }
    }
}
