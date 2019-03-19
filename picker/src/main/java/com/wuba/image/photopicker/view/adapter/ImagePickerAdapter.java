package com.wuba.image.photopicker.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wuba.image.photopicker.R;
import com.wuba.image.photopicker.data.DataHolder;
import com.wuba.image.photopicker.util.PickerUtil;
import com.wuba.image.photopicker.widget.checkbox.PickerCheckBox;

import java.util.ArrayList;

/**
 * author : dongSen
 * date : 2018/8/15 2:36 PM
 * desc : the list of image
 */
public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    private ArrayList<String> listData;

    private Context context;

//    private final RequestOptions cropOptions;

    private OnItemClickListener onItemClickListener;
    private final int size;

    public ImagePickerAdapter(Context context) {
        this.context = context;

        size = PickerUtil.getScreenWidth(context) / 4;

//        cropOptions = new RequestOptions()
//                .placeholder(new ColorDrawable(Color.GRAY))
//                .error(new ColorDrawable(Color.GRAY))
//                .centerCrop()
//                .override(size);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_picker_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = listData.get(position);

        if (imagePath == null)
            return;

//        Glide.with(context)
//                .load(imagePath)
//                .apply(cropOptions)
//                .into(holder.imageView);

        Glide.with(context)
                .load(imagePath)
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .dontAnimate()
                .centerCrop()
                .override(size, size)
                .into(holder.imageView);

        if (DataHolder.getInstance().isPickAvatar()) {
            holder.checkBox.setVisibility(View.GONE);
            return;
        }

        boolean checked = DataHolder.getInstance().imageIsSelected(position);

        holder.checkBox.setChecked(checked);
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public void setListData(ArrayList<String> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private ImageView imageView;
        private PickerCheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_image_picker_list);
            checkBox = itemView.findViewById(R.id.checkbox_image_picker_list);

            imageView.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            int position = getAdapterPosition();

            if (position == -1)
                return;

            if (id == R.id.checkbox_image_picker_list) {
                String imagePath = listData.get(position);

                PickerCheckBox checkBox = (PickerCheckBox) v;

                if (checkBox.isChecked()) {
                    boolean added = DataHolder.getInstance().addSelectImage(imagePath);

                    if (!added) {
                        PickerUtil.showToast(context, context.getString(R.string.image_picker_over_max_size, DataHolder.getInstance().getMaxSize()));

                        checkBox.setChecked(false);
                    }
                } else {
                    DataHolder.getInstance().removeSelectImage(imagePath);
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onItemChecked();
                }
            } else if (id == R.id.image_image_picker_list) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }

        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

        void onItemChecked();
    }
}
