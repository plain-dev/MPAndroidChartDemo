@file:Suppress("NOTHING_TO_INLINE")

package demo.android.mpchart.util

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import demo.android.mpchart.ChartApp

inline fun @receiver:androidx.annotation.StringRes Int.toStringByRes(): String =
    ChartApp.context.resources.getString(this)

inline fun @receiver:androidx.annotation.ColorRes Int.toColorByRes(): Int =
    ContextCompat.getColor(ChartApp.context, this)

inline fun @receiver:androidx.annotation.DrawableRes Int.toDrawableByRes(): Drawable? =
    ContextCompat.getDrawable(ChartApp.context, this)