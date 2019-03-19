package com.wuba.image.photopicker.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wuba.image.photopicker.R;
import com.wuba.image.photopicker.data.DataHolder;
import com.wuba.image.photopicker.data.ImageFolder;
import com.wuba.image.photopicker.data.task.ImageAsyncTask;
import com.wuba.image.photopicker.data.task.LoadImageTask;
import com.wuba.image.photopicker.observer.PickerObserver;
import com.wuba.image.photopicker.observer.PickerObserverHolder;
import com.wuba.image.photopicker.util.PickerSpaceItemDecoration;
import com.wuba.image.photopicker.util.PickerUtil;
import com.wuba.image.photopicker.view.adapter.ImagePickerAdapter;
import com.wuba.image.photopicker.view.crop.Crop;
import com.wuba.image.photopicker.widget.checkbox.PickerOriginalCheckbox;
import com.wuba.image.photopicker.widget.popup.PickerFolderAdapter;
import com.wuba.image.photopicker.widget.popup.PickerFolderPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.wuba.image.photopicker.data.PickerConstants.INTENT_NAME_PICK_AVATAR;
import static com.wuba.image.photopicker.data.PickerConstants.INTENT_NAME_SELECT_NUM;
import static com.wuba.image.photopicker.view.PickerConstant.EXTRA_PREVIEW_SELECT;

/**
 * desc :
 * date : 2018/8/14
 *
 * @author : dongSen
 */
public class PickerActivity extends PickerBaseActivity implements
        EasyPermissions.PermissionCallbacks, ImageAsyncTask.Callback<ArrayList<ImageFolder>>,
        ImagePickerAdapter.OnItemClickListener, View.OnClickListener, PickerObserver {

    private static final int REQUEST_CODE_PERMISSION_TAKE_PHOTO = 100;
    private static final int REQUEST_CODE_PERMISSION_STORAGE_PHOTO = 101;

    private LoadImageTask mLoadPhotoTask;

    private AppCompatDialog mLoadingDialog;

    private ArrayList<ImageFolder> mImageFolderModels;
    private ImagePickerAdapter adapter;
    private TextView titleTxt;
    private TextView sendTxt;
    private TextView previewTxt;
    private PickerFolderPopupWindow imageFolderView;
    private View bottomLayout;
    private PickerOriginalCheckbox originalCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_activity_picker);

        initView();
        initData(getIntent());

        requestStoragePermission();

        PickerObserverHolder.getInstance().addObserver(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNum(DataHolder.getInstance().getSelectListSize());
        setOriginChecked();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Crop.REQUEST_CROP) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelLoadPhotoTask();
        DataHolder.getInstance().clearSelect(this);
        PickerObserverHolder.getInstance().removeObserver(this);
    }

    @Override
    public void update() {
        DataHolder.getInstance().returnSelectImages(this);
        finish();
    }

    private void initView() {
        ImageButton backBtn = findViewById(R.id.btn_image_picker_title_back);
        titleTxt = findViewById(R.id.txt_image_picker_title_title);
        sendTxt = findViewById(R.id.btn_image_picker_title_detail);
        titleTxt.setText(R.string.image_picker_images);
        backBtn.setOnClickListener(this);
        sendTxt.setOnClickListener(this);

        RecyclerView imageRecycler = findViewById(R.id.recycler_view_photo_picker_content);
        adapter = new ImagePickerAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        imageRecycler.addItemDecoration(new PickerSpaceItemDecoration(this, R.dimen.image_picker_size_photo_divider));
        imageRecycler.setLayoutManager(layoutManager);
        imageRecycler.setHasFixedSize(true);
        imageRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        originalCheckBox = findViewById(R.id.checkbox_preview_select_source);
        previewTxt = findViewById(R.id.text_picker_select_choose);
        originalCheckBox.setOnClickListener(this);
        previewTxt.setOnClickListener(this);

        bottomLayout = findViewById(R.id.layout_picker_select_folder);
        TextView folderTxt = findViewById(R.id.text_picker_select_folder);
        folderTxt.setOnClickListener(this);
    }

    private void initData(Intent data) {
        int maxImage = data.getIntExtra(INTENT_NAME_SELECT_NUM, 9);
        boolean pickAvatar = data.getBooleanExtra(INTENT_NAME_PICK_AVATAR, false);

        DataHolder.getInstance().setMaxSize(maxImage);
        DataHolder.getInstance().setPickMode(pickAvatar);

        refreshView(pickAvatar);
    }

    private void refreshView(boolean pickAvatar) {
        if (pickAvatar) {
            originalCheckBox.setVisibility(View.GONE);
            previewTxt.setVisibility(View.GONE);
            sendTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPostExecute(@NonNull ArrayList<ImageFolder> imageFolders) {
        dismissLoadingDialog();

        mImageFolderModels = imageFolders;

        loadPhotos(0);
    }

    @Override
    public void onCancelled() {
        dismissLoadingDialog();
        cancelLoadPhotoTask();
    }

    private void loadPhotos(int position) {
        if (position < mImageFolderModels.size()) {
            List<String> images = mImageFolderModels.get(position).getImages();

            DataHolder.getInstance().setImageList(images);
            adapter.setListData(DataHolder.getInstance().getImageList());
        }
    }

    @Override
    public void onItemClick(int position) {
        DataHolder.getInstance().setCurrentPosition(position);

        if (DataHolder.getInstance().isPickAvatar()) {
            cropImage(position);
            return;
        }

        Intent intent = new Intent(this, PickerPreviewActivity.class);
        startActivity(intent);
    }

    /**
     * 剪裁图片界面
     */
    private void cropImage(int position) {
        String currentImage = DataHolder.getInstance().getCurrentImage(position);

        Uri source = Uri.fromFile(new File(currentImage));
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    @Override
    public void onItemChecked() {
        int selectListSize = DataHolder.getInstance().getSelectListSize();

        setNum(selectListSize);
    }

    private void setNum(int selectListSize) {
        sendTxt.setEnabled(selectListSize > 0);
        previewTxt.setEnabled(selectListSize > 0);

        if (selectListSize > 0) {
            sendTxt.setText(getString(R.string.image_picker_send_num, selectListSize, DataHolder.getInstance().getMaxSize()));
            previewTxt.setText(getString(R.string.image_picker_preview_num, selectListSize));
        } else {
            sendTxt.setText(getString(R.string.image_picker_send));
            previewTxt.setText(getString(R.string.image_picker_preview));
        }

    }

    private void setOriginChecked() {
        originalCheckBox.setChecked(DataHolder.getInstance().isOriginal());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_picker_select_choose) {
            Intent intent = new Intent(this, PickerPreviewActivity.class);
            intent.putExtra(EXTRA_PREVIEW_SELECT, true);
            startActivity(intent);
        } else if (id == R.id.btn_image_picker_title_back) {
            finish();
        } else if (id == R.id.btn_image_picker_title_detail) {
            DataHolder.getInstance().returnSelectImages(this);
            finish();
        } else if (id == R.id.text_picker_select_folder) {
            showFolderView();
        } else if (id == R.id.checkbox_preview_select_source) {
            DataHolder.getInstance().setOriginal(((PickerOriginalCheckbox) v).isChecked());
        }
    }

    private void showFolderView() {
        if (imageFolderView == null) {
            imageFolderView = new PickerFolderPopupWindow(this, bottomLayout, new PickerFolderAdapter.OnItemFolderClickListener() {
                @Override
                public void onFolderClick(int position) {
                    loadPhotos(position);
                    imageFolderView.dismiss();
                }
            });
            imageFolderView.setData(mImageFolderModels);
        }
        imageFolderView.show();
    }

    private void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new AppCompatDialog(this, R.style.PickerDialog);
            mLoadingDialog.setContentView(R.layout.image_picker_dialog_loading);
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    private void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    private void cancelLoadPhotoTask() {
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask.cancelTask();
            mLoadPhotoTask = null;
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TAKE_PHOTO)
    private void requestTakePhotoPermission() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            try {
//                startActivityForResult(mImageCaptureManager.getTakePictureIntent(), REQUEST_CODE_TAKE_PHOTO);
            } catch (Exception e) {
                PickerUtil.showToast(this, R.string.image_picker_photo_not_support);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, REQUEST_CODE_PERMISSION_TAKE_PHOTO);
            }
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_STORAGE_PHOTO)
    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            showLoadingDialog();
            mLoadPhotoTask = new LoadImageTask(getContentResolver(), this).perform();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, REQUEST_CODE_PERMISSION_STORAGE_PHOTO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        String text = "";

        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE_PHOTO) {
            text = "存储";
        } else if (requestCode == REQUEST_CODE_PERMISSION_TAKE_PHOTO) {
            text = "拍照";
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("您拒绝了 " + text + " 权限，如想正常使用，请在设置中打开")
                    .build()
                    .show();
        }
    }

}
