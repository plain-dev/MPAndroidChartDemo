package demo.android.mpchart.preference

import android.os.Bundle
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import demo.android.mpchart.ChartApp
import demo.android.mpchart.R
import demo.android.mpchart.util.toDrawableByRes
import demo.android.mpchart.util.toStringByRes

class AppPreferencesDialogFragment : BottomSheetDialogFragment() {

    private val preferences by lazy(LazyThreadSafetyMode.NONE) {
        ChartApp.appPreferences
    }

    private val buttonIdToOptionId = SparseIntArray()

    override fun onCreateView(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val container = inflater.inflate(
            R.layout.layout_preferences_dialog, viewGroup, false
        ) as LinearLayout
        for (appPreference: AppPreference in preferences.getPreferences()) {
            container.addView(createPreferenceView(layoutInflater, container, appPreference))
        }
        return container
    }

    private fun createPreferenceView(
        layoutInflater: LayoutInflater,
        container: LinearLayout,
        preference: AppPreference
    ): View {
        val view: View = layoutInflater.inflate(
            R.layout.layout_preferences_dialog_preference,
            container,
            false
        )

        val isEnabled: Boolean = preference.isEnabled()

        // description
        view.findViewById<TextView>(R.id.preference_description).also {
            it.isEnabled = isEnabled
            it.text = preference.description.toStringByRes()
        }

        // button toggle group
        view.findViewById<MaterialButtonToggleGroup>(
            R.id.preference_options
        ).also { buttonToggleGroup ->
            val selectedOptionId: Int = preference.getSelectedOption(view.context).id
            for (option: Option in preference.getOptions()) {
                createOptionButton(
                    layoutInflater,
                    buttonToggleGroup,
                    option
                ).also {
                    it.isEnabled = isEnabled
                    // Add first
                    buttonToggleGroup.addView(it)
                    it.isChecked = selectedOptionId == option.id
                }
            }

            buttonToggleGroup.isEnabled = isEnabled
            if (isEnabled) {
                buttonToggleGroup.addOnButtonCheckedListener { group: MaterialButtonToggleGroup, checkedId: Int, isChecked: Boolean ->
                    if (isChecked) {
                        preference.setSelectedOption(
                            group.context,
                            buttonIdToOptionId.get(checkedId)
                        )
                    }
                }
            }
        }

        return view
    }

    private fun createOptionButton(
        layoutInflater: LayoutInflater,
        container: MaterialButtonToggleGroup,
        option: Option
    ) = (layoutInflater.inflate(
        R.layout.layout_preferences_dialog_option_button,
        container,
        false
    ) as MaterialButton).also {
        val buttonId = ViewCompat.generateViewId()
        buttonIdToOptionId.append(buttonId, option.id)
        it.id = buttonId
        it.icon = option.icon.toDrawableByRes()
        //it.text = option.description.toStringByRes()
        //it.iconPadding = 0
        //it.insetBottom = 0
        //it.insetTop = 0
        //it.updateLayoutParams {
        //    width = ViewGroup.LayoutParams.MATCH_PARENT
        //}
    }

}