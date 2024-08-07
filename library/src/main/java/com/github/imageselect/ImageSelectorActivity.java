package com.github.imageselect;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidx.LogUtils;
import com.androidx.picker.MediaFolder;
import com.androidx.picker.MediaItem;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * user author: didikee
 * create time: 4/16/20 5:50 PM
 * description: 选择照片，支持单选和多选
 */
public class ImageSelectorActivity extends BaseMediaSelectActivity implements View.OnClickListener, LoadMediaActivity {

    private RecyclerView contentRecyclerView;
    private RecyclerView folderRecyclerView;
    private ImageView backImageView;
    private TextView titleTextView;
    private Button btDone;
    private View rootLayout, titleAppBarLayout, bottomActionLayout, loadingProgressBar;
    private FrameLayout emptyLayoutContainer;
    private ContentAdapter contentAdapter;
    private boolean singleMode;
    private FolderAdapter folderAdapter;
    private ImageSelector.Options options;
    private int colorAccent = Color.BLUE;

    /**
     * 这个类负责图片选择的所有自定义项的支持，并可以交给外部去实现，达到完全自定义的目的
     */
    private MediaSelectProvider provider;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_activity_image_selector);
        hideActionBar();
        Intent intent = getIntent();
        if (intent != null) {
            options = intent.getParcelableExtra(ImageSelector.DATA_OPTIONS);
        }
        if (options == null) {
            finish();
            return;
        }
        String providerClzName = options.providerClzName;
        if (!TextUtils.isEmpty(providerClzName)) {
            try {
                // 使用反射获取Class对象
                Class<?> clazz = Class.forName(providerClzName);
                // 使用Class对象创建实例
                Object instance = clazz.newInstance();

                // 这里可以根据需要使用实例进行操作
                if (instance instanceof MediaSelectProvider) {
                    provider = (MediaSelectProvider) instance;
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            //TODO 提供一个默认的实现
        }
        if (provider == null) {
            finish();
            return;
        }
        provider.init(this);

        // TODO: 设置主题色
        colorAccent = ContextCompat.getColor(this, R.color.colorAccent);
        singleMode = options.maxSelectCount <= 1;
        contentRecyclerView = findViewById(R.id.recyclerView);
        folderRecyclerView = findViewById(R.id.recyclerViewFolder);
        backImageView = findViewById(R.id.back);
        titleTextView = findViewById(R.id.title);
        btDone = findViewById(R.id.done);
        rootLayout = findViewById(R.id.root);
        titleAppBarLayout = findViewById(R.id.appbar);
        bottomActionLayout = findViewById(R.id.bottom);
        loadingProgressBar = findViewById(R.id.loading);
        emptyLayoutContainer = findViewById(R.id.layout_empty);

        backImageView.setOnClickListener(this);
        titleTextView.setOnClickListener(this);
        btDone.setOnClickListener(this);


        bottomActionLayout.setVisibility(singleMode ? View.GONE : View.VISIBLE);
        View emptyLayout = provider.getEmptyLayout(this);
        if (emptyLayout != null) {
            emptyLayoutContainer.addView(emptyLayout);
            hideEmptyLayout();
        }

        setupContentList();
        setupFolderList();
        updateSelectCountUi();


        // load image
        new LoadMediaTask(this, provider).execute();
    }

    private void setupContentList() {
        final int spanCount = 3;
        final int margin = Utils.dp2px(this, 2);
        final int bottomMargin = Utils.dp2px(this, 80);
        contentRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        contentRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                boolean bottomItem = Utils.isBottomItem(spanCount,
                        state.getItemCount(), childAdapterPosition);
                if (bottomItem) {
                    outRect.left = margin;
                    outRect.right = margin;
                    outRect.top = margin;
                    outRect.bottom = bottomMargin;
                } else {
                    outRect.left = margin;
                    outRect.right = margin;
                    outRect.top = margin;
                    outRect.bottom = margin;
                }
            }
        });
        contentAdapter = new ContentAdapter(spanCount, singleMode);
        contentAdapter.setShowFileSize(options.showDetail);
        contentAdapter.setColorAccent(colorAccent);
        contentAdapter.setOnContentAdapterListener(new ContentAdapter.OnContentAdapterListener() {
            @Override
            public void onItemSelectChange() {
                updateSelectCountUi();
            }

            @Override
            public void onItemClickInSingleMode(View view, int position, MediaItem mediaItem) {
                onDone(mediaItem);
            }
        });
        contentRecyclerView.setItemAnimator(null);
        contentRecyclerView.setAdapter(contentAdapter);
    }

    private void setupFolderList() {
        Context context = ImageSelectorActivity.this;
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        folderAdapter = new FolderAdapter();
        folderAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MediaFolder mediaFolder) {
                toggleFolderUi();
                onFolderChanged(mediaFolder);
            }
        });
        folderRecyclerView.setAdapter(folderAdapter);
    }

    private void onFolderChanged(MediaFolder mediaFolder) {
        if (mediaFolder == null) {
            return;
        }
        if (contentAdapter != null) {
            contentAdapter.setData(mediaFolder.items);
            contentAdapter.notifyDataSetChanged();
            updateSelectFolderName(mediaFolder.name);
        }
    }

    private void updateSelectFolderName(String folderName) {
        if (titleTextView == null) {
            return;
        }
        if (TextUtils.isEmpty(folderName)) {
            titleTextView.setText("");
        } else {
            titleTextView.setText(folderName);
        }
    }

    private void updateSelectCountUi() {
        if (contentAdapter != null && btDone != null) {
            int selectCount = contentAdapter.getSelectCount();
            String text = provider.getBottomActionButtonText();
            if (selectCount > 0) {
                btDone.setEnabled(true);
                String format = String.format(Locale.getDefault(), "%s(%d)", text, selectCount);
                btDone.setText(format);
            } else {
                btDone.setEnabled(false);
                btDone.setText(text);
            }

        }
    }

    private void toggleFolderUi() {
        if (bottomActionLayout == null) {
            return;
        }
        int height = rootLayout.getHeight() - titleAppBarLayout.getHeight() - bottomActionLayout.getHeight();
        boolean folderContentOpened = isFolderContentOpened();
        ValueAnimator valueAnimator;
        if (folderContentOpened) {
            valueAnimator = ValueAnimator.ofInt(height, 0);
        } else {
            valueAnimator = ValueAnimator.ofInt(0, height);
        }
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = folderRecyclerView.getLayoutParams();
                layoutParams.height = animatedValue;
                folderRecyclerView.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    private boolean isFolderContentOpened() {
        return folderRecyclerView != null && folderRecyclerView.getHeight() > 0;
    }

    /**
     * 完成选图操作
     *
     * @param mediaItem
     */
    protected void onDone(@Nullable MediaItem mediaItem) {
        if (singleMode) {
            ArrayList<MediaItem> selectItems = new ArrayList<>();
            selectItems.add(mediaItem);
            prepareResult(false, selectItems);
            finish();
        } else {
            // ignore mediaItem
            ArrayList<MediaItem> selectItems = null;
            if (contentAdapter != null) {
                selectItems = contentAdapter.getSelectItems();
            }
            prepareResult(false, selectItems);
            finish();
        }
    }

    private void prepareResult(boolean isCancelled, ArrayList<MediaItem> selectItems) {
        Intent data = new Intent();
        if (isCancelled) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            if (selectItems == null || selectItems.size() == 0) {
                setResult(Activity.RESULT_CANCELED);
            } else {
                data.putParcelableArrayListExtra(ImageSelector.SELECT_MEDIA, selectItems);
                setResult(Activity.RESULT_OK, data);
            }
        }
    }

    @Override
    public void onPreLoad() {
        if (isFinishing()) {
            return;
        }
        if (loadingProgressBar != null && contentRecyclerView != null) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            contentRecyclerView.setVisibility(View.INVISIBLE);
        }
        hideEmptyLayout();
    }

    @Override
    public void onLoadFinished(ArrayList<MediaFolder> allMediaFolders) {
        LogUtils.d("On Media load finished.");
        if (isFinishing()) {
            return;
        }
        // hide progress bar
        if (loadingProgressBar != null) {
            loadingProgressBar.setVisibility(View.GONE);
        }
        if (allMediaFolders == null || allMediaFolders.size() == 0) {
            showEmptyLayout();
            return;
        }

        if (contentRecyclerView != null) {
            contentRecyclerView.setVisibility(View.VISIBLE);
        }
        if (folderAdapter != null) {
            folderAdapter.setData(allMediaFolders);
            folderAdapter.notifyDataSetChanged();
        }
        MediaFolder mediaFolder = allMediaFolders.get(0);
        onFolderChanged(mediaFolder);
    }

    @Override
    public void onBackPressed() {
        prepareResult(true, null);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == backImageView) {
            onBackPressed();
            return;
        }
        if (v == btDone) {
            onDone(null);
            return;
        }
        if (v == titleTextView) {
            toggleFolderUi();
            return;
        }
    }

    private void showEmptyLayout() {
        emptyLayoutContainer.setVisibility(View.VISIBLE);
    }

    private void hideEmptyLayout() {
        emptyLayoutContainer.setVisibility(View.GONE);
    }
}
