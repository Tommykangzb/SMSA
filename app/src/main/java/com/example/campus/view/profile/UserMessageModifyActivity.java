package com.example.campus.view.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.campus.R;
import com.example.campus.view.BaseFragmentActivity;

import java.util.List;

public class UserMessageModifyActivity extends BaseFragmentActivity {
    private static final String TAG = "UserMsgModifyActivity";
    private Fragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_modify_user_message_activity);
        initHomeFragment();
    }

    private void initHomeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = new UserMsgHomeFragment();
            transaction.add(R.id.user_message_modify_frame_layout, homeFragment);
        }
        transaction.show(homeFragment);
        transaction.commit();
    }

    @Override
    public void changeFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务来控制Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentByTag(fragment.getClass().getName()) == null) {
            fragmentTransaction.add(R.id.user_message_modify_frame_layout, fragment, fragment.getClass().getName());
        }
        fragmentTransaction.addToBackStack(null);
        //隐藏Fragment
        hideFragment(fragmentTransaction);
        //显示需要显示的Fragment
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void hideFragment(@NonNull FragmentTransaction transaction) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && !fragment.isHidden()) {
                transaction.hide(fragment);
            }
        }
    }
}
