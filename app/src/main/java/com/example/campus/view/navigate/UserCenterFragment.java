package com.example.campus.view.navigate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campus.R;

public class UserCenterFragment extends Fragment {

    private static UserCenterFragment fragment;
    public static UserCenterFragment newInstance(){
        if (fragment == null) {
            fragment = new UserCenterFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_center_fragment,null);
        return view;
    }
}
