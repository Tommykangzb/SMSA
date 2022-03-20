package com.example.campus.view.navigate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus.R;
import com.example.campus.helper.ScreenHelp;

import java.util.Objects;

public class UserCenterFragment extends Fragment {

    private static final String TAG = "UserCenterFragment";

    private LinearLayout settingLinearLayout;
    private LinearLayout accountLinearLayout;
    private TextView textViewName;
    private TextView textViewUniversity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_center_fragment, null);
        //设置部分的容器
        settingLinearLayout = view.findViewById(R.id.user_center_layout_setting);
        //账户部分的容器
        accountLinearLayout = view.findViewById(R.id.user_center_layout_account);
        textViewName = view.findViewById(R.id.userName);
        textViewUniversity = view.findViewById(R.id.userUniversityAndGrade);
        initView();
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueBlue);
        ScreenHelp.setAndroidNativeLightStatusBar(getActivity(), true);
        return view;
    }

    private void initView() {
        addContent("修改密码", accountLinearLayout, getActivity(), (v -> Log.e(TAG, "click")));
        addContent("绑定邮箱", accountLinearLayout, getActivity(), (v -> Log.e(TAG, "click")));
        addContent("个人信息", settingLinearLayout, getActivity(), (v -> Log.e(TAG, "click")));
        updateName("康智波");
        updateUniversity("华南理工大学", "2018级");
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void addContent(String setting, LinearLayout llContentView, Context context, View.OnClickListener clickListener) {
        // 开始添加控件
        // 1.创建外围LinearLayout控件
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置margin
        lLayoutParams.setMargins(0, 10, 0, 10);
        layout.setLayoutParams(lLayoutParams);
        // 设置属性
        layout.setPadding(0, 30, 0, 20);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackground(context.getDrawable(R.drawable.user_center_item));

        // 2.创建内部TextText控件
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.weight = 5;
        textView.setLayoutParams(textParam);
        // 设置属性
        textView.setGravity(Gravity.START);
        textView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        textView.setPadding(20, 0, 0, 0);
        textView.setTextSize(18);
        textView.setText(setting);
        textView.setTextColor(Color.BLACK);
        // 将TextView放到LinearLayout里
        layout.addView(textView);

        // 3.创建进入控件
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParam.weight = 1;
        imageView.setLayoutParams(imageParam);
        // 设置属性
        imageView.setImageResource(R.drawable.ic_icon_enter);
        imageView.setPadding(0, 0, 10, 0);
        // 将ImageView放到LinearLayout里
        layout.addView(imageView);

        //添加点击监听事件
        layout.setOnClickListener(clickListener);
        // 4.将layout同它内部的所有控件加到最外围的llContentView容器里
        llContentView.addView(layout);
        addDividingLine(llContentView, context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addDividingLine(LinearLayout linearLayout, Context context) {
        //创建分割线
        View line = new View(context);
        LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        line.setLayoutParams(lineParam);
        line.setBackground(context.getDrawable(R.color.gray_user_center));
        linearLayout.addView(line);
    }

    public void updateName(@NonNull String name) {
        if (name.length() > 20) {
            Toast.makeText(getActivity(), R.string.user_name_over_length, Toast.LENGTH_SHORT).show();
            return;
        }
        textViewName.setText(name);
    }

    @SuppressLint("SetTextI18n")
    public void updateUniversity(@NonNull String university, @NonNull String grades) {
        textViewUniversity.setText(university + " | " + grades);
    }

}
