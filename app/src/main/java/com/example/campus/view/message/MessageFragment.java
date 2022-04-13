package com.example.campus.view.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.adaptar.MessageFragmentContainAdapter;

import java.util.Objects;

public class MessageFragment extends Fragment {
    private static MessageFragment fragment;

    public static MessageFragment newInstance(){
        if (fragment == null) {
            fragment = new MessageFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_message_fragment,null);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView_chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new MessageFragmentContainAdapter());
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueWhite);
        ScreenHelp.setAndroidNativeLightStatusBar(Objects.requireNonNull(getActivity()),true);
        return view;
    }

}