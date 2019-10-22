package com.sqsong.valuecomponent.utils

import android.content.Context
import android.content.res.Resources

object DensityUtils {

    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    fun px2dip(pxValue: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun dip2px(dipValue: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId)
    }

}
