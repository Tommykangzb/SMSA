package com.example.campus.adaptar;

import static com.example.campus.view.ImageHelper.setViewText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.view.Constance;
import com.example.campus.view.message.ChatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageFragmentContainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageFragmentAdapter";
    private final int mSize = 7;
    private List<String> dataList = new ArrayList<>(Arrays.asList("酒文化鉴赏与啤酒的酿造工艺讲解", "现代小说选集", "水利工程概论", "计算机概论", "医学解剖", "材料力学基础", "天体物理"));
    private List<String> dataListName = new ArrayList<>(Arrays.asList("刘墉", "方猪猪", "海盗狗", "海盗猪", "Aadsa", "Ddidae", "Mhala"));
    private List<String> dataListDate = new ArrayList<>(Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"));

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_fragment_contain_adpater, parent, false);
        Log.e(TAG, "onCreateViewHolder");
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setViewText(holder, R.id.chat_contain_name, dataListName.get(position));
        setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
        setViewText(holder, R.id.chat_contain_message, dataList.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull List payloads) {
        if (payloads.isEmpty()) {
            setViewText(holder, R.id.chat_contain_name, dataListName.get(position));
            setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
            setViewText(holder, R.id.chat_contain_message, dataList.get(position));
            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Constance.KEY_INTENT_CHAT_NAME, dataListName.get(position));
                context.startActivity(intent);
            });
            holder.itemView.setOnLongClickListener(v -> {
                //PopupMenu
                return true;
            });
        } else {
            int type = (int) payloads.get(0);
            updateItem(position, type, holder);
        }
    }


    @Override
    public int getItemCount() {
        return mSize;
    }

    private void updateItem(final int position, int type, RecyclerView.ViewHolder holder) {
        switch (type) {
            //更新消息内容
            case 0:
                setViewText(holder, R.id.chat_contain_message, dataList.get(position));
                break;
            //更新消息人名字
            case 1:
                setViewText(holder, R.id.chat_contain_name, dataListName.get(position));
                break;
            //更新日期
            case 2:
                setViewText(holder, R.id.chat_contain_date, dataListDate.get(position));
                break;
            default:
                break;
        }
    }

    private final View.OnClickListener messageClickListener = v -> {
        Context context = v.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        TextView textName = ((Activity) context).findViewById(R.id.chat_contain_name);
        intent.putExtra(Constance.KEY_INTENT_CHAT_NAME, textName.getText());
        context.startActivity(intent);
    };

}
