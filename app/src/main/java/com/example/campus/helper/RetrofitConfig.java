package com.example.campus.helper;

import android.util.Log;

import com.google.protobuf.ExtensionRegistry;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class RetrofitConfig {

    private static final String TAG = "RetrofitConfig";
    private Retrofit retrofit;
    private static RetrofitConfig mRetrofitConfig;

    //读取超时时间
    private static final int DEFAULT_READ_TIMEOUT = 60;
    //链接超时时间
    private static final int DEFAULT_CONN_TIMEOUT = 10;
    //上传超时时间
    private static final int DEFAULT_WRITE_TIMEOUT = 5 * 60;
    //请求地址
    public static final String host = "https://5y15h36412.oicp.vip:443";
    public static final String avatarHost = host + "/upload/";

    /**
     * 创建拦截器
     */
    private final Interceptor httpInterceptor = chain -> {
        Request request = addPublicParameter(chain.request());
        return chain.proceed(request);
    };

    public static RetrofitConfig getInstance() {
        if (mRetrofitConfig == null) {
            synchronized ((RetrofitConfig.class)) {
                if (mRetrofitConfig == null) {
                    mRetrofitConfig = new RetrofitConfig();
                }
            }
        }
        return mRetrofitConfig;
    }

    public <T> T getService(Class<T> clz) {
        if (retrofit == null) {
            //无条件信任所有证书
//            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder()
//                    .connectTimeout(DEFAULT_CONN_TIMEOUT, TimeUnit.SECONDS)//连接超时时间
//                    .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)//读取超时时间设置
//                    .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)//写入超时时间设置
//                    .addInterceptor(httpInterceptor)//添加拦截器
//                    .retryOnConnectionFailure(true)//错误重连
//                    .hostnameVerifier((s, sslSession) -> true);

            retrofit = new Retrofit.Builder()
                    //.client(httpBuilder.build())
                    .baseUrl(host) // 设置 网络请求 Url
                    .addConverterFactory(ProtoConverterFactory.create())
                    //.addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                    .build();
        }
        return retrofit.create(clz);
    }

    /**
     * 给拦截器request路径添加公共参数
     * 登录前和登录后所添加的公共参数不一样
     *
     * @param request 请求
     * @return 请求
     */
    private Request addPublicParameter(Request request) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpUrl.Builder builder = request
                .url()
                .newBuilder()
                .addQueryParameter("timestamp", timestamp);//添加公共参数(每个接口都携带的参数，还可以继续add)
        Log.e(TAG, "url = " + builder.build());
        return request
                .newBuilder()
                .method(request.method(), request.body())
                .url(builder.build())
                .build();
    }
}
