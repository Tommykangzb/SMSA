package com.example.campus.adaptar;

import static com.example.campus.view.ImageHelper.setViewText;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.FriendsList;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;
import com.example.campus.view.message.ChatActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragmentContainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageFragmentAdapter";
    private int mSize;
    private final List<String> dataListDate = new ArrayList<>(Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"));
    private Set<String> friendSet;
    private List<FriendsList.AccessUserMsgModel> friendModelList;
    private String curAccount;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_fragment_contain_adpater, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setViewText(holder, R.id.chat_contain_name, friendModelList.get(position).getName());
        setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
        setViewText(holder, R.id.chat_contain_message, friendModelList.get(position).getUserSelfDes());
        ShapeableImageView avatar = holder.itemView.findViewById(R.id.chat_contain_avatar);
        Glide.with(holder.itemView.getContext())
                .load(RetrofitConfig.avatarHost + friendModelList.get(position).getUserAvatarUrl())
                .placeholder(R.drawable.image_unload)
                .into(avatar);
        holder.itemView.setOnClickListener(v -> {
//                if (friendList == null || friendList.size() <= position){
//                    return;
//                }
            Context context = v.getContext();
            Intent intent = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            SharedPreferences spf = holder.itemView.getContext().getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE);
            String uid = spf.getString(Constance.KEY_USER_CENTER_USER_UID, "");
            bundle.putString(Constance.KEY_INTENT_CHAT_NAME, friendModelList.get(position).getName());
            bundle.putString(Constance.KEY_USER_CENTER_USER_ACCOUNT, curAccount);
            bundle.putString(Constance.KEY_USER_CENTER_USER_UID, uid);
            bundle.putString(Constance.KEY_REMOTE_UID, friendModelList.get(position).getUid());
            bundle.putString(Constance.KEY_REMOTE_NAME, friendModelList.get(position).getName());
            bundle.putString(Constance.KEY_REMOTE_AVATAR, friendModelList.get(position).getUserAvatarUrl());
            bundle.putString(Constance.KEY_USER_CENTER_USER_AVATAR_URL, "");
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            //PopupMenu
            return true;
        });
    }

//    @Override
//    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull List payloads) {
//        if (payloads.isEmpty()) {
//            setViewText(holder, R.id.chat_contain_name, dataListName.get(position));
//            setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
//            setViewText(holder, R.id.chat_contain_message, dataList.get(position));
//            holder.itemView.setOnClickListener(v -> {
////                if (friendList == null || friendList.size() <= position){
////                    return;
////                }
//                Context context = v.getContext();
//                Intent intent = new Intent(context, ChatActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(Constance.KEY_INTENT_CHAT_NAME,friendList.get(position).getName());
//                bundle.putString(Constance.KEY_USER_CENTER_USER_ACCOUNT,curAccount);
//                bundle.putString(Constance.KEY_REMOTE_UID,friendList.get(position).getUid());
//                bundle.putString(Constance.KEY_REMOTE_NAME,friendList.get(position).getName());
//                bundle.putString(Constance.KEY_REMOTE_AVATAR,friendList.get(position).getUserAvatarUrl());
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            });
//            holder.itemView.setOnLongClickListener(v -> {
//                //PopupMenu
//                return true;
//            });
//        } else {
//            int type = (int) payloads.get(0);
//            updateItem(position, type, holder);
//        }
//    }

    @Override
    public int getItemCount() {
        return mSize;
    }


    public void requestFriendList(View view) {
        SharedPreferences spf = view.getContext().getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE);
        curAccount = spf.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "");
        if (TextUtils.isEmpty(curAccount)) {
            return;
        }
        if (friendSet == null) {
            friendSet = new LinkedHashSet<>();
        }
        if (friendModelList == null) {
            friendModelList = new ArrayList<>();
        }
        FriendsList.FriendsListRequest.Builder builder = FriendsList.FriendsListRequest.newBuilder();
        builder.setCount(-1)
                .setCurrAccount(curAccount);
        ApiService service = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<FriendsList.FriendsListResponse> request = service.accessFriendList(builder.build());
        request.enqueue(new Callback<FriendsList.FriendsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<FriendsList.FriendsListResponse> call,
                                   @NonNull Response<FriendsList.FriendsListResponse> response) {
                if (response.body() == null) {
                    return;
                }
                FriendsList.FriendsListResponse rsp = response.body();
                for (FriendsList.AccessUserMsgModel model : rsp.getFriendsList()) {
                    if (!friendSet.contains(model.getUid())) {
                        friendSet.add(model.getUid());
                        friendModelList.add(model);
                    }
                }
                int lastSize = mSize;
                mSize = friendModelList.size();
                notifyItemRangeChanged(lastSize, mSize);
            }

            @Override
            public void onFailure(@NonNull Call<FriendsList.FriendsListResponse> call,
                                  @NonNull Throwable t) {
                Toast.makeText(view.getContext(), R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private final View.OnClickListener messageClickListener = v -> {
        Context context = v.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        TextView textName = ((Activity) context).findViewById(R.id.chat_contain_name);
        intent.putExtra(Constance.KEY_INTENT_CHAT_NAME, textName.getText());
        context.startActivity(intent);
    };

}
