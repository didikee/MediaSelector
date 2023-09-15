package com.github.imageselect.empty;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * description:
 */
public interface EmptyLayoutHolder {
    View getEmptyView(@NonNull Context context);

    void show();

    void hide();
}
