package com.example.campus.view.message;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campus.R;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.view.navigate.TopNavigateBar;

public class FriendsManager extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.layout_add_friends);
        LinearLayout layout = findViewById(R.id.root_layout);
        ScreenHelp.setStatusBarColor(this, ScreenHelp.stateBarColorValueWhite);
        ScreenHelp.setAndroidNativeLightStatusBar(this, true);
        TopNavigateBar.addTopBar(this, layout, "添加朋友");
    }
}
