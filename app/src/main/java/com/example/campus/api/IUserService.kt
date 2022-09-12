package com.example.campus.api

import android.content.Context

/**
 * Created by kangzhibo on 2022/9/2
 * @author kangzhibo
 */
interface IUserService {
    fun getCurUserId(context: Context): String
    fun getLoginState(): Boolean
}