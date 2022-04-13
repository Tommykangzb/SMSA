package com.example.campus.view.course;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.campus.R;
import com.example.campus.view.BaseDialog;
import com.example.campus.view.Constance;

public class SaveDialog extends BaseDialog {
    private Dialog dialog;
    private Bundle bundle;
    private BaseDialog parentDialog;
    public SaveDialog(Bundle bundle,BaseDialog dialog){
        this.bundle = bundle;
        parentDialog  = dialog;
    }
    public void showDialog(Activity activity){
        //创建一个dialog实例
        dialog = new Dialog(activity);
        //实例化一个view作为弹窗的内容view
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_course_comment_store_dialog, null);
        //设置弹窗的内容view
        dialog.setContentView(dialogView);
        //设置是否可以在窗口之外点击屏幕让弹窗消失
        dialog.setCanceledOnTouchOutside(false);
        //是否可以被按下back而让弹窗消失
        dialog.setCancelable(false);
        //获得弹窗的 window对象
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            //初始化弹窗的位置，在中间
            params.gravity = Gravity.CENTER;
            //弹窗的宽度是整个屏幕的宽度
            params.width = activity.getResources().getDisplayMetrics().widthPixels;
        }
        Button btnConfirm = dialogView.findViewById(R.id.btn_course_detail_store_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_course_detail_store_cancel);
        btnCancel.setOnClickListener(clickListenerCancel);
        btnConfirm.setOnClickListener(clickListenerConfirm);
        dialog.show();
    }

    private final View.OnClickListener clickListenerCancel = v -> {
        bundle.putBoolean(Constance.KEY_SAVE_FALSE_OR_TRUE,false);
        parentDialog.hideDialog();
        dialog.dismiss();
    };

    private final View.OnClickListener clickListenerConfirm = v -> {
        bundle.putBoolean(Constance.KEY_SAVE_FALSE_OR_TRUE,true);
        parentDialog.hideDialog();
        dialog.dismiss();
    };

    @Override
    public void hideDialog() {
        dialog.dismiss();
    }
}
