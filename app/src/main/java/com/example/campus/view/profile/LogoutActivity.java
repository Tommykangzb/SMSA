package com.example.campus.view.profile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus.R;
import com.example.campus.helper.PswVerifyHelper;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.Login;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutActivity extends AppCompatActivity {
    private static final String TAG = "PasswordEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_center_edit_psw);
        initHomeFragment();
    }

    private void initHomeFragment() {
        findViewById(R.id.btn_login_back).setOnClickListener(v -> finish());
        findViewById(R.id.psw_edit_text_new).setVisibility(View.GONE);
        findViewById(R.id.psw_edit_text_new_again).setVisibility(View.GONE);
        findViewById(R.id.edit_psw_new).setVisibility(View.GONE);
        findViewById(R.id.edit_psw_new_again).setVisibility(View.INVISIBLE);
        TextView textView = findViewById(R.id.psw_edit_text_origin);
        textView.setText(R.string.user_center_login_hint_password_text);
        Button btnConfirm = findViewById(R.id.btn_user_center_psw__edit);
        btnConfirm.setOnClickListener(clickListener);
    }

    private final View.OnClickListener clickListener = v -> {
        EditText editTextOrigin = findViewById(R.id.edit_psw_origin);
        String psw = editTextOrigin.getText().toString();
        SharedPreferences spf = getSharedPreferences("data", MODE_PRIVATE);
        String account = spf.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "");
        PswVerifyHelper.verifyPsw(account, psw, new Callback<Login.LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<Login.LoginResponse> call,
                                   @NonNull Response<Login.LoginResponse> response) {
                if (response.body() == null) {
                    toastInMain(R.string.request_error);
                    Log.e(TAG, "response is null");
                    return;
                }
                Login.LoginResponse responseBody = response.body();
                switch (responseBody.getLoginResult()) {
                    case -3:
                        toastInMain(R.string.request_error);
                        break;
                    case 0:
                        toastInMain(R.string.login_request_password_error);
                        break;
                    case 1:
                        Login.LoginRequest.Builder builder = Login.LoginRequest.newBuilder();
                        builder.setAccount(account)
                                .setPassword(psw)
                                .setTimeStamp(System.currentTimeMillis());
                        ApiService deleteService = RetrofitConfig.getInstance().getService(ApiService.class);
                        Call<Login.LoginResponse> callDelete = deleteService.deleteAccountRequest(builder.build());
                        callDelete.enqueue(new Callback<Login.LoginResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<Login.LoginResponse> call,
                                                   @NonNull Response<Login.LoginResponse> response) {
                                toastInMain(R.string.login_request_delete_account_succeed);
                                PswVerifyHelper.clearUserSpf((Activity) v.getContext());
                                finish();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Login.LoginResponse> call, @NonNull Throwable t) {
                                toastInMain(R.string.login_request_delete_account_fail);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login.LoginResponse> call, @NonNull Throwable t) {
                toastInMain(R.string.login_request_password_error);
            }
        });
    };

    private void toastInMain(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
}
