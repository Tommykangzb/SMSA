package com.example.campus.view.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.adaptar.AddFriendAdapter;
import com.example.campus.helper.InputHandleUtil;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.protoModel.AccessUserMessage;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.navigate.TopNavigateBar;
import com.google.android.material.imageview.ShapeableImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsManager extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private EditText searchEdit;
    @Override
    public void onCreate(@Nullable Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.layout_add_friends);
        LinearLayout layout = findViewById(R.id.root_layout);
        ScreenHelp.setStatusBarColor(this, ScreenHelp.stateBarColorValueWhite);
        ScreenHelp.setAndroidNativeLightStatusBar(this, true);
        TopNavigateBar.addTopBar(this, layout, "添加朋友");
        findViewById(R.id.search_result).setVisibility(View.GONE);
        initView();
    }

    private void initView(){
        TextView searchBtn = findViewById(R.id.add_friend_search);
        recyclerView = findViewById(R.id.add_friend_rcv);
        adapter = new AddFriendAdapter();
        searchEdit = findViewById(R.id.edit_search);
        searchBtn.setOnClickListener(v -> {
            v.postDelayed(runnable,1000);
        });
        Button button = findViewById(R.id.add_friend_btn);
        button.setOnClickListener(v -> {
            Toast.makeText(this,"已发送申请信息",Toast.LENGTH_SHORT).show();
        });
    }

    private final Runnable runnable = () -> {
        ShapeableImageView avatar = findViewById(R.id.add_friend_avatar);
        findViewById(R.id.search_result).setVisibility(View.VISIBLE);
        Glide.with(FriendsManager.this)
                .load(RetrofitConfig.avatarHost + "avatar_panda.webp")
                .placeholder(R.drawable.image_unload)
                .into(avatar);
    };

    private final View.OnClickListener searchListener = v -> {
        String account = searchEdit.getText().toString();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(FriendsManager.this, R.string.find_friend_error_account, Toast.LENGTH_SHORT).show();
        }
        if (!InputHandleUtil.Companion.checkAccount(account)) {
            Toast.makeText(FriendsManager.this, R.string.find_friend_error_account, Toast.LENGTH_SHORT).show();
        }
        ApiService service = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<AccessUserMessage.AccessUserMsgResponse> call = service.accessUserMsg(
                AccessUserMessage.AccessUserMsgRequest
                        .newBuilder()
                        .setAccount(account)
                        .build());
        call.enqueue(new Callback<AccessUserMessage.AccessUserMsgResponse>() {
            @Override
            public void onResponse(@NonNull Call<AccessUserMessage.AccessUserMsgResponse> call,
                                   @NonNull Response<AccessUserMessage.AccessUserMsgResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(FriendsManager.this, R.string.request_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = response.body().getResultCode();
                if (code == -1 || code == 0) {
                    Toast.makeText(FriendsManager.this, code == 0 ? R.string.request_error : R.string.find_friend_error_account_not_exist,
                            Toast.LENGTH_SHORT).show();
                    return;
                }


            }

            @Override
            public void onFailure(@NonNull Call<AccessUserMessage.AccessUserMsgResponse> call,
                                  @NonNull Throwable t) {

            }
        });
    };
}
