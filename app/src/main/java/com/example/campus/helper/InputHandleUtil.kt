package com.example.campus.helper

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import java.util.regex.Pattern

class InputHandleUtil {
    /**
     * Desc 用于处理全屏模式下输入框被遮挡问题
     * Author kzb
     * Date 2021/3/16
     */

    private var mRootView: ViewGroup? = null
    private var rootViewParams: FrameLayout.LayoutParams? = null
    private var rootViewMargin = 0
    private var lastRootLayoutHeight = 0
    private var scrollViewParams: ViewGroup.MarginLayoutParams? = null
    private var scrollViewMargin = 0
    private var ignoreViewParams: ViewGroup.MarginLayoutParams? = null

    /**
     * 处理包含输入框的容器方法
     * @param rootView 基本View
     * @param scrollView 只处理ScrollView内部view
     * @param ignoreView 忽略scrollView外部的View
     */
    fun handleInputView(
        rootView: ViewGroup,
        scrollView: ScrollView? = null,
        ignoreView: View? = null
    ) {
        mRootView = rootView.getChildAt(0) as ViewGroup
        rootViewParams = mRootView?.layoutParams as FrameLayout.LayoutParams
        rootViewMargin = rootViewParams?.bottomMargin ?: 0

        mRootView?.viewTreeObserver?.addOnGlobalLayoutListener {
            resizeLayout(scrollView, ignoreView)
        }
        lastRootLayoutHeight = getRootLayoutHeight()

        scrollView?.let {
            scrollViewParams = it.layoutParams as ViewGroup.MarginLayoutParams
            scrollViewMargin = scrollViewParams?.bottomMargin ?: 0
        }
        ignoreView?.let {
            ignoreViewParams = it.layoutParams as ViewGroup.MarginLayoutParams
        }
    }


    private fun resizeLayout(scrollView: ScrollView?, ignoreView: View? = null) {
        val currentRootLayoutHeight = getRootLayoutHeight()
        if (currentRootLayoutHeight != lastRootLayoutHeight) {
            val rootLayoutHeight = mRootView?.rootView?.height ?: 0
            val keyBoardHeight = rootLayoutHeight - currentRootLayoutHeight
            if (keyBoardHeight > rootLayoutHeight / 4) {
                if (scrollView != null) {
                    val height = ignoreView?.height ?: 0
                    val topMargin = ignoreViewParams?.topMargin ?: 0
                    val bottomMargin = ignoreViewParams?.bottomMargin ?: 0
                    scrollViewParams?.bottomMargin =
                        keyBoardHeight - height - topMargin - bottomMargin
                    scrollView.requestLayout()
                } else {
                    rootViewParams?.bottomMargin = rootViewMargin + keyBoardHeight
                    mRootView?.requestLayout()
                }
            } else {
                if (scrollView != null) {
                    scrollViewParams?.bottomMargin = scrollViewMargin
                    //重绘scrollView布局
                    scrollView.requestLayout()
                } else {
                    rootViewParams?.bottomMargin = rootViewMargin
                    //重绘xml根布局
                    mRootView?.requestLayout()
                }
            }
            lastRootLayoutHeight = currentRootLayoutHeight
        }
    }

    private fun getRootLayoutHeight(): Int {
        val r = Rect()
        mRootView?.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }

    companion object{
        fun checkAccount(account: String): Boolean {
            if (account.isEmpty() || account.length < 6) {
                return false
            }
            if (!(account[0] in 'a'..'z' || account[0] in 'A'..'Z')) {
                return false
            }
            val remainAccount = account
                .filter { it.isDigit() }
                .filter { it.isLetter() }
                .filter { !it.isLetterOrDigit() }
            return remainAccount.firstOrNull() == null
        }

        fun checkPassword(password: String): Boolean {
            if (password.length < 8)
                return false
            if (password.firstOrNull { it.isDigit() } == null)
                return false
            if (password.firstOrNull { it.isLetter() } == null)
                return false
            return true
        }

        fun checkEmail(email: String): Boolean {
            if (email.isNotEmpty()) {
                return Pattern.matches(
                    "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$",
                    email
                )
            }
            return false
        }
    }

}