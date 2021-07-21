@file:Suppress("unused")

package demo.common.mpchart.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import demo.common.mpchart.R
import demo.common.preference.dialog.AppPreferencesDialogFragment
import demo.common.preference.window.WindowPreferencesManager

abstract class BaseActivity : AppCompatActivity {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar = getToolbar()
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            if (isBack()) {
                // 先执行 setSupportActionBar
                toolbar.setNavigationIcon(R.drawable.ic_back)
                toolbar.setNavigationOnClickListener {
                    finish()
                }
            }
        }
        WindowPreferencesManager(this).applyEdgeToEdgePreference(window)
        initial()
    }

    private fun showAppPreferenceDialog() {
        AppPreferencesDialogFragment().show(supportFragmentManager, "preferences-screen")
    }

    protected abstract fun initial()

    protected open fun getToolbar(): Toolbar? = null

    protected open fun isBack() = false

}