@file:Suppress("RestrictedApi")

package demo.common.preference.base

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.google.android.material.internal.ContextUtils
import com.google.common.collect.ImmutableList
import demo.common.preference.data.Option

abstract class AppPreference(
    /**
     * The string resources ID of a human readable description of the preference when showing in the
     * preference settings screen.
     */
    @StringRes val description: Int
) {

    companion object {

        private const val SHARED_PREFERENCES_NAME = "app.preferences"
        private const val INVALID_OPTION_ID = 0

    }

    private val id = javaClass.simpleName

    /**
     * Sets the selected option of the preference. The selected option ID will be saved to
     * [SharedPreferences] and the selected option will be applied.
     */
    fun setSelectedOption(context: Context, optionId: Int) {
        if (optionId == getSelectedOptionId(context)) {
            return
        }
        for (option: Option in getOptions()) {
            if (option.id == optionId) {
                getSharedPreferences(context).edit().putInt(id, optionId).apply()
                apply(context, option)
                if (shouldRecreateActivityOnOptionChanged()) {
                    recreateActivityIfPossible(context)
                }
                return
            }
        }
    }

    /**
     * Returns the currently selected option.
     */
    fun getSelectedOption(context: Context): Option {
        val selectedOptionId = getSelectedOptionId(context)
        if (selectedOptionId != INVALID_OPTION_ID) {
            for (option in getOptions()) {
                if (option.id == selectedOptionId) {
                    return option
                }
            }
        }
        val defaultOption = getDefaultOption()
        setSelectedOption(context, defaultOption.id)
        return defaultOption
    }

    /**
     * Applies the currently selected option.
     */
    fun apply(context: Context) {
        apply(context, getSelectedOption(context))
    }

    /**
     * Applies the selected option to take effect on the app.
     */
    protected abstract fun apply(context: Context, selectedOption: Option)

    open fun getSelectedOptionId(context: Context): Int {
        return getSharedPreferences(context).getInt(id, INVALID_OPTION_ID)
    }

    open fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    open fun recreateActivityIfPossible(context: Context) {
        ContextUtils.getActivity(context)?.recreate()
    }

    /**
     * Override this method and return `false` when the preferences settings is not changeable.
     */
    open fun isEnabled(): Boolean {
        return true
    }

    /**
     * Override this method and return `true` if the current activity should be restarted after
     * the selected option is changed.
     */
    protected open fun shouldRecreateActivityOnOptionChanged(): Boolean {
        return false
    }

    /**
     * Returns all available options of the preference.
     */
    abstract fun getOptions(): ImmutableList<Option>

    /**
     * Returns the default option.
     */
    protected abstract fun getDefaultOption(): Option

}