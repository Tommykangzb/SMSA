package com.example.campus.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseFragmentActivity extends AppCompatActivity {
    public abstract void changeFragment(@NonNull Fragment fragment);

    public abstract void hideFragment(@NonNull FragmentTransaction transaction);
}
