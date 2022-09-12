package com.example.campus.view.course;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.campus.R;
import com.example.campus.adaptar.CourseDetailAdapter;
import com.example.campus.helper.RetrofitConfig;
import com.example.campus.protoModel.CourseDetail;
import com.example.campus.retrofit.requestApi.ApiService;
import com.example.campus.view.BaseDialog;
import com.example.campus.view.Constance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tutorial.CourseDetailCreate;
import tutorial.ResponseOuterClass;

public class CommentDialog extends BaseDialog {
    private static final String TAG = "CommentDialog";
    private Bundle bundle;
    private Dialog dialog;
    private View dialogView;
    private EditText evaluateText;
    private EditText frequencyText;
    private EditText attendanceWayText;
    private EditText examWayText;
    private EditText givenText;
    private EditText scoreText;
    private EditText creditText;
    private EditText courseAddName;
    private EditText courseAddTeacher;
    private Activity activity;
    private final String typeKey;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    List<String> dataList = new ArrayList<>();
    private CourseDetailAdapter courseAdapter;

    public CommentDialog(Bundle bundle) {
        this(bundle, Constance.KEY_DIALOG_TYPE_ADD_COMMENT);
    }

    public CommentDialog(Bundle bundle, String type) {
        this.bundle = bundle;
        typeKey = type;
    }


    @SuppressLint("InflateParams")
    @Override
    public void showDialog(Activity activity) {
        this.activity = activity;
        //创建一个dialog实例
        dialog = new Dialog(activity);
        //实例化一个view作为弹窗的内容view
        dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_course_comment_edit_dialog, null);
        //设置弹窗的内容view
        dialog.setContentView(dialogView);
        //设置是否可以在窗口之外点击屏幕让弹窗消失
        dialog.setCanceledOnTouchOutside(true);
        //是否可以被按下back而让弹窗消失
        dialog.setCancelable(true);
        //实例化组件
        if (typeKey.equals(Constance.KEY_DIALOG_TYPE_ADD_COMMENT)) {
            initCommentView();
            dialogView.findViewById(R.id.course_category_add_dialog).setVisibility(View.GONE);
        } else if (typeKey.equals(Constance.KEY_DIALOG_TYPE_ADD_CATEGORY)) {
            initCategoryView();
            dialogView.findViewById(R.id.course_detail_comment).setVisibility(View.GONE);
        }

        Button btnConfirm = dialogView.findViewById(R.id.btn_course_detail_edit_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_course_detail_edit_cancel);
        btnCancel.setOnClickListener(clickListenerCancel);
        btnConfirm.setOnClickListener(clickListenerConfirm);

        //获得弹窗的 window对象
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            //初始化弹窗的位置，在中间
            params.gravity = Gravity.CENTER;
            //弹窗的宽度是整个屏幕的宽度
            params.width = getScreenPixelsWidth(activity);
        }

        //显示弹窗
        dialog.show();
    }

    public void hideDialog() {
        if (dialog == null || bundle == null) {
            return;
        }
        if (bundle.getBoolean(Constance.KEY_CHOSE_FALSE_OR_TRUE)) {
            saveText(true);
        } else {
            clearText();
        }
        dialog.dismiss();
    }

    public void addAdapterItem(List<String> list, int defaultPosition) {
        if (!typeKey.equals(Constance.KEY_DIALOG_TYPE_ADD_CATEGORY)) {
            return;
        }
        if (spinner != null || adapter != null) {
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            spinner.setSelection(defaultPosition);
        }
    }

    private int getScreenPixelsWidth(Context activity) {
        return activity.getResources().getDisplayMetrics().widthPixels;
    }

    private void initCommentView() {
        evaluateText = dialogView.findViewById(R.id.course_edit_evaluate_words);
        frequencyText = dialogView.findViewById(R.id.course_detail_edit_frequency);
        attendanceWayText = dialogView.findViewById(R.id.course_detail_edit_attendance_way);
        examWayText = dialogView.findViewById(R.id.course_detail_edit_exam_way);
        givenText = dialogView.findViewById(R.id.course_detail_edit_exam_given);
        scoreText = dialogView.findViewById(R.id.course_detail_edit_course_score);
        creditText = dialogView.findViewById(R.id.course_detail_edit_credit);
        setLastText();
    }

    private void initCategoryView() {
        courseAddName = dialogView.findViewById(R.id.course_category_add_name);
        courseAddName.setTextSize(14);
        courseAddName.setHint("课程名字");

        courseAddTeacher = dialogView.findViewById(R.id.course_category_add_teacher);
        courseAddTeacher.setTextSize(14);
        courseAddTeacher.setHint("开课讲师");

        spinner = dialogView.findViewById(R.id.course_category_add_type);
        spinner.setGravity(Gravity.CENTER_HORIZONTAL);
        adapter = new ArrayAdapter<>(activity, R.layout.layout_school_selector, android.R.id.text1, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        adapter.addAll(dataList);
    }

    private final View.OnClickListener clickListenerCancel = v -> {
        if (activity == null) {
            return;
        }
        SaveDialog saveDialog = new SaveDialog(bundle, this);
        saveDialog.showDialog(activity);
        Log.e(TAG,"cancel click");
    };

    private final View.OnClickListener clickListenerConfirm = v -> {
        //if (typeKey == Constance.KEY_DIALOG_TYPE_ADD_COMMENT)
        saveText(false);
        Log.e(TAG,"click");
        dialog.dismiss();
    };

    private void setLastText() {
        if (activity == null ) {
            return;
        }
        SharedPreferences spf = activity.getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE);
        if (!spf.getBoolean(Constance.KEY_LOAD_NEXT_TIME, false)) {
            return;
        }
        switch (typeKey){
            case Constance.KEY_DIALOG_TYPE_ADD_COMMENT:
                setLastAddCommentText(spf);
                break;
            case Constance.KEY_DIALOG_TYPE_ADD_CATEGORY:
                setLastAddCourseText(spf);
                break;
            default:
                break;
        }

    }

    private void setLastAddCommentText(SharedPreferences spf){
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_FREQUENCY, ""))) {
            frequencyText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_FREQUENCY, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_EVALUATE, ""))) {
            evaluateText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_EVALUATE, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_ATTENDANCE_WAY, ""))) {
            attendanceWayText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_ATTENDANCE_WAY, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_EXAM_WAY, ""))) {
            examWayText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_EXAM_WAY, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_GIVEN, ""))) {
            givenText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_GIVEN, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_SCORE, ""))) {
            scoreText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_SCORE, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_CREDIT, ""))) {
            creditText.setText(spf.getString(Constance.KEY_COURSE_DETAIL_EDIT_CREDIT, ""));
        }
    }

    private void setLastAddCourseText(SharedPreferences spf) {
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_EDIT_ADD_NAME, ""))) {
            courseAddName.setText(spf.getString(Constance.KEY_COURSE_EDIT_ADD_NAME, ""));
        }
        if (!TextUtils.isEmpty(spf.getString(Constance.KEY_COURSE_EDIT_ADD_TEACHER, ""))) {
            courseAddTeacher.setText(spf.getString(Constance.KEY_COURSE_EDIT_ADD_TEACHER, ""));
        }
        int index = spf.getInt(Constance.KEY_COURSE_SPINNER_LAST_INDEX, -1);
        if (index >= 0 && index < adapter.getCount()) {
            spinner.setSelection(index);
        }
    }

    public void setAdapter(CourseDetailAdapter adapter){
        courseAdapter = adapter;
    }

    private void saveText(boolean loadNextTime) {
        if (activity == null || bundle == null) {
            return;
        }
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE).edit();
        switch (typeKey){
            case Constance.KEY_DIALOG_TYPE_ADD_COMMENT:
                saveCommentText(editor,loadNextTime);
                break;
            case Constance.KEY_DIALOG_TYPE_ADD_CATEGORY:
                saveAddCourseText(editor);
                break;
            default:
                break;
        }
        editor.apply();
        dialog.dismiss();
    }

    private void saveCommentText(SharedPreferences.Editor editor,boolean loadNextTime){
        CourseDetailCreate.CourseDetailCreateRequest.Builder builder = CourseDetailCreate.
                CourseDetailCreateRequest.newBuilder();
        CourseDetail.CourseDetailResponseItem.Builder item = CourseDetail.CourseDetailResponseItem.newBuilder();
        if (!TextUtils.isEmpty(frequencyText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_FREQUENCY, frequencyText.getText().toString());
            builder.setAttendanceFrequency(frequencyText.getText().toString());
            item.setAttendanceFrequency(frequencyText.getText().toString());
        }
        if (!TextUtils.isEmpty(evaluateText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_EVALUATE, evaluateText.getText().toString());
            builder.setCourseEvaluateWords(evaluateText.getText().toString());
            item.setCourseEvaluateWords(evaluateText.getText().toString());
        }
        if (!TextUtils.isEmpty(attendanceWayText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_ATTENDANCE_WAY, attendanceWayText.getText().toString());
            builder.setAttendanceWay(attendanceWayText.getText().toString());
            item.setAttendanceWay(attendanceWayText.getText().toString());
        }
        if (!TextUtils.isEmpty(examWayText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_EXAM_WAY, examWayText.getText().toString());
            builder.setExamWay(examWayText.getText().toString());
            item.setExamWay(examWayText.getText().toString());
        }
        if (!TextUtils.isEmpty(givenText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_GIVEN, givenText.getText().toString());
            builder.setExamGivenGrades(givenText.getText().toString());
            item.setExamGivenGrades(givenText.getText().toString());
        }
        if (!TextUtils.isEmpty(scoreText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_SCORE, scoreText.getText().toString());
            builder.setCourseScore(Integer.parseInt(scoreText.getText().toString()));
            item.setCourseScore(Integer.parseInt(scoreText.getText().toString()));
        }
        if (!TextUtils.isEmpty(creditText.getText())) {
            editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_CREDIT, creditText.getText().toString());
            builder.setCredit(Float.parseFloat(creditText.getText().toString()));
            item.setCredit(Float.parseFloat(creditText.getText().toString()));
        }
        SharedPreferences spf = activity.getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE);
        String account = spf.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "");
        item.setAvatarUrl(spf.getString(Constance.KEY_USER_CENTER_USER_AVATAR_URL,""));
        item.setEvaluatorName(spf.getString(Constance.KEY_USER_CENTER_USER_NAME,""));
        courseAdapter.addList(item.build());

        editor.putBoolean(Constance.KEY_LOAD_NEXT_TIME, loadNextTime);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(activity, R.string.user_center_not_login_text, Toast.LENGTH_SHORT).show();
            return;
        }
        builder.setCourseId(Long.parseLong(bundle.getString(Constance.KEY_COURSE_ID, "0")))
                .setUserId(bundle.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, ""))
                .setLikeCount(0)
                .setDate(simpleDateFormat.format(date));
        ApiService service = RetrofitConfig.getInstance().getService(ApiService.class);
        Call<ResponseOuterClass.Response> call = service.publishReview(builder.build());
        call.enqueue(new Callback<ResponseOuterClass.Response>() {
            @Override
            public void onResponse(@NonNull Call<ResponseOuterClass.Response> call,
                                   @NonNull Response<ResponseOuterClass.Response> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getResponseState() == 1) {
                    Toast.makeText(activity, R.string.course_detail_review_publish_succeed,
                            Toast.LENGTH_LONG).show();
                }
                if (response.body().getResponseState() == -5) {
                    Toast.makeText(activity, R.string.course_detail_review_has_published,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseOuterClass.Response> call,
                                  @NonNull Throwable t) {
                Toast.makeText(activity, R.string.request_net_error,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveAddCourseText(SharedPreferences.Editor editor){
        if (!TextUtils.isEmpty(courseAddName.getText())) {
            editor.putString(Constance.KEY_COURSE_EDIT_ADD_NAME, courseAddName.getText().toString());
        }
        if (!TextUtils.isEmpty(courseAddTeacher.getText())) {
            editor.putString(Constance.KEY_COURSE_EDIT_ADD_TEACHER, courseAddTeacher.getText().toString());
        }
        if (spinner == null || adapter == null || spinner.getSelectedItem() == null) {
            return;
        }
        int index = adapter.getPosition(spinner.getSelectedItem().toString());
        editor.putInt(Constance.KEY_COURSE_SPINNER_LAST_INDEX, index);
    }

    private void clearText() {
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE).edit();
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_FREQUENCY, "");
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_EVALUATE, "");
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_ATTENDANCE_WAY, "");
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_EXAM_WAY, "");
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_GIVEN, "");
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_SCORE, "");
        editor.putString(Constance.KEY_COURSE_DETAIL_EDIT_CREDIT, "");
        editor.putBoolean(Constance.KEY_LOAD_NEXT_TIME, false);
        editor.putString(Constance.KEY_COURSE_EDIT_ADD_TEACHER, "");
        editor.putString(Constance.KEY_COURSE_EDIT_ADD_NAME, "");
        editor.putInt(Constance.KEY_COURSE_SPINNER_LAST_INDEX, -1);
        editor.apply();
    }
}
