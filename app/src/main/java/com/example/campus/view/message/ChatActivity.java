package com.example.campus.view.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.helper.InputHandleUtil;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.netty.NettyConnectManager;
import com.example.campus.view.Constance;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private int imageIndex = 100000;
    //private boolean isSendModel;
    private EditText inputView;
    private ImageView otherImage;
    private Button sendBtn;
    private LinearLayout msgLayout;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_fragment);
        InputHandleUtil inputHandleUtil = new InputHandleUtil();
        inputHandleUtil.handleInputView(findViewById(android.R.id.content), null, null);
        initView();
    }

    private void initView() {
        TextView nameView = findViewById(R.id.chat_fragment_chat_name);
        nameView.setText(getIntent().getStringExtra(Constance.KEY_INTENT_CHAT_NAME));
        msgLayout = findViewById(R.id.chat_fragment_chat_message);
        inputView = findViewById(R.id.chat_input_edit);
        inputView.addTextChangedListener(watcher);
        otherImage = findViewById(R.id.chat_input_other);
        sendBtn = findViewById(R.id.btn_send_msg);
        sendBtn.setOnClickListener(v -> {
            if (inputView == null || TextUtils.isEmpty(inputView.getText().toString())) {
                return;
            }
            String text = inputView.getText().toString();
            Log.e(TAG,"send msg btn click");
            NettyConnectManager.getInstance().sendTextMsg("123","456",text);
            inputView.setText("");
            addChatContainViewSelf(text, msgLayout, v1 -> Log.e(TAG, "click"));
        });
        sendBtn.setVisibility(View.GONE);
        bundle = getIntent().getExtras();
        if (bundle.getBoolean(Constance.KEY_CHAT_IS_FROM_BOOK_DEAL, false)) {
            String avatarUrl = bundle.getString(Constance.KEY_BOOK_IMAGE_URL, "");
            float price = bundle.getFloat(Constance.KEY_BOOK_DEAL_SELL_PRICE, 0);
            String bookName = bundle.getString(Constance.KEY_BOOK_NAME, "");
            if (!TextUtils.isEmpty(avatarUrl) || !TextUtils.isEmpty(bookName)) {
                addBookMsgView(avatarUrl, String.valueOf(price), bookName);
            }
        }
        //连接服务器
        NettyConnectManager.getInstance().connect();
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams(
                        520, 110);
                lLayoutParams.setMargins(20, 0, 0, 0);
                lLayoutParams.gravity = Gravity.CENTER;
                inputView.setLayoutParams(lLayoutParams);
                sendBtn.setVisibility(View.VISIBLE);
                otherImage.setVisibility(View.GONE);
            } else if (s.length() == 0) {
                LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams(
                        580, 110);
                lLayoutParams.setMargins(20, 0, 0, 0);
                lLayoutParams.gravity = Gravity.CENTER;
                inputView.setLayoutParams(lLayoutParams);
                sendBtn.setVisibility(View.GONE);
                otherImage.setVisibility(View.VISIBLE);
            }
        }
    };

    private void addChatContainViewSelf(String msg, LinearLayout rootLayout, View.OnClickListener clickListener) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 5, 0, 5);

        // 2.创建头像控件
        ShapeableImageView imageView = new ShapeableImageView(this);
        RelativeLayout.LayoutParams imageParam = new RelativeLayout.LayoutParams(
                100, 100);
        imageParam.addRule(RelativeLayout.ALIGN_PARENT_END);
        imageView.setLayoutParams(imageParam);
        // 设置属性
        imageView.setImageResource(R.drawable.avatar);
        imageView.setId(imageIndex);
        layout.addView(imageView);

        // 3.创建内部TextText控件
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.addRule(RelativeLayout.START_OF, imageView.getId());
        textParam.setMargins(0, 0, 24, 0);
        textView.setLayoutParams(textParam);
        // 设置属性
        textView.setGravity(Gravity.START);
        textView.setTextSize(22);
        textView.setText(msg);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(40, 40, 40, 40);
        textView.setBackgroundResource(R.drawable.chat_message_shape);
        // 将TextView放到LinearLayout里
        layout.addView(textView);
        // 将ImageView放到LinearLayout里

        //添加点击监听事件
        layout.setOnClickListener(clickListener);
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        rootLayout.addView(layout);
        imageIndex++;
    }

    private void addChatContainViewPassive(String msg, LinearLayout rootLayout, View.OnClickListener clickListener) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 5, 0, 5);

        // 2.创建头像控件
        ShapeableImageView imageView = new ShapeableImageView(this);
        RelativeLayout.LayoutParams imageParam = new RelativeLayout.LayoutParams(
                100, 100);
        //imageParam.addRule(RelativeLayout.ALIGN_PARENT_END);
        imageView.setLayoutParams(imageParam);
        // 设置属性
        imageView.setImageResource(R.drawable.avatar);
        imageView.setId(imageIndex);
        layout.addView(imageView);

        // 3.创建内部TextText控件
        TextView textView = new TextView(this);
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
        textView.setBackgroundResource(R.drawable.chat_message_shape);
        // 将TextView放到LinearLayout里
        layout.addView(textView);
        // 将ImageView放到LinearLayout里

        //添加点击监听事件
        layout.setOnClickListener(clickListener);
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        rootLayout.addView(layout);
        imageIndex++;
    }

    private void addChatTimeView(String msg, LinearLayout rootLayout) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 5, 0, 5);

        // 3.创建内部TextText控件
        TextView textView = new TextView(this);
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

    private void addBookMsgView(String avatarUrl,String price,String bookName){
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(30, 0, 0, 50);
        layout.setLayoutParams(lLayoutParams);
        //第二层的线性布局
        LinearLayout linearLayout = new LinearLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(620,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundColor(Color.WHITE);
        layout.addView(linearLayout);
        //图片
        ShapeableImageView imageView = new ShapeableImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(600,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(imageParams);
        imageView.setAdjustViewBounds(true);
        linearLayout.addView(imageView);
        Glide.with(this)
                .load(RetrofitConfig.avatarHost + avatarUrl)
                .placeholder(R.drawable.image_unload)
                .into(imageView);
        //书名
        TextView textViewName = new TextView(this);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        nameParams.setMargins(0, 20, 0, 0);
        nameParams.gravity = Gravity.CENTER_HORIZONTAL;
        textViewName.setLayoutParams(nameParams);
        textViewName.setText(bookName);
        textViewName.setTextColor(Color.BLACK);
        textViewName.setTextSize(18);
        linearLayout.addView(textViewName);
        //价格
        TextView textViewPrice = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        priceParams.setMargins(0, 20, 0, 0);
        priceParams.gravity = Gravity.CENTER_HORIZONTAL;
        textViewPrice.setLayoutParams(priceParams);
        textViewPrice.setText("￥" + price);
        textViewPrice.setTextColor(Color.RED);
        textViewPrice.setTextSize(18);
        linearLayout.addView(textViewPrice);

        msgLayout.addView(layout);
    }
}
