@file:Suppress("NOTHING_TO_INLINE")

package demo.android.common.util

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

inline fun @receiver:androidx.annotation.StringRes Int.toStringByRes(): String =
    CommonUtil.applicationContext.resources.getString(this)

inline fun @receiver:androidx.annotation.ColorRes Int.toColorByRes(): Int =
    ContextCompat.getColor(CommonUtil.applicationContext, this)

inline fun @receiver:androidx.annotation.DrawableRes Int.toDrawableByRes(): Drawable? =
    ContextCompat.getDrawable(CommonUtil.applicationContext, this)