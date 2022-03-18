package com.example.campus.helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class ScreenHelp {
    public static final String stateBarColorValueWhite = "#FFFFFFFF";
    public static final String stateBarColorValueYellow = "#FFFFEB3B";
    public static final String stateBarColorValueBlue = "#FF54A2EF";
    public static void setStatusBarColor(Activity activity, String colorId) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor(colorId));
    }

    public static void setAndroidNativeLightStatusBar(@NonNull Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        View v = ((ViewGroup)activity.getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        v.setFitsSystemWindows(true);
    }
}
