package com.example.campus.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.campus.protoModel.Login;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;
import com.example.campus.view.profile.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;

public class PswVerifyHelper {

    public static void verifyPsw(String account, String psw, Callback<Login.LoginResponse> callback) {
        if (TextUtils.isEmpty(account)){
            return;
        }
        psw = LoginActivity.sha256Encrypt(psw);
        Login.LoginRequest.Builder builder = Login.LoginRequest.newBuilder();
        builder.setAccount(account)
                .setPassword(psw)
                .setTimeStamp(System.currentTimeMillis());
        ApiService loginService = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<Login.LoginResponse> call = loginService.loginRequest(builder.build());
        call.enqueue(callback);
    }

    public static void clearUserSpf(Activity activity){
        SharedPreferences.Editor editor = activity.getSharedPreferences("data", 0).edit();
        editor.putString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "");
        editor.putString(Constance.KEY_USER_CENTER_USER_NAME, "");
        editor.putString(Constance.KEY_USER_CENTER_USER_GRADES, "");
        editor.putString(Constance.KEY_USER_CENTER_USER_AVATAR_URL, "");
        editor.putString(Constance.KEY_USER_CENTER_USER_UNIVERSITY, "");
        editor.apply();
    }
}
