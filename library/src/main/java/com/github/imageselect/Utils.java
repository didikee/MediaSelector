package com.github.imageselect;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 *
 * description: 
 */
final class Utils {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param context context
     *            （DisplayMetrics类中属性density）
     * @return float cast to int
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param context context
     *            （DisplayMetrics类中属性density）
     * @return float cast to int
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px单位转换为sp
     *
     * @param pxValue need px
     * @param context context
     *         DisplayMetrics类中属性scaledDensity
     * @return float cast to int
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue need sp
     * @param context context
     *         DisplayMetrics类中属性scaledDensity
     * @return float cast to int
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取系统状态栏高度
     * @param context context
     * @return statusBar height
     */
    public static int getSystemStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取屏幕的宽高
     * @param context
     * @return
     */
    public static Pair<Integer, Integer> getWindowPixels(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return new Pair<Integer, Integer>(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static boolean isBottomItem(int spanCount, int itemCount, int itemPosition) {
        // 判断是否是最后一行
        int b = itemCount % spanCount;
        if (b == 0) {
            b = spanCount;
        }
        return itemPosition >= (itemCount - b);
    }

    public static Drawable createSelectCircle(Context context, boolean checked, int checkedColor) {
        GradientDrawable ovalDrawable = new GradientDrawable();
        ovalDrawable.setShape(GradientDrawable.OVAL);
        ovalDrawable.setColor(Color.parseColor("#40000000"));

        GradientDrawable ovalDrawable2 = new GradientDrawable();
        ovalDrawable2.setShape(GradientDrawable.OVAL);
        ovalDrawable2.setStroke(dp2px(context, 2), checked ? checkedColor : Color.WHITE);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                ovalDrawable, ovalDrawable2
        });
        final int padding = dp2px(context, 1);
        layerDrawable.setLayerInset(1, padding, padding, padding, padding);
        return layerDrawable;
    }

    public static int getAccentColor(@NonNull Activity activity) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = null;
        int color;
        try {
            a = activity.obtainStyledAttributes(typedValue.data,
                    new int[]{R.attr.colorAccent, R.attr.colorSecondary}
            );
            color = a.getColor(0, Color.TRANSPARENT);
            if (color != Color.TRANSPARENT) {
                return color;
            }
            color = a.getColor(1, Color.TRANSPARENT);
            if (color != Color.TRANSPARENT) {
                return color;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        return Color.BLUE;
    }

}
