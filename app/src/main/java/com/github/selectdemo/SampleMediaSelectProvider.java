package com.github.selectdemo;

import com.github.imageselect.LoadMediaActivity;
import com.github.imageselect.MediaSelectProvider;
import com.github.imageselect.empty.EmptyLayoutHolder;

/**
 * description:
 */
class SampleMediaSelectProvider implements MediaSelectProvider {
   @Override
   public void loadMedias(LoadMediaActivity mediaActivity) {

   }

   @Override
   public EmptyLayoutHolder setEmpty() {
      return null;
   }
}
