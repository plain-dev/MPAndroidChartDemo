package demo.android.mpchart.util

import com.github.mikephil.charting.utils.Utils

val Float.px
    get() = Utils.convertDpToPixel(this)

val Float.dp
    get() = Utils.convertPixelsToDp(this)