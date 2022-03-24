package com.example.campus.view.navigate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.adaptar.BookDisplayAdapter;
import com.example.campus.helper.ScreenHelp;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.Objects;

public class BookDealFragment extends Fragment {
    private static final String TAG = "BookDealFragment";
    public static BookDealFragment fragment;

    public static BookDealFragment newInstance() {
        if (fragment == null) {
            fragment = new BookDealFragment();
            Bundle bundle = new Bundle();
            //bundle.putString("from", from);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //复用课程评价的布局
        View view = inflater.inflate(R.layout.home_fragment_layout, null);
        initView(view);
        //设置搜索框以及背景
        LinearLayout layout = view.findViewById(R.id.top_message_layout);
        LinearLayout.LayoutParams LParams = new LinearLayout.LayoutParams(layout.getLayoutParams());
        LParams.setMargins(60, 0, 0, 0);
        layout.setLayoutParams(LParams);
        layout.setBackground(Objects.requireNonNull(getActivity()).getDrawable(R.color.white));
        addMenuView(layout, (v -> Log.i(TAG, "click")));
        SearchView searchView = view.findViewById(R.id.search_bar);
        searchView.setBackground(getActivity().getDrawable(R.drawable.book_deal_search_bg));

        //view.findViewById(R.id.bottom_layout_category).setVisibility(View.GONE);

        RecyclerView recyclerViewContain = view.findViewById(R.id.recycleView_home_contain);
        recyclerViewContain.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        recyclerViewContain.setAdapter(new BookDisplayAdapter());
        ScreenHelp.setStatusBarColor(Objects.requireNonNull(getActivity()), ScreenHelp.stateBarColorValueWhite);
        ScreenHelp.setAndroidNativeLightStatusBar(getActivity(), true);
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void initView(View view) {
        //隐藏学校以及课程分类等View
        //view.findViewById(R.id.top_school_text).setVisibility(View.GONE);
        //view.findViewById(R.id.category_selector).setVisibility(View.GONE);
        view.findViewById(R.id.top_school_selector).setVisibility(View.GONE);
        view.findViewById(R.id.imageView_create_sub).setVisibility(View.GONE);

        LinearLayout popularLinearLayout = view.findViewById(R.id.linear_popular_item);
        popularLinearLayout.setPadding(0, 20, 0, 20);
        //增加热门旧书
        TextView popularText = view.findViewById(R.id.popular_text);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(80, 50, 0, 0);
        popularText.getPaint().setFakeBoldText(true);
        popularText.setLayoutParams(textParam);
        popularText.setText(R.string.current_popular_text);
        for (int i = 0; i < 10; i++) {
            addPopularView(popularLinearLayout, i, (v -> Log.i(TAG, "click")), getContext().getDrawable(R.drawable.avatar), "高等数学" + i);
        }

    }

    private void addPopularView(LinearLayout linearLayout, int position, View.OnClickListener clickListener, Drawable drawable, String bookName) {
        //外层的容器
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemParam.setMargins(40, 10, 40, 10);
        itemParam.gravity = Gravity.CENTER_VERTICAL;
        layout.setLayoutParams(itemParam);

        //添加上层的图片
        ShapeableImageView shapeView = new ShapeableImageView(getActivity());
        //图片固定大小
        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(160, 160);
        imageParam.gravity = Gravity.CENTER_HORIZONTAL;
        shapeView.setLayoutParams(imageParam);
        ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build();
        shapeView.setShapeAppearanceModel(shape);
        shapeView.setImageDrawable(drawable);
        layout.addView(shapeView);

        //添加下面的文字
        TextView textView = new TextView(getActivity());
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addMenuView(LinearLayout linearLayout, View.OnClickListener clickListener) {
        ImageView imageView = new ImageView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(80, 0, 0, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(params);
        imageView.setBackground(Objects.requireNonNull(getActivity()).
                getDrawable(R.drawable.ic_icon_create_subject));
        imageView.setOnClickListener(clickListener);
        linearLayout.addView(imageView);
    }

}
