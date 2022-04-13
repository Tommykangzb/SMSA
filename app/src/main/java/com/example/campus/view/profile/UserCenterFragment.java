package com.example.campus.view.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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
import com.example.campus.view.Constance;
import com.example.campus.view.course.CommentDialog;

import java.util.Objects;

public class UserCenterFragment extends Fragment {

    private static final String TAG = "UserCenterFragment";

    private LinearLayout settingLinearLayout;
    private LinearLayout accountLinearLayout;
    private TextView textViewName;
    private TextView textViewUniversity;
    private TextView loginText;
    private CommentDialog commentDialog;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        bundle = new Bundle();
        bundle.putString("123","hello");
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
        loginText = view.findViewById(R.id.login);
        initView();
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueBlue);
        ScreenHelp.setAndroidNativeLightStatusBar(getActivity(), true);
        return view;
    }

    private void initView() {
        addContent("修改密码", accountLinearLayout, getActivity(), clickListener);
        addContent("绑定邮箱", accountLinearLayout, getActivity(), (v -> Log.e(TAG, "click")));
        addContent("个人信息", settingLinearLayout, getActivity(), clickListenerLogin);
        addContent("修改IP", settingLinearLayout, getActivity(), (v -> Log.e(TAG, "click")));
        SharedPreferences spf = Objects.requireNonNull(getActivity()).getSharedPreferences("data",Context.MODE_PRIVATE);
        String account = spf.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT,"");
        if (TextUtils.isEmpty(account)) {
            notLogin();
        } else {
            initViewLogin(spf);
        }
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

    private void notLogin() {
        textViewName.setVisibility(View.GONE);
        textViewUniversity.setVisibility(View.GONE);
        loginText.setVisibility(View.VISIBLE);
        loginText.setText("未登录");
        loginText.setOnClickListener(clickListenerLogin);
    }

    @SuppressLint("SetTextI18n")
    private void initViewLogin(SharedPreferences spf) {
        String name = spf.getString(Constance.KEY_USER_CENTER_USER_NAME, "");
        String university = spf.getString(Constance.KEY_USER_CENTER_USER_UNIVERSITY, "");
        String grades = spf.getString(Constance.KEY_USER_CENTER_USER_GRADES, "");
        textViewName.setVisibility(View.VISIBLE);
        textViewUniversity.setVisibility(View.VISIBLE);
        textViewName.setText(name);
        textViewUniversity.setText(university + " | " + grades);
        loginText.setVisibility(View.GONE);
    }

    private final View.OnClickListener clickListener = v -> {
        commentDialog = new CommentDialog(bundle);
        commentDialog.showDialog(getActivity());
    };

    private final View.OnClickListener clickListenerLogin = v -> {
        Intent intent = new Intent(v.getContext(),LoginActivity.class);
        v.getContext().startActivity(intent);
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (commentDialog != null) {
            commentDialog.hideDialog();
        }
    }
}