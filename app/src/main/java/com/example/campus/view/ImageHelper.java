package com.example.campus.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.API.IBitmapLoadAdapter;
import com.example.campus.adaptar.CourseDetailAdapter;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageHelper extends Thread{
    private String imageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2Ftp03%2F1Z92210320C612-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1651893102&t=cccb72041660c03dea897643b02137bf";
    private ShapeableImageView shapeableImageView;
    private Handler handler;
    IBitmapLoadAdapter adapter;

    public ImageHelper(IBitmapLoadAdapter adapter, Handler handler, @Nullable String Url) {
        this.adapter = adapter;
        this.handler = handler;
        if (!TextUtils.isEmpty(Url)) {
            imageUrl = Url;
        }
    }

    @Override
    public void run(){
        super.run();
        loadBitmapFromURL();
    }

    private void loadBitmapFromURL() {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            Message msg = new Message();
            adapter.setBitmap(bitmap);
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        }
    }


    public static void setViewText(RecyclerView.ViewHolder holder, int id, @NonNull String str) {
        View v = holder.itemView.findViewById(id);
        if (v instanceof TextView) {
            ((TextView) v).setText(str);
        }
    }
}
