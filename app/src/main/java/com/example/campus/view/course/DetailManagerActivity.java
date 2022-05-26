package com.example.campus.view.course;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;

import com.example.campus.DataGenerator;
import com.example.campus.R;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.netty.NettyConnectManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class DetailManagerActivity extends AppCompatActivity {
    private boolean sort = false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //intent = getIntent();
        ScreenHelp.enableTranslucentStatusBar(this);
        setContentView(R.layout.layout_course_details);

        initView();
        //loadViewData();
    }
    @SuppressLint("RestrictedApi")
    private void initView(){
        ImageView imageView = findViewById(R.id.course_detail_btn_review_mng);
        imageView.setOnClickListener(v ->{
            PopupMenu popupMenu = new PopupMenu(DetailManagerActivity.this,imageView);
            popupMenu.getMenuInflater().inflate(R.menu.menu_option,popupMenu.getMenu());
            popupMenu.show();
        });
        TextView textView = findViewById(R.id.tx_change_sort);
        textView.setText(DataGenerator.mReviewSortSetting[0]);
        findViewById(R.id.content_change_sort).setOnClickListener(v -> {
            textView.setText(sort ? DataGenerator.mReviewSortSetting[0] : DataGenerator.mReviewSortSetting[1]);
            sort = !sort;
        });
    }
}
