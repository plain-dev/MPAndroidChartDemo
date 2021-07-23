package demo.android.mpchart.toc.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import demo.android.mpchart.R

class Toc @JvmOverloads constructor(
    @StringRes var title: Int,
    @DrawableRes var icon: Int = R.drawable.ic_default,
    var targetClass: Class<*>? = null
)