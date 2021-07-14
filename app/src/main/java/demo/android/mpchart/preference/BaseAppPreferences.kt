package demo.android.mpchart.preference

import android.content.Context
import com.google.common.collect.ImmutableList

abstract class BaseAppPreferences {

    companion object {

        @JvmStatic
        protected val COMMON_PREFERENCES: ImmutableList<AppPreference> = ImmutableList.of(
            ThemePreference()
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