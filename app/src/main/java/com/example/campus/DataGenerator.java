package com.example.campus;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campus.view.deal.BookDealFragment;
import com.example.campus.view.message.MessageFragment;
import com.example.campus.view.course.HomeFragment;
import com.example.campus.view.profile.UserCenterFragment;


/**
 * Created by zhouwei on 17/4/23.
 */

public class DataGenerator {

    public static final int []mTabRes = new int[]{R.drawable.tab_home_selector,R.drawable.tab_deal_selector,R.drawable.tab_chat_selector,R.drawable.tab_profile_selector};
    public static final int []mTabResPressed = new int[]{R.drawable.ic_icon_tab_sub_on,R.drawable.ic_icon_tab_deal_on,R.drawable.ic_icon_tab_chat_on,R.drawable.ic_icon_tab_profile_on};
    public static final String []mTabTitle = new String[]{"选课","市场","消息","我的"};

    public static Fragment[] getFragments(String from){
        Fragment[] fragments = new Fragment[4];
        fragments[0] = HomeFragment.newInstance(from);
        fragments[1] = BookDealFragment.newInstance();
        fragments[2] = MessageFragment.newInstance();
        fragments[3] = new UserCenterFragment();
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context,int position){
        View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}