package demo.android.common.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.collection.ArraySet

object ActivityManager {

    private val activities_ = ArraySet<Activity>()

    val activities: Set<Activity> = activities_

    fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activities_.add(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                activities_.remove(activity)
            }
        })
    }

}