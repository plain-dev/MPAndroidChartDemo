@file:Suppress("unused")

package demo.android.mpchart.base

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import demo.android.mpchart.R
import demo.android.mpchart.preference.AppPreferencesDialogFragment

open class BaseActivity : AppCompatActivity {

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.normal_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_setting) {
            showAppPreferenceDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAppPreferenceDialog() {
        AppPreferencesDialogFragment().show(supportFragmentManager, "preferences-screen")
    }

}