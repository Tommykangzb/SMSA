package com.example.campus.view.navigate;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.campus.R;

public class TopNavigateBar {
    public static void addTopBar(Activity activity, LinearLayout rootLayout, String content) {
        RelativeLayout layout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setPadding(15, 15, 15, 15);
        layout.setLayoutParams(params);
        ImageView imageViewBack = new ImageView(activity);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(80, 80);
        imageParams.setMargins(10, 10, 0, 10);
        imageViewBack.setLayoutParams(imageParams);
        imageViewBack.setImageResource(R.drawable.ic_icon_back_black);
        imageViewBack.setOnClickListener(v -> activity.finish());
        layout.addView(imageViewBack);
        TextView textView = new TextView(activity);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(textParams);
        textView.setText(content);
        textView.setTextSize(18);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(Color.BLACK);
        layout.addView(textView);
        rootLayout.addView(layout, 0);
    }
}
