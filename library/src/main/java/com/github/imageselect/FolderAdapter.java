package com.github.imageselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidx.picker.MediaFolder;
import com.androidx.picker.MediaItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * user author: didikee
 * create time: 4/17/20 11:47 AM
 * description: 
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private List<MediaFolder> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public void setData(List<MediaFolder> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.adapter_image_selector_folder, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final MediaFolder mediaFolder = data.get(i);
        ArrayList<MediaItem> items = mediaFolder.items;
        int size = 0;
        if (items != null && items.size() > 0) {
            size = items.size();
            Glide.with(context).load(items.get(0).getUri()).into(viewHolder.imageView);
        } else {
            // TODO folder cover not found
        }
        viewHolder.title.setText(mediaFolder.name);
        viewHolder.desc.setText(String.format(Locale.getDefault(), "%d%s", size, "项"));

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, viewHolder.getAdapterPosition(), mediaFolder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private View rootView;
        private TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            rootView = itemView.findViewById(R.id.root);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position, MediaFolder mediaFolder);
    }
}
