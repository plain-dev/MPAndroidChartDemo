package demo.common.preference.base

import android.content.Context
import com.google.common.collect.ImmutableList
import demo.common.preference.theme.ThemePreference
import demo.common.preference.fullscreen.FullscreenPreference

abstract class BaseAppPreferences {

    companion object {

        @JvmStatic
        protected val COMMON_PREFERENCES: ImmutableList<AppPreference> = ImmutableList.of(
            ThemePreference(),
            FullscreenPreference()
        )

    }

    fun applyPreferences(context: Context?) = context?.let {
        for (preference in getPreferences()) {
            preference.apply(it)
        }
    }

    /**
     * Implement this method to return available [AppPreference] list on
     * the targeting Catalog version.
     */
    abstract fun getPreferences(): ImmutableList<AppPreference>

}