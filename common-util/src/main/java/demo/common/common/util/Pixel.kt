package demo.common.common.util

import android.content.res.Resources

val Float.px
    get() = convertDpToPixel(this)

val Float.dp
    get() = convertPixelsToDp(this)

val resource: Resources
    get() = CommonUtil.applicationContext.resources

private fun convertDpToPixel(dp: Float) = dp * resource.displayMetrics.density

private fun convertPixelsToDp(px: Float) = px / resource.displayMetrics.density