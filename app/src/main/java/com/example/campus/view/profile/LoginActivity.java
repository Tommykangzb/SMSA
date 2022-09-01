package com.example.campus.view.profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus.R;
import com.example.campus.helper.InputHandleUtil;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.protoModel.Login;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final char[] hex = "0123456789abcdef".toCharArray();
    private EditText accountEdit;
    private EditText nameEdit;
    private EditText passwordEdit;
    private Button btnLogin;
    private TextView accountMention;
    private TextView passwordMention;
    private TextView emailMention;
    private ApiService loginService;
    // loginState is true when need to create an account.
    // if have an account, loginState will be false
    private boolean loginState = false;
    private boolean passwordHideOrShow = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenHelp.enableTranslucentStatusBar(this);
        setContentView(R.layout.layout_login_or_sign);
        loginService = RetrofitConfig.getInstance().getService(ApiService.class);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_login_back).setOnClickListener(clickListenerBtnBack);
        accountEdit = findViewById(R.id.login_input_create_account);
        accountEdit.setOnFocusChangeListener(accountListener);
        nameEdit = findViewById(R.id.login_input_input_email);
        passwordEdit = findViewById(R.id.login_input_create_password);
        passwordEdit.setOnFocusChangeListener(passwordListener);
        accountMention = findViewById(R.id.account_mention);
        passwordMention = findViewById(R.id.password_mention);
        btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(clickListenerBtnLogin);
        emailMention = findViewById(R.id.email_mention);
        findViewById(R.id.exchange_text_login_or_sign).setOnClickListener(clickListenerExchangeState);
        findViewById(R.id.login_text_create_account).setVisibility(View.GONE);
        findViewById(R.id.login_input_email_layout).setVisibility(View.GONE);
        findViewById(R.id.password_display_icon).setOnClickListener(clickListenerHideOrShow);
    }

    private final View.OnClickListener clickListenerBtnBack = v -> finish();

    private final View.OnFocusChangeListener accountListener = (v, hasFocus) -> {
        if (!loginState) {
            return;
        }
        if (!hasFocus) {
            String account = ((EditText) v).getText().toString();
            if (!InputHandleUtil.Companion.checkAccount(account)) {
                setMentionText(accountMention, R.string.user_center_account_mention);
            }
        }
    };

    private final View.OnFocusChangeListener passwordListener = (v, hasFocus) -> {
        if (!loginState) {
            return;
        }
        if (!hasFocus) {
            String password = ((EditText) v).getText().toString();
            if (!InputHandleUtil.Companion.checkPassword(password)) {
                setMentionText(passwordMention, R.string.user_center_password_mention);
            }
        }
    };

    private final Callback<Login.LoginResponse> callbackLogin = new Callback<Login.LoginResponse>() {
        @Override
        public void onResponse(@NonNull Call<Login.LoginResponse> call, Response<Login.LoginResponse> response) {
            if (response.body() == null) {
                toastInMain(R.string.request_error, Toast.LENGTH_SHORT);
                Log.e(TAG, "response is null");
                return;
            }
            Login.LoginResponse responseBody = response.body();
            switch (responseBody.getLoginResult()){
                case -3:
                    toastInMain(R.string.request_error, Toast.LENGTH_SHORT);
                    break;
                case -2:
                    toastInMain(R.string.login_request_account_exist, Toast.LENGTH_SHORT);
                    break;
                case -1:
                    toastInMain(R.string.login_request_no_such_account, Toast.LENGTH_SHORT);
                    break;
                case 0:
                    toastInMain(R.string.login_request_password_error, Toast.LENGTH_SHORT);
                    break;
                case 1:
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString(Constance.KEY_USER_CENTER_USER_ACCOUNT,accountEdit.getText().toString());
                    editor.putString(Constance.KEY_USER_CENTER_USER_NAME, responseBody.getUserName());
                    editor.putString(Constance.KEY_USER_CENTER_USER_GRADES, responseBody.getUserGrade());
                    editor.putString(Constance.KEY_USER_CENTER_USER_AVATAR_URL, responseBody.getUserImageUrl());
                    editor.putString(Constance.KEY_USER_CENTER_USER_UNIVERSITY, responseBody.getUserSchool());
                    editor.putString(Constance.KEY_USER_CENTER_USER_UID, responseBody.getUserId());
                    editor.apply();
                    finish();
                    break;
                case 2:
                    SharedPreferences.Editor editorSignUp = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editorSignUp.putString(Constance.KEY_USER_CENTER_USER_ACCOUNT,accountEdit.getText().toString());
                    editorSignUp.putString(Constance.KEY_USER_CENTER_USER_NAME, nameEdit.getText().toString());
                    editorSignUp.putString(Constance.KEY_USER_CENTER_USER_UID, responseBody.getUserId());
                    editorSignUp.apply();
                    finish();
                    break;
                case 3:
                    //toastInMain(R.string.login_request_delete_account_succeed, Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onFailure(@NonNull Call<Login.LoginResponse> call, @NonNull Throwable t) {
            toastInMain(R.string.request_error, Toast.LENGTH_SHORT);
        }
    };

    private final View.OnClickListener clickListenerBtnLogin = v -> {
        Log.e(TAG, "loginState: " + loginState);
        if (!loginState) {
            //登录态
            String account = accountEdit.getText().toString();
            if (TextUtils.isEmpty(account)) {
                setMentionText(accountMention, R.string.user_center_account_mention_null);
                return;
            }
            String password = passwordEdit.getText().toString();
            if (TextUtils.isEmpty(password)) {
                setMentionText(passwordMention, R.string.user_center_password_mention_null);
                return;
            }
            password = sha256Encrypt(password);
            Login.LoginRequest.Builder builder = Login.LoginRequest.newBuilder();
            builder.setAccount(account)
                    .setPassword(password)
                    .setTimeStamp(System.currentTimeMillis());
            Call<Login.LoginResponse> call = loginService.loginRequest(builder.build());
            call.enqueue(callbackLogin);
            return;
        }
        String account = accountEdit.getText().toString();
        if (!InputHandleUtil.Companion.checkAccount(account)) {
            setMentionText(accountMention, R.string.user_center_account_mention);
            return;
        }
        String password = passwordEdit.getText().toString();
        if (!InputHandleUtil.Companion.checkPassword(password)) {
            setMentionText(passwordMention, R.string.user_center_password_mention);
            return;
        }
        String email = nameEdit.getText().toString();
        if (!InputHandleUtil.Companion.checkEmail(email)) {
            setMentionText(emailMention, R.string.user_center_user_name_mention);
            return;
        }
        password = sha256Encrypt(password);
        Login.LoginRequest.Builder builder = Login.LoginRequest.newBuilder();
        builder.setAccount(account)
                .setPassword(password)
                .setTimeStamp(System.currentTimeMillis());
        Call<Login.LoginResponse> call = loginService.signUpRequest(builder.build());
        call.enqueue(callbackLogin);
        //SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        //finish();
    };

    private final View.OnClickListener clickListenerExchangeState = v -> {
        if (loginState) {
            findViewById(R.id.login_text_create_account).setVisibility(View.GONE);
            accountEdit.setHint(R.string.user_center_login_input_text);
            btnLogin.setText(R.string.login);
            findViewById(R.id.login_input_email_layout).setVisibility(View.GONE);
            TextView textView = findViewById(R.id.exchange_text_account);
            textView.setText(R.string.user_center_login_exchange_sign_text);
            TextView textViewBtn = findViewById(R.id.exchange_text_login_or_sign);
            textViewBtn.setText(R.string.sign_up);
            loginState = false;
        } else {
            findViewById(R.id.login_text_create_account).setVisibility(View.VISIBLE);
            accountEdit.setHint(R.string.user_center_login_create_text);
            btnLogin.setText(R.string.sign_up);
            findViewById(R.id.login_input_email_layout).setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.exchange_text_account);
            textView.setText(R.string.user_center_login_exchange_login_text);
            TextView textViewBtn = findViewById(R.id.exchange_text_login_or_sign);
            textViewBtn.setText(R.string.login);
            loginState = true;
        }
    };

    private final View.OnClickListener clickListenerHideOrShow = v -> {
        ImageView imageView = findViewById(R.id.password_display_icon);
        int pos = passwordEdit.getSelectionStart();
        if (passwordHideOrShow) {
            imageView.setImageResource(R.drawable.ic_icon_password_show);
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imageView.setImageResource(R.drawable.ic_icon_password_hide);
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        passwordEdit.setSelection(pos);
        passwordHideOrShow = !passwordHideOrShow;
    };

    private void setMentionText(TextView textView, int textId) {
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.RED);
        textView.setTextSize(12);
        textView.setText(textId);
        textView.postDelayed(() -> textView.setVisibility(View.INVISIBLE), 3000);
    }

    private void toastInMain(int id, int length) {
        Toast.makeText(this, id, length).show();
    }

    public static String sha256Encrypt(String password) {
        byte[] result;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            result = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return toHexString(result);
    }

    public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(bytes.length << 1);
            for (byte aByte : bytes) {
                sb.append(hex[(aByte & 240) >> 4]).append(hex[aByte & 15]);
            }
            return sb.toString();
        }
    }


}
