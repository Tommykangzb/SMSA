package com.example.campus.adaptar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.AccessUserMessage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter {
    private List<AccessUserMessage.SingleUserMsgModel> friendResult = new ArrayList<>();
    private Activity activity;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity = (Activity) parent.getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_book_display_contain_adpater, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView name = activity.findViewById(R.id.userName);
        TextView account = activity.findViewById(R.id.account);
        TextView school = activity.findViewById(R.id.school);
        TextView grade = activity.findViewById(R.id.grade);
        TextView des = activity.findViewById(R.id.selfDes);
        ShapeableImageView avatar = activity.findViewById(R.id.add_friend_avatar);
        Glide.with(activity)
                .load(RetrofitConfig.avatarHost + friendResult.get(position).getUserAvatarUrl())
                .placeholder(R.drawable.image_unload)
                .into(avatar);


    }

    @Override
    public int getItemCount() {
        return friendResult.size();
    }

    public void updateData(List<AccessUserMessage.SingleUserMsgModel> list){
        int lastSize = friendResult.size();
        friendResult.addAll(list);
        notifyItemRangeChanged(lastSize,list.size());
    }
}
