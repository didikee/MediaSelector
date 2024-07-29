package com.github.imageselect;

import android.os.AsyncTask;

import com.androidx.LogUtils;
import com.androidx.picker.MediaFolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 *
 * description:  加载
 */
public class LoadMediaTask extends AsyncTask<Void, Void, ArrayList<MediaFolder>> {
    private WeakReference<LoadMediaActivity> weakReference;
    @NonNull
    private MediaSelectProvider provider;
    public LoadMediaTask(@NonNull LoadMediaActivity activity,@NonNull MediaSelectProvider provider) {
        this.weakReference = new WeakReference<>(activity);
        this.provider = provider;
    }

    @Override
    protected ArrayList<MediaFolder> doInBackground(Void... voids) {
        LoadMediaActivity loadMediaActivity = weakReference.get();
        if (loadMediaActivity == null) {
            return null;
        }
        LogUtils.d("Start load medias");
//        ContentResolver contentResolver = loadMediaActivity.getContentResolver();
//        if (loadVideo) {
//            return new MediaLoader.Builder(contentResolver)
//                    .setOrder("date_added DESC")
//                    .ofVideo()
//                    .getFolders(null);
//        } else {
//            return new MediaLoader.Builder(contentResolver)
//                    .setOrder("date_added DESC")
//                    .ofImage()
//                    .getFolders(null);
//        }
        return provider.loadMedias(loadMediaActivity);
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
