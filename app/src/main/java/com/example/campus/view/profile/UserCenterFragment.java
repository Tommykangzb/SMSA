package com.example.campus.view.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.helper.PswVerifyHelper;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.view.ActivityViewTest;
import com.example.campus.view.BaseDialog;
import com.example.campus.view.Constance;
import com.example.campus.view.course.DetailManagerActivity;
import com.example.campus.view.course.SaveDialog;
import com.example.campus.view.message.FriendsManager;
import com.google.android.material.imageview.ShapeableImageView;


public class UserCenterFragment extends Fragment {

    private static final String url = RetrofitConfig.avatarHost;
    private static final String TAG = "UserCenterFragment";

    private LinearLayout settingLinearLayout;
    private LinearLayout accountLinearLayout;
    private TextView textViewName;
    private TextView textViewUniversity;
    private TextView loginText;
    private BaseDialog commentDialog;
    private Bundle bundle;
    private ShapeableImageView avatar;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        bundle = new Bundle();
        bundle.putString("123","hello");
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView ");
        View view = inflater.inflate(R.layout.layout_user_center_fragment, null);
        //设置部分的容器
        settingLinearLayout = view.findViewById(R.id.user_center_layout_setting);
        //账户部分的容器
        accountLinearLayout = view.findViewById(R.id.user_center_layout_account);
        textViewName = view.findViewById(R.id.userName);
        textViewUniversity = view.findViewById(R.id.userUniversityAndGrade);
        loginText = view.findViewById(R.id.login);
        avatar = view.findViewById(R.id.userCenterAvatar);
        initView();
        ScreenHelp.setStatusBarColor(activity, ScreenHelp.stateBarColorValueBlue);
        ScreenHelp.setAndroidNativeLightStatusBar(activity, true);
        return view;
    }

    private void initView() {
        addContent("修改密码", accountLinearLayout, activity, clickListenerEditPsw);
        //addContent("绑定邮箱", accountLinearLayout, activity, clickListener);
        addContent("退出登录", accountLinearLayout, activity, clickListenerSignOut);
        addContent("注销账号", accountLinearLayout, activity, clickListenerLogout);
        addContent("个人信息", settingLinearLayout, activity, clickListenerLoginEditMsg);
        addContent("修改IP", settingLinearLayout, activity,
                (v -> startActivity(new Intent(activity, DetailManagerActivity.class))));
        addContent("测试View", settingLinearLayout, activity,
                (v -> startActivity(new Intent(activity, ActivityViewTest.class))));
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoginMsg();
        Log.e(TAG, "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (commentDialog != null) {
            commentDialog.hideDialog();
        }
    }

    private void setLoginMsg(){
        if (isNotLogin()) {
            notLogin();
            Log.e(TAG, "not login");
        } else {
            initViewLogin(activity.getSharedPreferences("data", Context.MODE_PRIVATE));
            Log.e(TAG, "login");
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
        avatar.setImageResource(R.drawable.image_unload);
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
        String avatarUrl = url + spf.getString(Constance.KEY_USER_CENTER_USER_AVATAR_URL, "");
        Glide.with(activity)
                .load(avatarUrl)
                .placeholder(R.drawable.image_unload)
                .into(avatar);
    }

    private boolean isNotLogin(){
        SharedPreferences spf = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
        String account = spf.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "");
        return TextUtils.isEmpty(account);
    }

    private final View.OnClickListener clickListenerEditPsw = v -> {
        if (isNotLogin()) {
            Toast.makeText(activity,R.string.user_center_not_login_text,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(v.getContext(),PasswordEditActivity.class);
        v.getContext().startActivity(intent);
    };

    private final View.OnClickListener clickListenerSignOut = v -> {
        if (isNotLogin()) {
            Toast.makeText(activity,R.string.user_center_not_login_text,Toast.LENGTH_SHORT).show();
            return;
        }
        commentDialog = new SaveDialog(bundle,null,"是否退出登录");
        ((SaveDialog)commentDialog).setSaveCallBack(new SaveDialog.SaveCallBack() {
            @Override
            public void choseTrue() {
                PswVerifyHelper.clearUserSpf(activity);
                setLoginMsg();
            }

            @Override
            public void choseFalse() {

            }
        });
        commentDialog.showDialog(getActivity());
    };

    private final View.OnClickListener clickListenerLogin = v -> {
        Intent intent = new Intent(v.getContext(),LoginActivity.class);
        v.getContext().startActivity(intent);
    };

    private final View.OnClickListener clickListenerLoginEditMsg = v -> {
        if (isNotLogin()) {
            Toast.makeText(activity,R.string.user_center_not_login_text,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(v.getContext(), UserMessageModifyActivity.class);
        v.getContext().startActivity(intent);
    };

    private final View.OnClickListener clickListenerLogout = v -> {
        if (isNotLogin()) {
            Toast.makeText(activity,R.string.user_center_not_login_text,Toast.LENGTH_SHORT).show();
            return;
        }
        commentDialog = new SaveDialog(bundle,null,"是否注销账号");
        ((SaveDialog)commentDialog).setSaveCallBack(new SaveDialog.SaveCallBack() {
            @Override
            public void choseTrue() {
                Intent intent = new Intent(v.getContext(), LogoutActivity.class);
                startActivity(intent);
            }

            @Override
            public void choseFalse() {

            }
        });
        commentDialog.showDialog(getActivity());
    };


}
