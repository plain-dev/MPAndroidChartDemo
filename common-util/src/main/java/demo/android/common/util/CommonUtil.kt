@file:SuppressLint("StaticFieldLeak")

package demo.android.common.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

object CommonUtil {

    lateinit var applicationContext: Context
        private set

    fun initialize(context: Context) {
        applicationContext = context
        ActivityManager.initialize(context as Application)
    }

}