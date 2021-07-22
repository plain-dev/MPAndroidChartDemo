@file:Suppress("RestrictedApi")

package demo.android.common.util

import android.content.Context
import androidx.annotation.ColorInt
import androidx.appcompat.widget.ThemeUtils

@ColorInt
fun @receiver:androidx.annotation.AttrRes Int.toColorByThemeAttr(context: Context) =
    ThemeUtils.getThemeAttrColor(context, this)