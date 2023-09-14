//package com.github.imageselect;
//
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.androidx.picker.MediaFolder;
//import com.androidx.picker.MediaItem;
//import com.google.android.material.appbar.AppBarLayout;
//
//import java.util.ArrayList;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
///**
// *
// * description:
// */
//public class MultiSelectableActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private AppBarLayout appbar;
//    private LinearLayout layoutRoot;
//    private ImageView backView;
//    private TextView titleTextView;
//    private RecyclerView contentRecyclerView;
//    private ProgressBar progressCircular;
//    private RelativeLayout layoutPanel;
//    private LinearLayout layoutSelect;
//    private ImageView ivDelete;
//    private TextView tvContent;
//    private AppCompatButton btNext;
//    private RecyclerView recyclerViewSelect;
//    private RecyclerView recyclerViewFolder;
//
//    private ContentAdapter contentAdapter;
//
//    // 参数
//    private ImageSelector.Options options;
//    private int folderAnimeDuration = 250;//ms
//    private int contentColor = Color.WHITE;
//    private int selectContentBackgroundColor = Color.parseColor("#F2FFFFFF");
//    private int selectLayoutHeight = 200; //dp
//
//    private FolderAdapter folderAdapter;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.default_activity_image_select_reselectable);
//
//        Intent intent = getIntent();
//        if (intent != null) {
//            options = intent.getParcelableExtra("data");
//        }
//
//        layoutRoot = findViewById(R.id.root);
//        appbar = findViewById(R.id.appbar);
//        backView = findViewById(R.id.back);
//        titleTextView = findViewById(R.id.title);
//        contentRecyclerView = findViewById(R.id.recyclerView);
//        progressCircular = findViewById(R.id.progress_circular);
//        layoutPanel = findViewById(R.id.layout_panel);
//        layoutSelect = findViewById(R.id.layout_select);
//        ivDelete = findViewById(R.id.iv_delete);
//        tvContent = findViewById(R.id.tv_content);
//        btNext = findViewById(R.id.bt_next);
//        recyclerViewSelect = findViewById(R.id.recyclerViewSelect);
//        recyclerViewFolder = findViewById(R.id.recyclerViewFolder);
//
//        backView.setOnClickListener(this);
//        titleTextView.setOnClickListener(this);
//
//        recyclerViewFolder.setBackgroundColor(contentColor);
//        layoutRoot.setBackgroundColor(contentColor);
//        layoutSelect.setBackgroundColor(selectContentBackgroundColor);
//
//        ViewGroup.LayoutParams layoutParams = layoutSelect.getLayoutParams();
//        layoutParams.height = Utils.dp2px(this, selectLayoutHeight);
//        layoutSelect.setLayoutParams(layoutParams);
//
//
//        setupContentList();
//        setupFolderContent();
//        showLoadingContent();
//        loadData();
//
//    }
//
//
//    private void loadData() {
////        new LoadMediaTask(this, false).execute();
//    }
//
//    public void onLoadFinished(@Nullable ArrayList<MediaFolder> mediaFolders) {
//        if (mediaFolders == null || mediaFolders.size() == 0) {
//            showLoadingContent();
//            Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        showListContent();
//        if (folderAdapter != null) {
//            folderAdapter.setData(mediaFolders);
//            folderAdapter.notifyDataSetChanged();
//        }
//        onFolderChanged(mediaFolders.get(0));
//    }
//
//    private void showLoadingContent() {
//        if (progressCircular != null) {
//            progressCircular.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void showListContent() {
//        if (contentRecyclerView != null) {
//            contentRecyclerView.setVisibility(View.VISIBLE);
//            progressCircular.setVisibility(View.GONE);
//        }
//    }
//
//    private void setupContentList() {
//        final int spanCount = 3;
//        final int margin = Utils.dp2px(this, 2);
//        final int bottomMargin;
//        boolean multiSelectMode = true;
//        final int defaultBottomOffset = 16;
//        if (multiSelectMode) {
//            bottomMargin = Utils.dp2px(this, selectLayoutHeight + defaultBottomOffset);
//        } else {
//            bottomMargin = Utils.dp2px(this, defaultBottomOffset);
//        }
//        contentRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
//        contentRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
//                                       @NonNull RecyclerView parent,
//                                       @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                int childAdapterPosition = parent.getChildAdapterPosition(view);
//                boolean bottomItem = Utils.isBottomItem(spanCount,
//                        state.getItemCount(), childAdapterPosition);
//                if (bottomItem) {
//                    outRect.left = margin;
//                    outRect.right = margin;
//                    outRect.top = margin;
//                    outRect.bottom = bottomMargin;
//                } else {
//                    outRect.left = margin;
//                    outRect.right = margin;
//                    outRect.top = margin;
//                    outRect.bottom = margin;
//                }
//            }
//        });
//        final boolean singleMode = false;
//        contentAdapter = new ContentAdapter(spanCount, singleMode);
//        contentAdapter.setShowFileSize(false);
//        contentAdapter.setOnContentAdapterListener(new ContentAdapter.OnContentAdapterListener() {
//            @Override
//            public void onItemSelectChange() {
//                updateSelectCountUi();
//            }
//
//            @Override
//            public void onItemClickInSingleMode(View view, int position, MediaItem mediaItem) {
//
//            }
//        });
//        contentRecyclerView.setItemAnimator(null);
//        contentRecyclerView.setAdapter(contentAdapter);
//    }
//
//    private void setupFolderContent() {
//        Context context = MultiSelectableActivity.this;
//        recyclerViewFolder.setLayoutManager(new LinearLayoutManager(context,
//                LinearLayoutManager.VERTICAL, false));
//        folderAdapter = new FolderAdapter();
//        folderAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, MediaFolder mediaFolder) {
//                toggleFolderUi();
//                onFolderChanged(mediaFolder);
//            }
//        });
//        recyclerViewFolder.setAdapter(folderAdapter);
//    }
//
//    private void toggleFolderUi() {
//        if (layoutPanel == null) {
//            return;
//        }
//        int height = layoutPanel.getHeight() - layoutSelect.getHeight();
//        boolean folderContentOpened = isFolderContentOpened();
//        ValueAnimator valueAnimator;
//        if (folderContentOpened) {
//            valueAnimator = ValueAnimator.ofInt(height, 0);
//        } else {
//            valueAnimator = ValueAnimator.ofInt(0, height);
//        }
//        valueAnimator.setDuration(250);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animatedValue = (int) animation.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = recyclerViewFolder.getLayoutParams();
//                layoutParams.height = animatedValue;
//                recyclerViewFolder.setLayoutParams(layoutParams);
//            }
//        });
//        valueAnimator.start();
//    }
//
//
//    private boolean isFolderContentOpened() {
//        return recyclerViewFolder != null && recyclerViewFolder.getHeight() > 0;
//    }
//
//    private void onFolderChanged(MediaFolder mediaFolder) {
//        if (mediaFolder == null) {
//            return;
//        }
//        if (contentAdapter != null) {
//            contentAdapter.setData(mediaFolder.items);
//            contentAdapter.notifyDataSetChanged();
//            updateSelectFolderName(mediaFolder.name);
//        }
//    }
//
//    private void updateSelectFolderName(String folderName) {
//        if (titleTextView == null) {
//            return;
//        }
//        if (TextUtils.isEmpty(folderName)) {
//            titleTextView.setText("");
//        } else {
//            titleTextView.setText(folderName);
//        }
//    }
//
//    private void updateSelectCountUi() {
////        if (contentAdapter != null && btDone != null) {
////            int selectCount = contentAdapter.getSelectCount();
////            String text = getString(R.string.next);
////            if (selectCount > 0) {
////                btDone.setEnabled(true);
////                String format = String.format(Locale.getDefault(), "%s(%d)", text, selectCount);
////                btDone.setText(format);
////            } else {
////                btDone.setEnabled(false);
////                btDone.setText(text);
////            }
////
////        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == backView) {
//            onBackPressed();
//            return;
//        }
////        if (v == btDone) {
////            onDone();
////            return;
////        }
//        if (v == titleTextView) {
//            toggleFolderUi();
//            return;
//        }
//    }
//}
