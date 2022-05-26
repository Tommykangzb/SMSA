package com.example.campus.view.deal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campus.R;
import com.example.campus.adaptar.BookDisplayAdapter;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.protoModel.BookDealHome;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("UseCompatLoadingForDrawables")
public class BookDealFragment extends Fragment {
    private static final String TAG = "BookDealFragment";
    public static BookDealFragment fragment;
    private BookDisplayAdapter adapter;
    private boolean isLoadAll = false;
    private LinearLayout popularLinearLayout;
    private Activity activity;
    private Bundle bundle;
    private List<BookDealHome.BookMessage> hotBookList;

    public static BookDealFragment newInstance() {
        if (fragment == null) {
            fragment = new BookDealFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        bundle = getArguments();
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_book_deal_fragment, null);
        initView(view);
        //设置搜索框以及背景
        LinearLayout layout = view.findViewById(R.id.top_message_layout);
        LinearLayout.LayoutParams LParams = new LinearLayout.LayoutParams(layout.getLayoutParams());
        LParams.setMargins(60, 0, 0, 0);
        layout.setLayoutParams(LParams);
        layout.setBackground(activity.getDrawable(R.color.white));
        addMenuView(layout, (v -> Log.i(TAG, "click")));
        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setBackground(activity.getDrawable(R.drawable.book_deal_search_bg));

        RecyclerView recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        adapter = new BookDisplayAdapter();
        recyclerViewContain.setAdapter(adapter);
        requestData();
        ScreenHelp.setStatusBarColor(activity, ScreenHelp.stateBarColorValueWhite);
        ScreenHelp.setAndroidNativeLightStatusBar(activity, true);
        return view;
    }

    public void initView(View view) {
        popularLinearLayout = view.findViewById(R.id.linear_popular_item);
        popularLinearLayout.setPadding(0, 20, 0, 20);
        //增加热门旧书
        TextView popularText = view.findViewById(R.id.popular_text);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(80, 50, 0, 0);
        popularText.getPaint().setFakeBoldText(true);
        popularText.setLayoutParams(textParam);
        popularText.setText(R.string.current_popular_text);

    }

    private void addPopularView(LinearLayout linearLayout, int position, View.OnClickListener clickListener, String url, String bookName) {
        //外层的容器
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemParam.setMargins(40, 10, 40, 10);
        itemParam.gravity = Gravity.CENTER_VERTICAL;
        layout.setLayoutParams(itemParam);
        layout.setOnClickListener(clickListener);

        //添加上层的图片
        ShapeableImageView shapeView = new ShapeableImageView(activity);
        //图片固定大小
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(160, 160);
        imageParam.gravity = Gravity.CENTER_HORIZONTAL;
        shapeView.setLayoutParams(imageParam);
        ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build();
        shapeView.setShapeAppearanceModel(shape);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity)
                    .load(RetrofitConfig.avatarHost + url)
                    .placeholder(R.drawable.image_unload)
                    .into(shapeView);
        }
        layout.addView(shapeView);
        //添加下面的文字
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(textParam);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(12);
        textView.setText(bookName);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        layout.addView(textView);
        linearLayout.addView(layout, position);
    }

    private void addMenuView(LinearLayout linearLayout, View.OnClickListener clickListener) {
        ImageView imageView = new ImageView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(80, 0, 0, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(params);
        imageView.setBackground(activity.getDrawable(R.drawable.ic_icon_create_subject));
        imageView.setOnClickListener(clickListener);
        linearLayout.addView(imageView);
    }

    private void requestData() {
        BookDealHome.BookDealRequest.Builder requestBuilder = BookDealHome.BookDealRequest.newBuilder();
        requestBuilder.setLimitCount(20)
                .setUserId(12345678L);
        ApiService bookDealApi = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<BookDealHome.BookDealResponse> callOfBookDealView = bookDealApi.getCommodity(requestBuilder.build());
        //异步请求
        callOfBookDealView.enqueue(new Callback<BookDealHome.BookDealResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookDealHome.BookDealResponse> call,
                                   @NonNull Response<BookDealHome.BookDealResponse> response) {
                if (response.body() == null) {
                    Log.e(TAG, "response is null");
                    return;
                }
                BookDealHome.BookDealResponse bookDealResponse = response.body();
                adapter.updateData(bookDealResponse.getCommodity2List());
                updateHotList(bookDealResponse.getHotBook1List());
                isLoadAll = bookDealResponse.getLoadAll();
            }

            @Override
            public void onFailure(@NonNull Call<BookDealHome.BookDealResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "fail " + t);
            }
        });
    }

    private final View.OnClickListener clickListenerHotBook = v -> {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        int index = popularLinearLayout.indexOfChild(v);
        bundle.putString(Constance.KEY_BOOK_DEAL_SELLER, hotBookList.get(index).getSellerName());
        bundle.putString(Constance.KEY_BOOK_DEAL_AVATAR_URL, hotBookList.get(index).getSellerAvatarUrl());
        bundle.putLong(Constance.KEY_BOOK_DEAL_SELLER_ID, hotBookList.get(index).getSellerId());
        bundle.putLong(Constance.KET_DEAL_BOOK_ID, hotBookList.get(index).getBookId());
        ArrayList<String> arrayList = new ArrayList<>(hotBookList.get(index).getBookMsgImageList());
        bundle.putStringArrayList(Constance.KEY_BOOK_DEAL_IMAGE_MSG, arrayList);
        bundle.putString(Constance.KEY_BOOK_DEAL_SELL_MSG, hotBookList.get(index).getSellMsg());
        bundle.putFloat(Constance.KEY_BOOK_DEAL_SELL_PRICE,hotBookList.get(index).getPrice());
        bundle.putString(Constance.KEY_BOOK_DEAL_SELL_DES,hotBookList.get(index).getDescription());
        bundle.putString(Constance.KEY_BOOK_IMAGE_URL,hotBookList.get(index).getBookImageUrl());
        bundle.putString(Constance.KEY_BOOK_NAME,hotBookList.get(index).getBookName());
        bundle.putInt(Constance.KEY_BOOK_DETAIL_LIKE_COUNT,hotBookList.get(index).getLikeCount());
        intent.putExtras(bundle);
        activity.startActivity(intent);
    };

    public void updateHotList(List<BookDealHome.BookMessage> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        hotBookList = list;
        for (int i = 0; i < list.size(); i++) {
            addPopularView(popularLinearLayout, i, clickListenerHotBook,
                    list.get(i).getBookImageUrl(), list.get(i).getBookName());
        }
    }

}
