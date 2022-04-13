package com.example.campus.view.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus.R;
import com.example.campus.helper.InputHandleUtil;
import com.example.campus.view.Constance;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private int imageIndex = 100000;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_fragment);
        InputHandleUtil inputHandleUtil = new InputHandleUtil();
        inputHandleUtil.handleInputView(findViewById(android.R.id.content),null,null);
        initView();
    }

    private void initView() {
        TextView nameView = findViewById(R.id.chat_fragment_chat_name);
        nameView.setText(getIntent().getStringExtra(Constance.KEY_INTENT_CHAT_NAME));
        LinearLayout layout = findViewById(R.id.chat_fragment_chat_message);
        for (int i = 0; i < 20; i++) {
            addChatContainViewSelf("这周一起去吃饭吗？", layout, this, v -> Log.e("kkk", "qwe"));
            addChatContainViewPassive("这周一起去吃饭吗？", layout, this, v -> Log.e("kkk", "qwe"));
            if (i % 5 == 0) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M月d号 E H:mm");
                Date date = new Date(System.currentTimeMillis());
                addChatTimeView(simpleDateFormat.format(date), layout, this);
            }
        }
    }

    private void addChatContainViewSelf(String msg, LinearLayout rootLayout, Context context, View.OnClickListener clickListener) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 5, 0, 5);

        // 2.创建头像控件
        ShapeableImageView imageView = new ShapeableImageView(context);
        RelativeLayout.LayoutParams imageParam = new RelativeLayout.LayoutParams(
                100, 100);
        imageParam.addRule(RelativeLayout.ALIGN_PARENT_END);
        imageView.setLayoutParams(imageParam);
        // 设置属性
        imageView.setImageResource(R.drawable.avatar);
        imageView.setId(imageIndex);
        layout.addView(imageView);

        // 3.创建内部TextText控件
        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
                textParam.addRule(RelativeLayout.START_OF,imageView.getId());
        textParam.setMargins(0,0,24,0);
        textView.setLayoutParams(textParam);
        // 设置属性
        textView.setGravity(Gravity.START);
        textView.setTextSize(22);
        textView.setText(msg);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(40,40,40,40);
        textView.setBackground(getDrawable(R.drawable.chat_message_shape));
        // 将TextView放到LinearLayout里
        layout.addView(textView);
        // 将ImageView放到LinearLayout里


        //添加点击监听事件
        layout.setOnClickListener(clickListener);
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        rootLayout.addView(layout);
        imageIndex++;
    }

    private void addChatContainViewPassive(String msg, LinearLayout rootLayout, Context context, View.OnClickListener clickListener) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 5, 0, 5);

        // 2.创建头像控件
        ShapeableImageView imageView = new ShapeableImageView(context);
        RelativeLayout.LayoutParams imageParam = new RelativeLayout.LayoutParams(
                100, 100);
        //imageParam.addRule(RelativeLayout.ALIGN_PARENT_END);
        imageView.setLayoutParams(imageParam);
        // 设置属性
        imageView.setImageResource(R.drawable.avatar);
        imageView.setId(imageIndex);
        layout.addView(imageView);

        // 3.创建内部TextText控件
        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.END_OF,imageView.getId());
        textParam.setMargins(24,0,0,0);
        textView.setLayoutParams(textParam);
        // 设置属性
        textView.setGravity(Gravity.START);
        textView.setTextSize(22);
        textView.setText(msg);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(40,40,40,40);
        textView.setBackground(getDrawable(R.drawable.chat_message_shape));
        // 将TextView放到LinearLayout里
        layout.addView(textView);
        // 将ImageView放到LinearLayout里


        //添加点击监听事件
        layout.setOnClickListener(clickListener);
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        rootLayout.addView(layout);
        imageIndex++;
    }

    private void addChatTimeView(String msg, LinearLayout rootLayout, Context context) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 5, 0, 5);

        // 3.创建内部TextText控件
        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(textParam);
        // 设置属性
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setText(msg);
        // 将TextView放到LinearLayout里
        layout.addView(textView);
        // 将ImageView放到LinearLayout里
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        rootLayout.addView(layout);
    }

}
