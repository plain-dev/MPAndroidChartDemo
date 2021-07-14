@file:Suppress("StaticFieldLeak")

package demo.android.mpchart

import android.app.Application
import android.content.Context
import demo.android.mpchart.preference.AppPreferences

class ChartApp : Application() {

    companion object {

        lateinit var context: Context

        val appPreferences by lazy(LazyThreadSafetyMode.NONE) { AppPreferences() }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        appPreferences.applyPreferences(context)
    }

}