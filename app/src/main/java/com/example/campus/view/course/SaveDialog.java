package com.example.campus.view.course;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.campus.R;
import com.example.campus.view.BaseDialog;
import com.example.campus.view.Constance;
import com.google.android.material.tabs.TabLayout;

public class SaveDialog extends BaseDialog {
    private Dialog dialog;
    private Bundle bundle;
    private BaseDialog parentDialog;
    private String DialogText;
    public SaveCallBack saveCallBack;
    public interface SaveCallBack{
        void choseTrue();
        void choseFalse();
    }
    public SaveDialog(Bundle bundle,BaseDialog dialog){
        this.bundle = bundle;
        parentDialog  = dialog;
    }
    public SaveDialog(Bundle bundle,BaseDialog dialog,String text){
        this.bundle = bundle;
        parentDialog  = dialog;
        DialogText = text;
    }

    @Override
    public void showDialog(Activity activity) {
        //创建一个dialog实例
        dialog = new Dialog(activity);
        //实例化一个view作为弹窗的内容view
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_view_test, null);
//        if (!TextUtils.isEmpty(DialogText)) {
//            TextView textView = dialogView.findViewById(R.id.sava_dialog_text);
//            textView.setText(DialogText);
//        }
        //设置弹窗的内容view
        dialog.setContentView(dialogView);
        //设置是否可以在窗口之外点击屏幕让弹窗消失
        dialog.setCanceledOnTouchOutside(true);
        //是否可以被按下back而让弹窗消失
        dialog.setCancelable(true);
        //("Messages","Activities")
        for (int i = 0; i < 2; i++) {
            TabLayout view = dialogView.findViewById(R.id.layout_view_test_tab);
            view.addTab(view.newTab().setText("Messages"));
        }
        //获得弹窗的 window对象
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            //初始化弹窗的位置，在中间
            params.gravity = Gravity.BOTTOM;
            //弹窗的宽度是整个屏幕的宽度
            params.width = activity.getResources().getDisplayMetrics().widthPixels;
            params.height = activity.getResources().getDisplayMetrics().heightPixels/2;
        }
        dialog.show();
    }

    public void setSaveCallBack(SaveCallBack saveCallBack) {
        this.saveCallBack = saveCallBack;
    }

    private final View.OnClickListener clickListenerCancel = v -> {
        bundle.putBoolean(Constance.KEY_CHOSE_FALSE_OR_TRUE, false);
        if (parentDialog != null) {
            parentDialog.hideDialog();
        }
        dialog.dismiss();
        if (saveCallBack != null) {
            saveCallBack.choseFalse();
        }
    };

    private final View.OnClickListener clickListenerConfirm = v -> {
        bundle.putBoolean(Constance.KEY_CHOSE_FALSE_OR_TRUE, true);
        if (parentDialog != null) {
            parentDialog.hideDialog();
        }
        if (saveCallBack != null) {
            saveCallBack.choseTrue();
        }
        dialog.dismiss();
    };

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }
}
