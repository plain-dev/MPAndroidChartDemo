@file:SuppressLint("StaticFieldLeak")

package demo.common.common.util

import android.annotation.SuppressLint
import android.content.Context

object CommonUtil {

    lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

}