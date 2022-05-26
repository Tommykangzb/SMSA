package com.example.campus.view.profile;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.campus.R;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.view.BaseFragmentActivity;

import java.util.Objects;

public class UserMsgHomeFragment extends Fragment {
    BaseFragmentActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_modify_user_message_home_fragment, null);
        activity = (BaseFragmentActivity) getActivity();
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueBlue);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.user_message_home_back).setOnClickListener(clickListenerBtnBack);
        LinearLayout layout = view.findViewById(R.id.user_message_modify_content);
        addContent("头像", null, layout, v -> activity.changeFragment(new UserMsgAvatarFragment()));
        addContent("昵称", "kzb", layout, null);
        addDividingLine(layout, 20);
        addContent("签名", "明天会更好", layout, null);
    }

    private void addContent(String setting, @Nullable String str, LinearLayout llContentView, @Nullable View.OnClickListener clickListener) {
        // 1.创建外围LinearLayout控件
        LinearLayout layout = new LinearLayout(activity);
        LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(0, 10, 0, 10);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 30, 0, 20);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        //添加点击监听事件
        if (clickListener != null) {
            addTextContent(layout, setting, 5);
            layout.setOnClickListener(clickListener);
            addImageContent(layout);
        } else {
            addTextContent(layout, setting, 0);
            addEditContent(layout, str == null ? "" : str);
        }
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        llContentView.addView(layout);
        addDividingLine(llContentView, 2);
    }

    private void addEditContent(LinearLayout layout, String str) {
        EditText editText = new EditText(activity);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.weight = 0;
        textParam.setMargins(140, 0, 0, 0);
        editText.setLayoutParams(textParam);
        // 设置属性
        editText.setGravity(Gravity.START);
        editText.setPadding(20, 0, 0, 0);
        editText.setTextSize(17);
        editText.setText(str);
        editText.setBackground(activity.getDrawable(R.drawable.input_no_frame));
        // 将TextView放到LinearLayout里
        layout.addView(editText);
    }

    private void addTextContent(LinearLayout layout, String str, int weight) {
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.weight = weight;
        textParam.setMargins(20, 0, 0, 0);
        textView.setLayoutParams(textParam);
        // 设置属性
        textView.setGravity(Gravity.START);
        textView.setPadding(20, 0, 0, 0);
        textView.setTextSize(17);
        textView.setText(str);
        textView.setTextColor(Color.BLACK);
        // 将TextView放到LinearLayout里
        layout.addView(textView);
    }

    private void addImageContent(LinearLayout layout) {
        // 3.创建进入控件
        ImageView imageView = new ImageView(activity);
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParam.weight = 0;
        imageView.setLayoutParams(imageParam);
        // 设置属性
        imageView.setImageResource(R.drawable.ic_icon_enter);
        imageView.setPadding(0, 0, 20, 0);
        // 将ImageView放到LinearLayout里
        layout.addView(imageView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addDividingLine(LinearLayout linearLayout, int deep) {
        //创建分割线
        View line = new View(activity);
        LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, deep);
        line.setLayoutParams(lineParam);
        line.setBackground(activity.getDrawable(R.color.gray_user_center));
        linearLayout.addView(line);
    }

    private final View.OnClickListener clickListenerBtnBack = v ->
            ((AppCompatActivity) v.getContext()).getSupportFragmentManager().popBackStack();
}
