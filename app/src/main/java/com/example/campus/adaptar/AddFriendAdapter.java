package com.example.campus.adaptar;

import android.app.Activity;
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
import com.example.campus.protoModel.AccessUserMessage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AccessUserMessage.SingleUserMsgModel> friendResult = new ArrayList<>();
    private Activity activity;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity = (Activity) parent.getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_adapter_search_friend_result, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (activity == null) {
            return;
        }
        TextView name = activity.findViewById(R.id.search_result_name);
        TextView account = activity.findViewById(R.id.search_result_account);
        TextView school = activity.findViewById(R.id.search_result_school);
        TextView sex = activity.findViewById(R.id.search_result_sex);
        ShapeableImageView avatar = activity.findViewById(R.id.search_result_avatar);
        Glide.with(activity)
                .load(RetrofitConfig.avatarHost + friendResult.get(position).getUserAvatarUrl())
                .placeholder(R.drawable.image_unload)
                .into(avatar);
        TextView searchBtn = activity.findViewById(R.id.search_result_add);
        searchBtn.setOnClickListener(v -> Toast.makeText(activity, "已发送申请信息", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return friendResult.size();
    }

    public void updateData(List<AccessUserMessage.SingleUserMsgModel> list) {
        if (list == null) {
            return;
        }
        int lastSize = friendResult.size();
        friendResult.addAll(list);
        notifyItemRangeChanged(lastSize, list.size());
    }
}
