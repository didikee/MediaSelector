package com.github.imageselect;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidx.picker.MediaItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * user author: didikee
 * create time: 4/17/20 10:53 AM
 * description: 选择照片的内容显示的adapter
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private List<MediaItem> data;
    private Context context;
    private int shadowColor = Color.parseColor("#7F000000");
    private final int spanCount;
    private final boolean singleMode;
    private Drawable uncheckedDrawable, checkedDrawable;
    private int colorAccent;

    private OnContentAdapterListener onContentAdapterListener;
    private ArrayList<MediaItem> selectItems = new ArrayList<>();
    private boolean showFileSize;

    public ContentAdapter(int spanCount, boolean singleMode) {
        this.spanCount = spanCount;
        this.singleMode = singleMode;
    }

    public void setColorAccent(int colorAccent) {
        this.colorAccent = colorAccent;
    }

    public void setData(List<MediaItem> data) {
        this.data = data;
    }

    public void setOnContentAdapterListener(OnContentAdapterListener onContentAdapterListener) {
        this.onContentAdapterListener = onContentAdapterListener;
    }

    public ArrayList<MediaItem> getSelectItems() {
        return selectItems;
    }

    public int getSelectCount() {
        return selectItems.size();
    }

    public void setShowFileSize(boolean showFileSize) {
        this.showFileSize = showFileSize;
    }

    private boolean isChecked(int order) {
        return order > 0;
    }

    private int orderOfSelection(MediaItem mediaItem) {
        return selectItems.indexOf(mediaItem) + 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        uncheckedDrawable = Utils.createSelectCircle(context, false, colorAccent);
        checkedDrawable = Utils.createSelectCircle(context, true, colorAccent);
        View inflate = LayoutInflater.from(context).inflate(R.layout.adapter_image_seletor_content, viewGroup, false);
        // 确定item的高度
        Integer phoneWidth = Utils.getWindowPixels(context).first;
        // TODO 间距待完善
        int marginOffset = Utils.dp2px(context, 10);
        int itemHeight = (int) ((phoneWidth - marginOffset) * 1f / spanCount);
        View rootView = inflate.findViewById(R.id.root);
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
        layoutParams.height = itemHeight;
        rootView.setLayoutParams(layoutParams);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final MediaItem mediaItem = data.get(i);
        Glide.with(context).load(mediaItem.getUri()).into(viewHolder.imageView);
        if (singleMode) {
            viewHolder.checkView.setVisibility(View.GONE);
            viewHolder.checkLayerLayout.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onContentAdapterListener != null) {
                        onContentAdapterListener.onItemClickInSingleMode(v, viewHolder.getAdapterPosition(), mediaItem);
                    }
                }
            });
        } else {
            viewHolder.checkLayerLayout.setVisibility(View.VISIBLE);
            viewHolder.checkView.setVisibility(View.VISIBLE);
            int orderOfSelection = orderOfSelection(mediaItem);
            final boolean checked = isChecked(orderOfSelection);
            if (checked) {
                viewHolder.checkBgView.setImageDrawable(checkedDrawable);
                viewHolder.checkSelectView.setVisibility(View.VISIBLE);
                viewHolder.checkSelectView.setText(String.valueOf(orderOfSelection));
                viewHolder.checkLayerLayout.setBackgroundColor(shadowColor);
            } else {
                viewHolder.checkBgView.setImageDrawable(uncheckedDrawable);
                viewHolder.checkSelectView.setVisibility(View.GONE);
                viewHolder.checkLayerLayout.setBackgroundDrawable(null);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onContentAdapterListener != null) {
                        if (checked) {
                            selectItems.remove(mediaItem);
                        } else {
                            selectItems.add(mediaItem);
                        }
                        notifyDataSetChanged();
//                        notifyItemChanged(viewHolder.getAdapterPosition());
                        onContentAdapterListener.onItemSelectChange();
                    }
                }
            });
        }
        if (showFileSize) {
            viewHolder.detailLayout.setVisibility(View.VISIBLE);
            viewHolder.tvSize.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
            viewHolder.tvResolution.setText(String.format(Locale.getDefault(), "%d x %d", mediaItem.getWidth(), mediaItem.getHeight()));
        } else {
            viewHolder.detailLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, checkBgView;
        private View checkView;
        private TextView checkSelectView;
        // check 遮罩
        private View checkLayerLayout;
        // 文件大小
        private View detailLayout;
        private TextView tvSize, tvResolution;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            checkView = itemView.findViewById(R.id.check);
            checkBgView = itemView.findViewById(R.id.check_bg);
            checkSelectView = itemView.findViewById(R.id.check_selected);

            detailLayout = itemView.findViewById(R.id.detail_layout);
            tvSize = itemView.findViewById(R.id.tv_size);
            tvResolution = itemView.findViewById(R.id.tv_resolution);

            checkLayerLayout = itemView.findViewById(R.id.check_layout);
        }
    }

    public interface OnContentAdapterListener {

        void onItemSelectChange();

        void onItemClickInSingleMode(View view, int position, MediaItem mediaItem);

    }
}
