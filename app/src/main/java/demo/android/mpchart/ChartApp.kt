@file:Suppress("StaticFieldLeak")

package demo.android.mpchart

import android.app.Application
import android.content.Context
import demo.android.common.util.CommonUtil
import demo.android.common.preference.base.AppPreferences

class ChartApp : Application() {

    companion object {

        lateinit var context: Context

        val appPreferences by lazy(LazyThreadSafetyMode.NONE) { AppPreferences() }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        CommonUtil.init(context)
        appPreferences.applyPreferences(context)
    }

}