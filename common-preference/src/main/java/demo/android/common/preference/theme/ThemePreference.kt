package demo.android.common.preference.theme

import android.content.Context
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatDelegate
import com.google.common.collect.ImmutableList
import demo.android.common.preference.R
import demo.android.common.preference.base.AppPreference
import demo.android.common.preference.data.Option

class ThemePreference : AppPreference(R.string.theme_preference_description) {

    companion object {

        private const val OPTION_ID_LIGHT = 1
        private const val OPTION_ID_DARK = 2
        private const val OPTION_ID_SYSTEM_DEFAULT = 3
        private const val OPTION_ID_BATTERY_SAVER = 4

        private val OPTION_ID_TO_NIGHT_MODE by lazy(LazyThreadSafetyMode.NONE) {
            SparseIntArray().apply {
                append(OPTION_ID_LIGHT, AppCompatDelegate.MODE_NIGHT_NO)
                append(OPTION_ID_DARK, AppCompatDelegate.MODE_NIGHT_YES)
                append(OPTION_ID_SYSTEM_DEFAULT, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                append(OPTION_ID_BATTERY_SAVER, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            }
        }

        private val DEFAULT_OPTION = Option(
            id = OPTION_ID_SYSTEM_DEFAULT,
            icon = R.drawable.ic_theme_default,
            description = R.string.theme_preference_option_system_default
        )

        private val OPTIONS: ImmutableList<Option> = ImmutableList.of(
            Option(
                id = OPTION_ID_LIGHT,
                icon = R.drawable.ic_theme_light,
                description = R.string.theme_preference_option_light
            ),
            Option(
                id = OPTION_ID_DARK,
                icon = R.drawable.ic_theme_dark,
                description = R.string.theme_preference_option_dark
            ),
            Option(
                id = OPTION_ID_BATTERY_SAVER,
                icon = R.drawable.ic_theme_battery_saver,
                description = R.string.theme_preference_option_battery_saver
            ),
            DEFAULT_OPTION
        )

    }

    override fun getOptions(): ImmutableList<Option> {
        return OPTIONS
    }

    override fun getDefaultOption(): Option {
        return DEFAULT_OPTION
    }

    override fun apply(context: Context, selectedOption: Option) {
        AppCompatDelegate.setDefaultNightMode(
            OPTION_ID_TO_NIGHT_MODE[selectedOption.id]
        )
    }

}