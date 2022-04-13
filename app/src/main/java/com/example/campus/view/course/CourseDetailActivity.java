package com.example.campus.view.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campus.R;
import com.example.campus.adaptar.CourseDetailAdapter;
import com.example.campus.helper.ScreenHelp;
import com.example.campus.view.Constance;
import com.example.campus.view.RecyclerViewHelper;
import com.google.android.material.imageview.ShapeableImageView;

import java.lang.ref.WeakReference;

public class CourseDetailActivity extends AppCompatActivity {

    private CourseDetailAdapter courseDetailAdapter;
    private ImageHandler mHandler = new ImageHandler(this);
    Intent intent;

    private static class ImageHandler extends Handler {
        WeakReference<CourseDetailActivity> mActivity;

        public ImageHandler(CourseDetailActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@Nullable Message message) {
            ShapeableImageView bgView = mActivity.get().findViewById(R.id.course_details_bg);
            bgView.setImageBitmap(mActivity.get().courseDetailAdapter.avatar);
            mActivity.get().courseDetailAdapter.notifyItemChanged(0, 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         intent = getIntent();
        ScreenHelp.enableTranslucentStatusBar(this);
        setContentView(R.layout.layout_course_details);
        initView();
    }


    private void initView(){
        if (intent == null){
            return;
        }
        RecyclerView recyclerView = findViewById(R.id.recycleView_course_details);
        ((TextView)findViewById(R.id.course_detail_watch_text)).setText("858");
        findViewById(R.id.course_detail_btn_back).setOnClickListener(clickListenerBack);
        TextView courseNameInBg = findViewById(R.id.course_detail_name_in_bg);
        Bundle bundle = intent.getExtras();
        courseNameInBg.setText(bundle.getString(Constance.KEY_COURSE_NAME,""));
        TextView courseTeacherInBg = findViewById(R.id.course_detail_teacher_in_bg);
        courseTeacherInBg.setText(bundle.getString(Constance.KEY_COURSE_TEACHER_NAME,""));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        courseDetailAdapter = new CourseDetailAdapter();
        RecyclerViewHelper recyclerViewHelper = new RecyclerViewHelper(courseDetailAdapter,mHandler);
        recyclerViewHelper.start();
        recyclerView.setAdapter(courseDetailAdapter);

        TextView addEvaluateWordText = findViewById(R.id.course_detail_hint);
        addEvaluateWordText.setOnClickListener(clickListenerAddEvaluate);
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
      v.setVisibility(View.GONE);
    };

}
