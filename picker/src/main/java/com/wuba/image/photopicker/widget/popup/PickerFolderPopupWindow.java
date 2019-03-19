package com.wuba.image.photopicker.widget.popup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.wuba.image.photopicker.R;
import com.wuba.image.photopicker.data.ImageFolder;

import java.util.ArrayList;

/**
 * desc : show image folders
 * date : 2018/8/16
 *
 * @author : dongSen
 */
public class PickerFolderPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private PickerFolderAdapter folderAdapter;

    public PickerFolderPopupWindow(Context context, View anchorView, PickerFolderAdapter.OnItemFolderClickListener listener) {
        super(context, R.layout.image_picker_view_image_folder,
                anchorView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        folderAdapter.setItemFolderClickListener(listener);
    }

    @Override
    protected void initView() {
        View contentView = getContentView();

        View rootView = contentView.findViewById(R.id.layout_image_folder_root);
        rootView.setOnClickListener(this);

        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view_image_folder_content);
        folderAdapter = new PickerFolderAdapter(mContext);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(folderAdapter);

        setAnimationStyle(R.style.popupWindowAnimation);
        setOutsideTouchable(true);
        setFocusable(true);

    }

    public void setData(ArrayList<ImageFolder> folderArrayList) {
        folderAdapter.setListData(folderArrayList);
    }

    @Override
    public void show() {
        showAsDropDown(mAnchorView);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.layout_image_folder_root) {
            dismiss();
        }
    }
}
