@file:Suppress("StaticFieldLeak")

package demo.android.mpchart

import android.app.Application
import android.content.Context

class ChartApp : Application() {

    companion object {

        lateinit var context: Context

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}