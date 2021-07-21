package demo.common.preference.fullscreen

import android.content.Context
import android.util.SparseBooleanArray
import com.google.common.collect.ImmutableList
import demo.common.preference.base.AppPreference
import demo.common.preference.data.Option
import demo.common.preference.R
import demo.common.preference.window.WindowPreferencesManager

class FullscreenPreference : AppPreference(R.string.fullscreen_preference_description) {

    companion object {

        private const val OPTION_ID_CLOSE = 1
        private const val OPTION_ID_OPEN = 2

        private val OPTION_ID_TO_FULLSCREEN_MODE by lazy(LazyThreadSafetyMode.NONE) {
            SparseBooleanArray().apply {
                append(OPTION_ID_CLOSE, false)
                append(OPTION_ID_OPEN, true)
            }
        }

        private val DEFAULT_OPTION = Option(
            id = OPTION_ID_CLOSE,
            icon = R.drawable.ic_close_fullscreen,
            description = R.string.fullscreen_preference_option_close
        )

        private val OPTIONS: ImmutableList<Option> = ImmutableList.of(
            DEFAULT_OPTION,
            Option(
                id = OPTION_ID_OPEN,
                icon = R.drawable.ic_open_fullscreen,
                description = R.string.fullscreen_preference_option_open
            )
        )
    }

    override fun apply(context: Context, selectedOption: Option) {
        val mode = OPTION_ID_TO_FULLSCREEN_MODE[selectedOption.id]
        WindowPreferencesManager.toggleEdgeToEdgeEnabled(context, mode)
    }

    override fun getOptions(): ImmutableList<Option> {
        return OPTIONS
    }

    override fun getDefaultOption(): Option {
        return DEFAULT_OPTION
    }

    override fun shouldRecreateActivityOnOptionChanged() = true

}