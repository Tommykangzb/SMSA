package com.example.campus.adaptar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.BookDealHome;
import com.example.campus.view.Constance;
import com.example.campus.view.deal.BookDetailActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class BookDisplayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BookDisplayAdapter";
    private int mSize;
    private List<BookDealHome.BookMessage> dataList = new ArrayList<>();
    private Activity activity;
    private Bundle bundle;
    private Intent intent;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity = (Activity) parent.getContext();
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_book_display_contain_adpater, parent, false);
        mSize = (dataList.size() + 1) / 2;
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (intent == null || bundle == null) {
            intent = new Intent(activity, BookDetailActivity.class);
            bundle = new Bundle();
            //intent.putExtras(bundle);
        }
        if (position * 2 >= dataList.size()) {
            return;
        }
        updateFirstItem(position, holder);
        if (dataList.size() < (position + 1) * 2) {
            return;
        }
        updateSecondItem(position, holder);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public void updateData(List<BookDealHome.BookMessage> list) {
        dataList.addAll(list);
        notifyItemRangeChanged(mSize, (list.size() + 1) / 2);
        mSize += (list.size() + 1) / 2;
    }

    @SuppressLint("SetTextI18n")
    private void updateFirstItem(int position, @NonNull RecyclerView.ViewHolder holder) {
        int index = position * 2;
        ShapeableImageView v = holder.itemView.findViewById(R.id.book_deal_first_image);
        String url = dataList.get(index).getBookImageUrl();
        v.setAdjustViewBounds(true);
        Log.e(TAG, "AvatarUrl： " + url);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity)
                    .load(RetrofitConfig.avatarHost + url)
                    .placeholder(R.drawable.image_unload)
                    .into(v);
        }
        String urlAvatar = dataList.get(index).getSellerAvatarUrl();
        ShapeableImageView avatar = holder.itemView.findViewById(R.id.book_deal_first_avatar);
        if (!TextUtils.isEmpty(urlAvatar)) {
            Glide.with(activity)
                    .load(RetrofitConfig.avatarHost + urlAvatar)
                    .placeholder(R.drawable.image_unload)
                    .into(avatar);
        }
        TextView textViewIntroFirst = holder.itemView.findViewById(R.id.book_deal_first_intro_text);
        textViewIntroFirst.setText(dataList.get(index).getBookName());
        TextView textViewPriceFirst = holder.itemView.findViewById(R.id.book_deal_first_price);
        textViewPriceFirst.setText("￥" + dataList.get(index).getPrice());
        TextView textViewSellerFirst = holder.itemView.findViewById(R.id.book_deal_first_seller);
        textViewSellerFirst.setText(dataList.get(index).getSellerName());
        holder.itemView.findViewById(R.id.book_deal_first_layout).setOnClickListener(v1 -> {
            bundle.putString(Constance.KEY_BOOK_DEAL_SELLER, dataList.get(index).getSellerName());
            bundle.putString(Constance.KEY_BOOK_DEAL_AVATAR_URL, dataList.get(index).getSellerAvatarUrl());
            bundle.putLong(Constance.KEY_BOOK_DEAL_SELLER_ID, dataList.get(index).getSellerId());
            bundle.putLong(Constance.KET_DEAL_BOOK_ID, dataList.get(index).getBookId());
            ArrayList<String> arrayList = new ArrayList<>(dataList.get(index).getBookMsgImageList());
            bundle.putStringArrayList(Constance.KEY_BOOK_DEAL_IMAGE_MSG, arrayList);
            bundle.putString(Constance.KEY_BOOK_DEAL_SELL_MSG, dataList.get(index).getSellMsg());
            bundle.putFloat(Constance.KEY_BOOK_DEAL_SELL_PRICE, dataList.get(index).getPrice());
            bundle.putString(Constance.KEY_BOOK_DEAL_SELL_DES, dataList.get(index).getDescription());
            bundle.putString(Constance.KEY_BOOK_IMAGE_URL, dataList.get(index).getBookImageUrl());
            bundle.putString(Constance.KEY_BOOK_NAME,dataList.get(index).getBookName());
            bundle.putInt(Constance.KEY_BOOK_DETAIL_LIKE_COUNT,dataList.get(index).getLikeCount());
            intent.putExtras(bundle);
            activity.startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateSecondItem(int position, @NonNull RecyclerView.ViewHolder holder) {
        int index = position * 2 + 1;
        ShapeableImageView vSec = holder.itemView.findViewById(R.id.book_deal_second_image);
        vSec.setAdjustViewBounds(true);
        String url = dataList.get(index).getBookImageUrl();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity)
                    .load(RetrofitConfig.avatarHost + url)
                    .placeholder(R.drawable.image_unload)
                    .into(vSec);
        }
        String urlAvatar = dataList.get(index).getSellerAvatarUrl();
        ShapeableImageView avatar = holder.itemView.findViewById(R.id.book_deal_second_avatar);
        if (!TextUtils.isEmpty(urlAvatar)) {
            Glide.with(activity)
                    .load(RetrofitConfig.avatarHost + urlAvatar)
                    .placeholder(R.drawable.image_unload)
                    .into(avatar);
        }
        TextView textViewIntroSec = holder.itemView.findViewById(R.id.book_deal_second_intro_text);
        textViewIntroSec.setText(dataList.get(index).getBookName());
        TextView textViewPriceSec = holder.itemView.findViewById(R.id.book_deal_second_price);
        textViewPriceSec.setText("￥" + dataList.get(index).getPrice());
        TextView textViewSellerSec = holder.itemView.findViewById(R.id.book_deal_second_seller);
        textViewSellerSec.setText(dataList.get(index).getSellerName());
        holder.itemView.findViewById(R.id.book_deal_second_layout).setOnClickListener(v1 -> {
            bundle.putString(Constance.KEY_BOOK_DEAL_SELLER, dataList.get(index).getSellerName());
            bundle.putString(Constance.KEY_BOOK_DEAL_AVATAR_URL, dataList.get(index).getSellerAvatarUrl());
            bundle.putLong(Constance.KEY_BOOK_DEAL_SELLER_ID, dataList.get(index).getSellerId());
            bundle.putLong(Constance.KET_DEAL_BOOK_ID, dataList.get(index).getBookId());
            ArrayList<String> arrayList = new ArrayList<>(dataList.get(index).getBookMsgImageList());
            bundle.putStringArrayList(Constance.KEY_BOOK_DEAL_IMAGE_MSG, arrayList);
            bundle.putString(Constance.KEY_BOOK_DEAL_SELL_MSG, dataList.get(index).getSellMsg());
            bundle.putFloat(Constance.KEY_BOOK_DEAL_SELL_PRICE, dataList.get(index).getPrice());
            bundle.putString(Constance.KEY_BOOK_DEAL_SELL_DES, dataList.get(index).getDescription());
            bundle.putString(Constance.KEY_BOOK_IMAGE_URL, dataList.get(index).getBookImageUrl());
            bundle.putString(Constance.KEY_BOOK_NAME,dataList.get(index).getBookName());
            bundle.putInt(Constance.KEY_BOOK_DETAIL_LIKE_COUNT,dataList.get(index).getLikeCount());
            intent.putExtras(bundle);
            activity.startActivity(intent);
        });
    }
}
