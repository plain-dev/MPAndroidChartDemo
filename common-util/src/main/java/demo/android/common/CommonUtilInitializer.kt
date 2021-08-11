package demo.android.common

import android.content.Context
import androidx.startup.Initializer
import demo.android.common.util.CommonUtil

class CommonUtilInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        CommonUtil.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}