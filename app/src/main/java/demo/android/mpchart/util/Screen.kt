package demo.android.mpchart.util

import android.app.Activity
import android.util.DisplayMetrics
import androidx.core.view.WindowInsetsCompat

fun getWindowHeight(context: Activity, windowInsets: WindowInsetsCompat? = null): Int {
    val displayMetrics = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(displayMetrics)
    var height = displayMetrics.heightPixels
    if (windowInsets != null) {
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        height += insets.top
        height += insets.bottom
    }
    return height
}