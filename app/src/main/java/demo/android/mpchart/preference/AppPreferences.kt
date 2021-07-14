package demo.android.mpchart.preference

import com.google.common.collect.ImmutableList

class AppPreferences : BaseAppPreferences() {

    override fun getPreferences(): ImmutableList<AppPreference> {
        return COMMON_PREFERENCES
    }

}