package com.wuba.image.photopicker.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wuba.image.photopicker.R;

import java.util.ArrayList;

/**
 * desc :
 * date : 2018/8/15
 *
 * @author : dongSen
 */
public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {

//    private final RequestOptions cropOptions;

    private Context context;

    private ArrayList<String> listData;

    private int selectPosition;

    private OnItemClickListener onItemClickListener;

    public ImagePreviewAdapter(Context context, ArrayList<String> listData) {
        this.context = context;
        this.listData = listData;

//        cropOptions = new RequestOptions()
////                .placeholder(loadingResId)
////                .error(failResId)
//                .centerCrop()
//                .timeout(10000);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_picker_item_select_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context)
//                .load(listData.get(position))
//                .apply(cropOptions)
//                .into(holder.imageView);

        Glide.with(context)
                .load(listData.get(position))
                .centerCrop()
                .into(holder.imageView);

        holder.checkBox.setChecked(selectPosition == position);
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setSelectPosition(int selectPosition) {
        if (this.selectPosition == selectPosition)
            return;

        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_image_picker_preview);
            checkBox = itemView.findViewById(R.id.checkbox_image_picker_preview);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position > -1 && onItemClickListener != null) {
                onItemClickListener.onItemClick(listData.get(position));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String imagePath);
    }
}
