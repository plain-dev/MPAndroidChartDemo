package demo.android.common.util

import android.app.Activity
import android.util.DisplayMetrics
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.core.math.MathUtils
import androidx.core.view.WindowInsetsCompat

const val GRID_SPAN_COUNT_MIN = 1
const val GRID_SPAN_COUNT_MAX = 6

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

fun getDeviceWidth() = resource.displayMetrics.widthPixels

fun getDeviceHeight() = resource.displayMetrics.heightPixels

fun calculateGridSpanCount(
    @Px itemSize: Int,
    @IntRange(from = 1) minimumSpanCount: Int = GRID_SPAN_COUNT_MIN,
    maximumSpanCount: Int = GRID_SPAN_COUNT_MAX
) = MathUtils.clamp(
    getDeviceWidth() / itemSize,
    if (minimumSpanCount < 1) 1 else minimumSpanCount,
    maximumSpanCount
)