package com.example.campus.view.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.adaptar.CourseDetailAdapter;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.api.IBitmapLoader;
import com.example.campus.protoModel.CourseDetail;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.Constance;
import com.example.campus.view.ImageHelper;
import com.google.android.material.imageview.ShapeableImageView;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetailActivity extends AppCompatActivity implements IBitmapLoader {
    private static final String TAG = "CourseDetailActivity";
    private CourseDetailAdapter courseDetailAdapter;
    private ImageHandler mHandler = new ImageHandler(this);
    private Intent intent;
    private boolean isFirst = false;


    @Override
    public void notifyLoadImage() {
        ShapeableImageView bgView = findViewById(R.id.course_details_bg);
        bgView.setImageBitmap(courseDetailAdapter.avatar);
        courseDetailAdapter.notifyItemChanged(0, 0);
    }

    public static class ImageHandler extends Handler {
        WeakReference<IBitmapLoader> loader;

        public ImageHandler(IBitmapLoader ld) {
            loader = new WeakReference<>(ld);
        }

        @Override
        public void handleMessage(@Nullable Message message) {
            loader.get().notifyLoadImage();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        ScreenHelp.enableTranslucentStatusBar(this);
        setContentView(R.layout.layout_course_details);
        initView();
        loadViewData();
    }


    private void initView(){
        if (intent == null){
            return;
        }
        RecyclerView recyclerView = findViewById(R.id.recycleView_course_details);
        findViewById(R.id.course_detail_btn_back).setOnClickListener(clickListenerBack);
        TextView courseNameInBg = findViewById(R.id.course_detail_name_in_bg);
        Bundle bundle = intent.getExtras();
        courseNameInBg.setText(bundle.getString(Constance.KEY_COURSE_NAME,""));
        TextView courseTeacherInBg = findViewById(R.id.course_detail_teacher_in_bg);
        courseTeacherInBg.setText(bundle.getString(Constance.KEY_COURSE_TEACHER_NAME,""));
        int watchCount = bundle.getInt(Constance.KEY_COURSE_WATCH_COUNT,0);
        ((TextView)findViewById(R.id.course_detail_watch_text)).setText(String.valueOf(watchCount));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        courseDetailAdapter = new CourseDetailAdapter();
        String bgUrl = bundle.getString(Constance.KEY_COURSE_DETAIL_GROUND_URL,"");
        ImageHelper imageHelper = new ImageHelper(courseDetailAdapter,mHandler, RetrofitConfig.avatarHost + bgUrl);
        imageHelper.start();
        recyclerView.setAdapter(courseDetailAdapter);
        TextView addEvaluateWordText = findViewById(R.id.course_detail_hint);
        addEvaluateWordText.setOnClickListener(clickListenerAddEvaluate);
    }

    private void loadViewData(){
        Bundle bundle = intent.getExtras();
        CourseDetail.CourseDetailRequest.Builder builder = CourseDetail.CourseDetailRequest.newBuilder();
        builder.setCourseId(bundle.getLong(Constance.KEY_COURSE_ID, 0))
                .setCourseName(bundle.getString(Constance.KEY_COURSE_NAME, ""))
                .setStartIndex(0)
                .setLimitCount(-1);
        ApiService courseService = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<CourseDetail.CourseDetailResponse> call = courseService.getCourseDetail(builder.build());
        call.enqueue(new Callback<CourseDetail.CourseDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<CourseDetail.CourseDetailResponse> call,
                                   @NonNull Response<CourseDetail.CourseDetailResponse> response) {
                if (response.body() == null){
                    Log.e(TAG,"response is null");
                    return;
                }
                courseDetailAdapter.updateList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<CourseDetail.CourseDetailResponse> call,
                                  @NonNull Throwable t) {
                Toast.makeText(getCurrentFocus().getContext(), R.string.request_net_error,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mHandler = null;
    }

    private final View.OnClickListener clickListenerBack = v -> finish();
    private final View.OnClickListener clickListenerAddEvaluate = v -> {
      CommentDialog commentDialog = new CommentDialog(intent.getExtras());
      commentDialog.showDialog(this);
      commentDialog.setAdapter(courseDetailAdapter);
      //v.setVisibility(View.GONE);
    };
    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.e(TAG,"position: " + position + ", id: " + id);
            if (!isFirst){
                isFirst = true;
                return;
            }
            if (position == 0){
                Intent intent = new Intent(view.getContext(),DetailManagerActivity.class);
                startActivity(intent);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.e(TAG,"onNothingSelected ");
        }
    };

}
