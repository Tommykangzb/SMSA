package com.example.campus.adaptar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SchoolSelectorAdapter extends BaseAdapter {
    List<String> schoolData;

    public SchoolSelectorAdapter(@NonNull List<String> list) {
        if (list.size() == 0) {
            schoolData = new ArrayList<>(Collections.singletonList(" "));
            return;
        }
        schoolData = list;
    }

    public SchoolSelectorAdapter(@NonNull String[] data) {
        if (data.length == 0) {
            schoolData = new ArrayList<>(Collections.singletonList(" "));
            return;
        }
        schoolData = Arrays.asList(data);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
