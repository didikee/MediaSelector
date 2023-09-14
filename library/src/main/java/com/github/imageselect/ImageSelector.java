package com.github.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidx.picker.MediaItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 * description: 
 */
public final class ImageSelector {
    public static final int VIDEO = 0;
    public static final int IMAGE = 1;
    public static final int IMAGE_WITHOUT_GIF = 2;
    public static final int IMAGE_GIF = 3;

    public static final int REQUEST_MEDIA = 101;


    public static final String DATA_OPTIONS = "data_options";
    public static final String SELECT_MEDIA = "select_media";
    public static final String MEDIA_TYPE = "media_type";
    public static final String MAX_COUNT = "max_count";
    public static final String IMAGE_SHOW_DETAIL = "image_detail";

    public static class Options implements Parcelable {
        private int mediaType;
        int maxSelectCount = 1;/*1: 单选,大于1则为多选*/
        /*默认不显示文件详情*/
        boolean showDetail = false;

        public Options() {
        }


        protected Options(Parcel in) {
            mediaType = in.readInt();
            maxSelectCount = in.readInt();
            showDetail = in.readByte() != 0;
        }

        public static final Creator<Options> CREATOR = new Creator<Options>() {
            @Override
            public Options createFromParcel(Parcel in) {
                return new Options(in);
            }

            @Override
            public Options[] newArray(int size) {
                return new Options[size];
            }
        };

        public Options videos() {
            this.mediaType = VIDEO;
            return this;
        }

        public Options images() {
            this.mediaType = IMAGE;
            return this;
        }

        public Options gif() {
            this.mediaType = IMAGE_GIF;
            return this;
        }

        public Options imagesWithoutGif() {
            this.mediaType = IMAGE_WITHOUT_GIF;
            return this;
        }

        public Options maxSelectCount(int max) {
            this.maxSelectCount = Math.max(1, max);
            return this;
        }

        public Options showDetail(boolean showDetail) {
            this.showDetail = showDetail;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mediaType);
            dest.writeInt(maxSelectCount);
            dest.writeByte((byte) (showDetail ? 1 : 0));
        }
    }

    public void start(@NonNull Activity activity, @Nullable Options options) {
        try {
            Intent intent = new Intent(activity, ImageSelectorActivity.class);
            intent.putExtra(DATA_OPTIONS, options);
            activity.startActivityForResult(intent, REQUEST_MEDIA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static ArrayList<MediaItem> getResult(Intent intent) {
        ArrayList<MediaItem> result = new ArrayList<>();
        if (intent != null) {
            ArrayList<MediaItem> medias = intent.getParcelableArrayListExtra(ImageSelector.SELECT_MEDIA);
            if (medias != null) {
                result.addAll(medias);
            }
        }
        return result;
    }


}
