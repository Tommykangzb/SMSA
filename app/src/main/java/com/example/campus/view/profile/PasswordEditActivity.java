package com.example.campus.view.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus.R;
import com.example.campus.helper.InputHandleUtil;
import com.example.campus.helper.PswVerifyHelper;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.Login;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordEditActivity extends AppCompatActivity {
    private static final String TAG = "PasswordEditActivity";
    private boolean pswIsRight = false;
    private EditText editTextNew;
    private EditText editTextNewAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_center_edit_psw);
        initHomeFragment();
    }

    public void initHomeFragment(){
        findViewById(R.id.btn_login_back).setOnClickListener(v -> finish());
        Button btnConfirm = findViewById(R.id.btn_user_center_psw__edit);
        btnConfirm.setOnClickListener(clickListenerPswEdit);
        editTextNew = findViewById(R.id.edit_psw_new);
        editTextNewAgain = findViewById(R.id.edit_psw_new_again);
    }

    private final View.OnClickListener clickListenerPswEdit = v -> {
        EditText editTextOrigin = findViewById(R.id.edit_psw_origin);
        String psw = editTextOrigin.getText().toString();
        SharedPreferences spf = getSharedPreferences(Constance.USER_DATA, MODE_PRIVATE);
        String account = spf.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "");
        PswVerifyHelper.verifyPsw(account, psw, new Callback<Login.LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<Login.LoginResponse> call,
                                   @NonNull Response<Login.LoginResponse> response) {
                if (response.body() == null) {
                    toastInMain(R.string.request_error, Toast.LENGTH_SHORT);
                    Log.e(TAG, "response is null");
                    return;
                }
                Login.LoginResponse responseBody = response.body();
                switch (responseBody.getLoginResult()) {
                    case -3:
                        toastInMain(R.string.request_error, Toast.LENGTH_SHORT);
                        break;
                    case 0:
                        pswIsRight = false;
                        toastInMain(R.string.login_request_password_error, Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        String pswNew = editTextNew.getText().toString();
                        String pswNewAgain = editTextNewAgain.getText().toString();
                        if (!pswNew.equals(pswNewAgain)) {
                            toastInMain(R.string.user_center_login_password_equal, Toast.LENGTH_LONG);
                            return;
                        }
                        if (!InputHandleUtil.Companion.checkPassword(pswNew)) {
                            toastInMain(R.string.user_center_password_mention, Toast.LENGTH_LONG);
                            return;
                        }
                        Login.LoginRequest.Builder builder = Login.LoginRequest.newBuilder();
                        builder.setAccount(account)
                                .setPassword(psw)
                                .setTimeStamp(System.currentTimeMillis());
                        ApiService editService = RetrofitConfig.getInstance().getService(ApiService.class);
                        Call<Login.LoginResponse> callEditPsw = editService.loginRequest(builder.build());
                        callEditPsw.enqueue(new Callback<Login.LoginResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<Login.LoginResponse> call,
                                                   @NonNull Response<Login.LoginResponse> response) {
                                Toast.makeText(getCurrentFocus().getContext(),
                                        R.string.login_request_edit_password_succeed,
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Login.LoginResponse> call,
                                                  @NonNull Throwable t) {
                                Toast.makeText(getCurrentFocus().getContext(),
                                        R.string.request_net_error,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        pswIsRight = true;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login.LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(getCurrentFocus().getContext(),R.string.request_net_error,Toast.LENGTH_SHORT).show();
            }
        });
    };

    private void toastInMain(int id, int length) {
        Toast.makeText(this, id, length).show();
    }

}
