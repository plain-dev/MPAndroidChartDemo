package demo.common.preference.data

import android.content.SharedPreferences
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * A preference option that can be selected.
 */
data class Option(
    /**
     * ID of the option. Will be used as the saved value in [SharedPreferences].
     */
    val id: Int,
    /**
     * The drawable resource ID of the icon of the option to show in the preferences screen.
     */
    @field:DrawableRes @param:DrawableRes val icon: Int,
    /**
     * The string resource ID of the human readable description of the option to show in the
     * preferences screen.
     */
    @field:StringRes val description: Int
)