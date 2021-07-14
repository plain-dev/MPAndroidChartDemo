@file:Suppress("RestrictedApi")

package demo.android.mpchart.util

import android.content.Context
import androidx.annotation.ColorInt
import androidx.appcompat.widget.ThemeUtils

@ColorInt
fun @receiver:androidx.annotation.AttrRes Int.toColorByThemeAttr(context: Context) =
    ThemeUtils.getThemeAttrColor(context, this)