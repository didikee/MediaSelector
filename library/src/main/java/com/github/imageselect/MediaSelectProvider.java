package com.github.imageselect;

import com.github.imageselect.empty.EmptyLayoutHolder;

/**
 * description:
 */
public interface MediaSelectProvider {

    void loadMedias(LoadMediaActivity mediaActivity);

    EmptyLayoutHolder setEmpty();
}
