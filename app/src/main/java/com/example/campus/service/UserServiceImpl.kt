package com.example.campus.service

import android.content.Context
import android.content.SharedPreferences
import com.example.campus.api.IUserService
import com.example.campus.view.Constance

/**
 * Created by kangzhibo on 2022/9/2
 * @author kangzhibo
 */
class UserServiceImpl : IUserService {
    override fun getCurUserId(context: Context): String {
        val spf = context.getSharedPreferences(Constance.USER_DATA, Context.MODE_PRIVATE)
        return spf?.getString(Constance.KEY_USER_CENTER_USER_ACCOUNT, "") ?: ""
    }

    override fun getLoginState(): Boolean {
        return true
    }
}