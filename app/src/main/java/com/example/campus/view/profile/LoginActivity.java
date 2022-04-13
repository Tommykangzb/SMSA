package com.example.campus.view.profile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus.R;
import com.example.campus.helper.InputHandleUtil;
import com.example.campus.helper.ScreenHelp;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText accountEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button btnLogin;
    private TextView accountMention;
    private TextView passwordMention;
    private TextView emailMention;
    // loginState is true when need to create an account.
    // if have an account, loginState will be false
    private boolean loginState = false;
    private boolean passwordHideOrShow = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenHelp.enableTranslucentStatusBar(this);
        setContentView(R.layout.layout_login_or_sign);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_login_back).setOnClickListener(clickListenerBtnBack);
        accountEdit = findViewById(R.id.login_input_create_account);
        accountEdit.setOnFocusChangeListener(accountListener);
        emailEdit = findViewById(R.id.login_input_input_email);
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

    private final View.OnClickListener clickListenerBtnLogin = v -> {
        Log.e(TAG, "loginState: " + loginState);
        if (!loginState) {
            //登录态 todo
            finish();
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
        String email = emailEdit.getText().toString();
        if (!InputHandleUtil.Companion.checkEmail(email)) {
            setMentionText(emailMention, R.string.user_center_email_mention);
            return;
        }
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();

        //editor.putString(Constance.KEY_USER_CENTER_USER_ACCOUNT,account);
        finish();
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
}
