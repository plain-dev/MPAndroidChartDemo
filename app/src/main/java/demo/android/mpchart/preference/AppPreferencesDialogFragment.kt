package demo.android.mpchart.preference

import android.app.Activity
import android.os.Bundle
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import demo.android.mpchart.ChartApp
import demo.android.mpchart.R
import demo.android.mpchart.util.getWindowHeight
import demo.android.mpchart.util.px
import demo.android.mpchart.util.toDrawableByRes
import demo.android.mpchart.util.toStringByRes

class AppPreferencesDialogFragment : BottomSheetDialogFragment() {

    private val preferences by lazy(LazyThreadSafetyMode.NONE) {
        ChartApp.appPreferences
    }

    private val buttonIdToOptionId = SparseIntArray()

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.let { d ->
            setBottomSheetNormal(dialog = d)
        }
        view?.let { v ->
            setBottomSheetHeight(view = v, fullScreen = true)
        }
    }

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

    private fun setBottomSheetNormal(
        dialog: BottomSheetDialog,
        @Px peekHeight: Int = 300f.px.toInt()
    ) {
        dialog.dismissWithAnimation = true
        dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            BottomSheetBehavior.from(
                it
            ).peekHeight = peekHeight
        }
    }

    private fun setBottomSheetHeight(view: View, fullScreen: Boolean) {
        val modalBottomSheetChildView: LinearLayout = view.findViewById(R.id.llDrawer)
        val layoutParams = modalBottomSheetChildView.layoutParams
        val behavior = (dialog as BottomSheetDialog).behavior
        var fitToContents = true
        var halfExpandedRatio = 0.5f
        val windowHeight = getWindowHeight(context as Activity)
        if (layoutParams != null) {
            if (fullScreen) {
                layoutParams.height = windowHeight
                fitToContents = false
                halfExpandedRatio = 0.7f
            } else {
                layoutParams.height = windowHeight * 3 / 5
            }
            modalBottomSheetChildView.layoutParams = layoutParams
            behavior.isFitToContents = fitToContents
            behavior.halfExpandedRatio = halfExpandedRatio
        }
    }

}