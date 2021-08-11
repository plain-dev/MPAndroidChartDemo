@file:Suppress("StaticFieldLeak")

package demo.android.mpchart

import android.app.Application
import android.content.Context
import demo.android.common.util.CommonUtil
import demo.android.common.preference.base.AppPreferences
import demo.android.common.util.ActivityManager

class ChartApp : Application() {

    companion object {

        val appPreferences by lazy(LazyThreadSafetyMode.NONE) { AppPreferences() }

    }

    override fun onCreate() {
        super.onCreate()
        appPreferences.applyPreferences(this)
    }

}