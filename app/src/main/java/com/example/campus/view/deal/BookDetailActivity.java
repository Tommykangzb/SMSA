package com.example.campus.view.deal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.campus.API.IBitmapLoadAdapter;
import com.example.campus.API.IBitmapLoader;
import com.example.campus.R;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.view.Constance;
import com.example.campus.view.ImageHelper;
import com.example.campus.view.course.CourseDetailActivity;
import com.example.campus.view.message.ChatActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity implements IBitmapLoader, IBitmapLoadAdapter {
    private static final String TAG = "BookDetailActivity";
    //最多放五张照片
    private static final int IMAGE_COUNT = 5;
    private Bundle bundle;
    private LinearLayout contentLayout;
    private LinearLayout desLayout;
    //TextView priceText;
    private LinearLayout reviewText;
    private CourseDetailActivity.ImageHandler mHandler;
    private Bitmap sellerAvatar;
    private final List<ShapeableImageView> imagesList = new ArrayList<>();
    private boolean collectionState;
    private int likeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book_detail);
        mHandler = new CourseDetailActivity.ImageHandler(this);
        bundle = getIntent().getExtras();
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_book_detail_back).setOnClickListener(v -> finish());
        String seller = bundle.getString(Constance.KEY_BOOK_DEAL_SELLER, "");
        TextView nameText = findViewById(R.id.book_deal_detail_seller_name);
        nameText.setText(seller);
        String avatarUrl = bundle.getString(Constance.KEY_BOOK_DEAL_AVATAR_URL, "");
        if (!TextUtils.isEmpty(avatarUrl)) {
            ImageHelper imageHelper = new ImageHelper(this, mHandler, RetrofitConfig.avatarHost + avatarUrl);
            imageHelper.start();
        }
        String sellMsg = bundle.getString(Constance.KEY_BOOK_DEAL_SELL_MSG,"");
        TextView sellMsgView = findViewById(R.id.book_deal_detail_sell_msg);
        sellMsgView.setText(sellMsg);
        contentLayout = findViewById(R.id.book_deal_detail_content);
        float price = bundle.getFloat(Constance.KEY_BOOK_DEAL_SELL_PRICE, 0);
        List<String> imageUrls = bundle.getStringArrayList(Constance.KEY_BOOK_DEAL_IMAGE_MSG);
        addPriceContentView(String.valueOf(price));
        String description = bundle.getString(Constance.KEY_BOOK_DEAL_SELL_DES, "");
        addDesContentView(description);
        if (imageUrls != null){
            addItemImage(imageUrls);
        }
        TextView starText = findViewById(R.id.book_deal_detail_star_text);
        likeCount = bundle.getInt(Constance.KEY_BOOK_DETAIL_LIKE_COUNT,0);
        starText.setText(String.valueOf(likeCount));
        findViewById(R.id.book_deal_detail_private_chat).setOnClickListener(chatBtnClickListener);
        findViewById(R.id.book_deal_detail_star).setOnClickListener(v -> {
            collectionState = !collectionState;
            v.setSelected(collectionState);
            likeCount = collectionState ? likeCount + 1 : likeCount - 1;
            starText.setText(String.valueOf(likeCount));
        });

        addHotReview(contentLayout);
        for (int i = 0; i < 5; i++) {
            addFirstReviews(contentLayout);
        }
    }

    public void addDesContentView(String content) {
        TextView desContent = new TextView(this);
        LinearLayout.LayoutParams desParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        desParams.setMargins(0, 10, 0, 20);
        desContent.setPadding(20, 10, 0, 10);
        desContent.setLayoutParams(desParams);
        desContent.setTextColor(Color.BLACK);
        desContent.setTextSize(14);
        desContent.setText(content);
        contentLayout.addView(desContent);
    }

    public void addPriceContentView(String price) {
        TextView priceText = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        priceParams.setMargins(0, 20, 0, 10);
        priceText.setPadding(20, 10, 0, 10);
        priceText.setLayoutParams(priceParams);
        priceText.setTextColor(Color.RED);
        priceText.setTextSize(28);
        priceText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        priceText.setText("￥" + price);
        contentLayout.addView(priceText);
    }

    private void addHotReview(LinearLayout root){
        TextView tx = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0,40,0,40);
        tx.setLayoutParams(layoutParams);
        tx.setGravity(Gravity.CENTER);
        tx.setText("----- 热门留言 -----");
        tx.setTextSize(22);
        tx.setTextColor(Color.RED);
        tx.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        root.addView(tx);
    }

    public void addFirstReviews(LinearLayout root){
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 20, 40, 20);
        layout.setLayoutParams(layoutParams);
        //头像
        LinearLayout layoutReviewer = new LinearLayout(this);
        LinearLayout.LayoutParams paramsReviewer = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutReviewer.setLayoutParams(paramsReviewer);

        ShapeableImageView avatar = new ShapeableImageView(this);
        LinearLayout.LayoutParams paramsReviewerAvatar = new LinearLayout.LayoutParams(80, 80);
        avatar.setLayoutParams(paramsReviewerAvatar);
        ShapeAppearanceModel shape = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build();
        avatar.setShapeAppearanceModel(shape);
        avatar.setImageResource(R.drawable.avatar);
        layoutReviewer.addView(avatar);
        layout.addView(layoutReviewer);

        //右边内容
        LinearLayout layoutCnt = new LinearLayout(this);
        LinearLayout.LayoutParams paramsReviewerCnt = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsReviewerCnt.setMargins(30,0,0,0);
        layoutCnt.setLayoutParams(paramsReviewerCnt);

        layoutCnt.setOrientation(LinearLayout.VERTICAL);

        //昵称与留言
        LinearLayout reviewer  = new LinearLayout(this);
        LinearLayout.LayoutParams paramsReviewerName = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        reviewer.setLayoutParams(paramsReviewerName);

        TextView txName = new TextView(this);
        LinearLayout.LayoutParams paramsReviewerNameTx = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsReviewerNameTx.gravity = Gravity.CENTER;
        paramsReviewerNameTx.weight = 1;
        txName.setLayoutParams(paramsReviewerNameTx);
        txName.setTextColor(Color.BLACK);
        txName.setTextSize(16);
        txName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        txName.setText("用户123");
        reviewer.addView(txName);

        TextView txCount = new TextView(this);
        LinearLayout.LayoutParams paramsCount = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsCount.gravity = Gravity.CENTER;
        txCount.setLayoutParams(paramsCount);
        txCount.setTextColor(Color.BLACK);
        txCount.setTextSize(14);
        txCount.setText("34");
        reviewer.addView(txCount);

        ImageView like = new ImageView(this);
        LinearLayout.LayoutParams paramsLike = new LinearLayout.LayoutParams(60,60);
        paramsLike.gravity = Gravity.CENTER;
        paramsLike.setMargins(10,0,0,0);
        like.setLayoutParams(paramsLike);
        like.setImageResource(R.drawable.ic_icon_like);
        reviewer.addView(like);

        layoutCnt.addView(reviewer);

        //留言内容
        TextView txContent = new TextView(this);
        LinearLayout.LayoutParams cntParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cntParams.setMargins(0, 20, 0, 0);
        txContent.setLayoutParams(cntParams);
        txContent.setTextColor(Color.BLACK);
        txContent.setTextSize(18);
        txContent.setText("真不错！");
        layoutCnt.addView(txContent);
        //留言内容
        TextView cntTime = new TextView(this);
        txContent.setLayoutParams(cntParams);
        cntTime.setTextSize(14);
        cntTime.setText("2021/2/4");
        layoutCnt.addView(cntTime);

        layout.addView(layoutCnt);
        root.addView(layout);
        addSubReview(layoutCnt);
        addDivideLine(root);
    }

    private void addDivideLine(LinearLayout root){
        View view = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,2);
        layoutParams.setMargins(20,0,20,0);
        view.setLayoutParams(layoutParams);
        view.setBackground(getDrawable(R.color.gray));
        root.addView(view);
    }

    private void addSubReview(LinearLayout root){

    }

    public void addItemImage(List<String> list) {
        for (int i = 0; i < Math.min(IMAGE_COUNT, list.size()); i++) {
            ShapeableImageView imageView = new ShapeableImageView(this);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            imageParams.setMargins(0, 4, 0, 0);
            imageView.setLayoutParams(imageParams);
            imageView.setAdjustViewBounds(true);
            contentLayout.addView(imageView);
            imagesList.add(imageView);
            Glide.with(this)
                    .load(RetrofitConfig.avatarHost + list.get(i))
                    .placeholder(R.drawable.image_unload)
                    .into(imagesList.get(i));
        }
    }

    @Override
    public void notifyLoadImage() {
        ShapeableImageView avatarImage = findViewById(R.id.book_deal_detail_avatar);
        if (sellerAvatar != null) {
            avatarImage.setImageBitmap(sellerAvatar);
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        sellerAvatar = bitmap;
    }

    private final View.OnClickListener chatBtnClickListener = v -> {
        Intent intent = new Intent(this, ChatActivity.class);
        String name = bundle.getString(Constance.KEY_BOOK_DEAL_SELLER, "");
        bundle.putString(Constance.KEY_INTENT_CHAT_NAME, name);
        bundle.putBoolean(Constance.KEY_CHAT_IS_FROM_BOOK_DEAL,true);
        intent.putExtras(new Bundle(bundle));
        startActivity(intent);
    };
}
