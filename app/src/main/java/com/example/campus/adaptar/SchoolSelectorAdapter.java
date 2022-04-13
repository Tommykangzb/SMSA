package com.example.campus.adaptar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.campus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SchoolSelectorAdapter extends ArrayAdapter {
    List<String> schoolData;

    public SchoolSelectorAdapter(@NonNull List<String> list, Context context, int resId) {
        super(context,resId);
        if (list.size() == 0) {
            schoolData = new ArrayList<>(Collections.singletonList(" "));
            return;
        }
        schoolData = list;
    }


    @Override
    public int getCount() {
        return schoolData.size();
    }

    @Override
    public Object getItem(int position) {
        return schoolData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.layout_school_selector,null);
        if (convertView != null){
            ((TextView)convertView.findViewById(android.R.id.text1)).setText(schoolData.get(position));
        }
        return convertView;
    }

    public void updateData(List<String> list){

        notifyDataSetChanged();
    }

}
