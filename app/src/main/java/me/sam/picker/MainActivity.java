package me.sam.picker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.wuba.image.photopicker.ImagePickerApi;
import com.wuba.image.photopicker.data.DataHolder;
import com.wuba.image.photopicker.view.crop.Crop;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_sample);
        adapter = new SampleAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public void pickImage(View view) {
        Intent intent = ImagePickerApi.pickerIntent(this);
        startActivityForResult(intent, 1);
    }

    public void pickAvatar(View view) {
        Intent intent = ImagePickerApi.pickerAvatarIntent(this);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    ArrayList<String> images = data.getStringArrayListExtra(DataHolder.EXTRA_SELECTED_IMAGES);
                    boolean isFull = data.getBooleanExtra(DataHolder.EXTRA_IS_FULL, false);
                    adapter.setDataAndNotify(images);
                    break;
                case 2:
                    final Uri uri = Crop.getOutput(data);
                    Log.e("uri", uri.toString());
                    break;
            }

        }
    }


}
