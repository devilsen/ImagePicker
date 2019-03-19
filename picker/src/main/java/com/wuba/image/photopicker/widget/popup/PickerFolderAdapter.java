package com.wuba.image.photopicker.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuba.image.photopicker.R;
import com.wuba.image.photopicker.data.ImageFolder;

import java.util.ArrayList;

/**
 * desc :
 * date : 2018/8/16
 *
 * @author : dongSen
 */
public class PickerFolderAdapter extends RecyclerView.Adapter<PickerFolderAdapter.ViewHolder> {

    private Context context;

    private ArrayList<ImageFolder> listData;

//    private final RequestOptions cropOptions;

    private OnItemFolderClickListener itemFolderClickListener;

    PickerFolderAdapter(Context context) {
        this.context = context;
//
//        cropOptions = new RequestOptions()
//                .placeholder(new ColorDrawable(Color.GRAY))
//                .error(new ColorDrawable(Color.GRAY))
//                .centerCrop();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_picker_item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context)
//                .load(listData.get(position).getCoverPath())
//                .apply(cropOptions)
//                .into(holder.imageView);

        Glide.with(context)
                .load(listData.get(position).getCoverPath())
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .centerCrop()
                .into(holder.imageView);

        holder.textView.setText(listData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public void setListData(ArrayList<ImageFolder> listData) {
        this.listData = listData;
    }

    public void setItemFolderClickListener(OnItemFolderClickListener itemFolderClickListener) {
        this.itemFolderClickListener = itemFolderClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_image_picker_folder);
            textView = itemView.findViewById(R.id.text_image_picker_folder_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position > -1 && itemFolderClickListener != null) {
                itemFolderClickListener.onFolderClick(position);
            }
        }
    }

    public interface OnItemFolderClickListener {
        void onFolderClick(int position);
    }
}
