package com.example.campus.helper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class RetrofitHelper {
    private static final String host = "http://192.168.0.103:8080/";

    public static <T> T getService(Class<T> clz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host) // 设置 网络请求 Url
                .addConverterFactory(ProtoConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
        //Method []methods = clz.getDeclaredMethods();
        //clz
        return retrofit.create(clz);
    }
}
