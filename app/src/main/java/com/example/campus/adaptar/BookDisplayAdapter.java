package com.example.campus.adaptar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDisplayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BookDisplayAdapter";
    private int mSize;
    private List<List<String>> dataList = new ArrayList<>(Arrays.asList(Arrays.asList("高等数学", "线性代数"),
            Arrays.asList("计算机概论", "计算机组成"), Arrays.asList("平凡的世界", "白鹿原"), Arrays.asList("论语")));
    private List<List<String>> dataListPrice = new ArrayList<>(Arrays.asList(Arrays.asList("65", "26"),
            Arrays.asList("29", "34"), Arrays.asList("45", "36"), Arrays.asList("22")));

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_display_contain_adpater, parent, false);
        mSize = dataList.size();
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ShapeableImageView v = holder.itemView.findViewById(R.id.book_deal_first_image);
        if (v == null) {
            return;
        }
        v.setBackground(v.getContext().getDrawable(R.drawable.avatar));
        TextView textViewIntroFirst = holder.itemView.findViewById(R.id.book_deal_first_intro_text);
        textViewIntroFirst.setText(dataList.get(position).get(0));
        TextView textViewPriceFirst = holder.itemView.findViewById(R.id.book_deal_first_price);
        textViewPriceFirst.setText(dataListPrice.get(position).get(0));
        if (dataList.get(position).size() == 1) {
            return;
        }
        ShapeableImageView vSec = holder.itemView.findViewById(R.id.book_deal_second_image);
        vSec.setBackground(v.getContext().getDrawable(R.drawable.avatar));
        TextView textViewIntroSec = holder.itemView.findViewById(R.id.book_deal_second_intro_text);
        textViewIntroSec.setText(dataList.get(position).get(1));
        TextView textViewPriceSec = holder.itemView.findViewById(R.id.book_deal_second_price);
        textViewPriceSec.setText(dataListPrice.get(position).get(1));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
