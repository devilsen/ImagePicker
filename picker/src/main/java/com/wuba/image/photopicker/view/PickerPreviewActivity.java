package com.wuba.image.photopicker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuba.image.photopicker.R;
import com.wuba.image.photopicker.data.DataHolder;
import com.wuba.image.photopicker.observer.PickerObserverHolder;
import com.wuba.image.photopicker.util.PickerUtil;
import com.wuba.image.photopicker.view.adapter.ImagePagerAdapter;
import com.wuba.image.photopicker.view.adapter.ImagePreviewAdapter;
import com.wuba.image.photopicker.widget.PickerViewPager;
import com.wuba.image.photopicker.widget.checkbox.PickerOriginalCheckbox;
import com.wuba.mobile.photoview.OnViewTapListener;

import java.util.ArrayList;

import static com.wuba.image.photopicker.view.PickerConstant.EXTRA_PREVIEW_SELECT;

/**
 * desc :
 * date : 2018/8/15
 *
 * @author : dongSen
 */
public class PickerPreviewActivity extends PickerBaseActivity implements
        CompoundButton.OnCheckedChangeListener, ViewPager.OnPageChangeListener,
        ImagePreviewAdapter.OnItemClickListener, View.OnClickListener, OnViewTapListener {

    private ImagePreviewAdapter previewAdapter;
    private PickerViewPager viewPager;
    private CheckBox chooseCheckBox;

    private DataHolder dataHolder = DataHolder.getInstance();

    private ArrayList<String> imageList;
    private int currentPosition;
    private RecyclerView selectRecycler;
    private TextView titleTxt;
    private TextView sendTxt;

    private LinearLayout toolbarLayout;
    private LinearLayout bottomBarLayout;
    //预览模式
    private boolean previewMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_activity_picker_preview);
        initData();
        initView();
    }

    private void initData() {
        Intent data = getIntent();
        boolean previewSelect = data.getBooleanExtra(EXTRA_PREVIEW_SELECT, false);

        if (previewSelect) {//预览已经选择的图片
            imageList = new ArrayList<>(dataHolder.getSelectList());
            currentPosition = 0;
            previewMode = true;
        } else {//预览所有图片
            imageList = dataHolder.getImageList();
            currentPosition = dataHolder.getCurrentPosition();
        }
    }

    private void initView() {
        toolbarLayout = findViewById(R.id.image_picker_view_toolbar_layout);
        bottomBarLayout = findViewById(R.id.image_picker_view_bottom_layout);

        ImageButton backBtn = findViewById(R.id.btn_image_picker_title_back);
        titleTxt = findViewById(R.id.txt_image_picker_title_title);
        sendTxt = findViewById(R.id.btn_image_picker_title_detail);
        titleTxt.setText(R.string.image_picker_all_image);
        backBtn.setOnClickListener(this);
        sendTxt.setOnClickListener(this);
        setSendNum(dataHolder.getSelectListSize());

        viewPager = findViewById(R.id.view_pager_photo_picker_content);

        ImagePagerAdapter adapter = new ImagePagerAdapter(imageList, this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnClickListener(this);
        setTitleTxt(currentPosition);

        selectRecycler = findViewById(R.id.recycler_view_preview_select);

        previewAdapter = new ImagePreviewAdapter(this, dataHolder.getSelectList());
        selectRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectRecycler.setHasFixedSize(true);
        selectRecycler.setAdapter(previewAdapter);
        previewAdapter.setOnItemClickListener(this);
        setSelectVisibility();
        previewAdapter.setSelectPosition(findSelectPositionByPath(imageList.get(currentPosition)));

        chooseCheckBox = findViewById(R.id.checkbox_preview_select_choose);
        chooseCheckBox.setOnCheckedChangeListener(this);
        setChooseChecked(viewPager.getCurrentItem());

        PickerOriginalCheckbox originalCheckBox = findViewById(R.id.checkbox_preview_select_source);
        originalCheckBox.setOnClickListener(this);
        originalCheckBox.setChecked(dataHolder.isOriginal());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.checkbox_preview_select_choose) {
            String imagePath = imageList.get(viewPager.getCurrentItem());
            int selectListSize = dataHolder.getSelectListSize();
            if (isChecked) {
                selectListSize++;
                setSendButtonState(selectListSize);

                if (selectListSize > DataHolder.getInstance().getMaxSize()) {
                    buttonView.setChecked(false);
                    PickerUtil.showToast(this, getString(R.string.image_picker_over_max_size, DataHolder.getInstance().getMaxSize()));
                    return;
                }

                dataHolder.addSelectImage(imagePath);
                previewAdapter.notifyItemInserted(selectListSize);
                selectRecycler.smoothScrollToPosition(selectListSize);

                previewAdapter.setSelectPosition(findSelectPositionByPath(imageList.get(viewPager.getCurrentItem())));
            } else {
                selectListSize--;
                setSendButtonState(selectListSize);

                dataHolder.removeSelectImage(imagePath);
                previewAdapter.notifyDataSetChanged();
            }
            setSelectVisibility();
            setSendNum(selectListSize);
        }
    }

    private void setSendNum(int selectListSize) {
        if (selectListSize > 0) {
            sendTxt.setText(getString(R.string.image_picker_send_num, selectListSize, dataHolder.getMaxSize()));
        } else {
            sendTxt.setText(getString(R.string.image_picker_send));
        }
    }

    private void setTitleTxt(int position) {
        position++;
        titleTxt.setText(getString(R.string.image_picker_select_total, position, imageList.size()));
    }

    private void setSelectVisibility() {
        if (dataHolder.getSelectListSize() > 0) {
            selectRecycler.setVisibility(View.VISIBLE);
        } else {
            selectRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleTxt(position);

        setChooseChecked(position);
        previewAdapter.setSelectPosition(findSelectPositionByPath(imageList.get(position)));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_image_picker_title_detail) {
            sendImage();
        } else if (id == R.id.btn_image_picker_title_back) {
            finish();
        } else if (id == R.id.checkbox_preview_select_source) {
            DataHolder.getInstance().setOriginal(((PickerOriginalCheckbox) v).isChecked());
        }
    }

    private void sendImage() {
        if (dataHolder.getSelectListSize() == 0) {
            String imagePath = imageList.get(viewPager.getCurrentItem());
            dataHolder.addSelectImage(imagePath);
            previewAdapter.notifyItemInserted(dataHolder.getSelectListSize() + 1);
        }
        finish();
        PickerObserverHolder.getInstance().notifyFinish();
    }

    private void setSendButtonState(int selectListSize) {
        if (!previewMode)
            return;

        sendTxt.setEnabled(selectListSize > 0);
    }

    @Override
    public void onItemClick(String imagePath) {
        int position = findPositionByPath(imagePath);

        if (position != -1)
            viewPager.setCurrentItem(position);
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        changeDecorStatus();
    }

    private boolean isVisible = true;

    private void changeDecorStatus() {
        if (isVisible) {
            toolbarLayout.animate().translationY(-toolbarLayout.getHeight()).setDuration(200).start();
            bottomBarLayout.animate().translationY(bottomBarLayout.getHeight()).setDuration(200).start();
            isVisible = false;


        } else {
            toolbarLayout.animate().translationYBy(toolbarLayout.getHeight()).setDuration(200).start();
            bottomBarLayout.animate().translationYBy(-bottomBarLayout.getHeight()).setDuration(200).start();
            isVisible = true;
        }
    }

    private void setChooseChecked(int position) {
        chooseCheckBox.setOnCheckedChangeListener(null);
        chooseCheckBox.setChecked(dataHolder.imageIsSelected(imageList.get(position)));
        chooseCheckBox.setOnCheckedChangeListener(this);
    }

    /**
     * 通过图片路径找到位置
     *
     * @param imagePath 图片路径
     */
    public int findPositionByPath(String imagePath) {
        if (TextUtils.isEmpty(imagePath))
            return -1;

        for (int i = 0; i < imageList.size(); i++) {
            if (imagePath.equals(imageList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private int findSelectPositionByPath(String imagePath) {
        if (TextUtils.isEmpty(imagePath))
            return -1;

        ArrayList<String> selectList = dataHolder.getSelectList();
        for (int i = 0; i < selectList.size(); i++) {
            if (imagePath.equals(selectList.get(i))) {
                return i;
            }
        }
        return -1;
    }

}
