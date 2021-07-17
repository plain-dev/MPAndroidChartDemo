@file:Suppress("unused", "ObsoleteSdkInt")

package demo.android.mpchart.windowpreferences

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import com.google.android.material.color.MaterialColors
import com.google.android.material.color.MaterialColors.isColorLight
import android.graphics.Color
import android.graphics.Color.TRANSPARENT
import android.view.View.*

class WindowPreferencesManager(private val context: Context?) {

    companion object {

        private const val PREFERENCES_NAME = "window_preferences"
        private const val KEY_EDGE_TO_EDGE_ENABLED = "edge_to_edge_enabled"
        private const val EDGE_TO_EDGE_BAR_ALPHA = 128

        @RequiresApi(VERSION_CODES.LOLLIPOP)
        private val EDGE_TO_EDGE_FLAGS: Int =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    }

    fun toggleEdgeToEdgeEnabled() {
        if (context != null) {
            getSharedPreferences(context)
                .edit()
                .putBoolean(KEY_EDGE_TO_EDGE_ENABLED, !isEdgeToEdgeEnabled())
                .apply()
        }
    }

    fun isEdgeToEdgeEnabled(): Boolean {
        if (context != null) {
            return getSharedPreferences(context).getBoolean(KEY_EDGE_TO_EDGE_ENABLED, true)
        }
        return false
    }

    fun applyEdgeToEdgePreference(window: Window) {
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            return
        }
        if (context != null) {
            val edgeToEdgeEnabled = isEdgeToEdgeEnabled()
            val statusBarColor = getStatusBarColor(context, isEdgeToEdgeEnabled())
            val navbarColor = getNavBarColor(context, isEdgeToEdgeEnabled())
            val lightBackground = isColorLight(
                MaterialColors.getColor(context, android.R.attr.colorBackground, Color.BLACK)
            )
            val lightNavbar = isColorLight(navbarColor)
            val showDarkNavbarIcons = lightNavbar || navbarColor == TRANSPARENT && lightBackground
            val decorView: View = window.decorView
            val currentStatusBar = if (VERSION.SDK_INT >= VERSION_CODES.M) {
                decorView.systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                0
            }
            val currentNavBar = if (showDarkNavbarIcons && VERSION.SDK_INT >= VERSION_CODES.O) {
                SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                0
            }
            window.navigationBarColor = navbarColor
            window.statusBarColor = statusBarColor
            val edgeToEdgeFlag = if (edgeToEdgeEnabled) {
                EDGE_TO_EDGE_FLAGS
            } else {
                SYSTEM_UI_FLAG_VISIBLE
            }
            val systemUiVisibility = edgeToEdgeFlag or currentStatusBar or currentNavBar
            decorView.systemUiVisibility = systemUiVisibility
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    private fun getStatusBarColor(context: Context, isEdgeToEdgeEnabled: Boolean): Int {
        if (isEdgeToEdgeEnabled && VERSION.SDK_INT < VERSION_CODES.M) {
            val opaqueStatusBarColor = MaterialColors.getColor(
                context,
                android.R.attr.statusBarColor,
                Color.BLACK
            )
            return ColorUtils.setAlphaComponent(opaqueStatusBarColor, EDGE_TO_EDGE_BAR_ALPHA)
        }
        return if (isEdgeToEdgeEnabled) {
            TRANSPARENT
        } else {
            MaterialColors.getColor(
                context,
                android.R.attr.statusBarColor,
                Color.BLACK
            )
        }
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    private fun getNavBarColor(context: Context, isEdgeToEdgeEnabled: Boolean): Int {
        if (isEdgeToEdgeEnabled && VERSION.SDK_INT < VERSION_CODES.O_MR1) {
            val opaqueNavBarColor = MaterialColors.getColor(
                context,
                android.R.attr.navigationBarColor,
                Color.BLACK
            )
            return ColorUtils.setAlphaComponent(opaqueNavBarColor, EDGE_TO_EDGE_BAR_ALPHA)
        }
        return if (isEdgeToEdgeEnabled) {
            TRANSPARENT
        } else {
            MaterialColors.getColor(
                context,
                android.R.attr.navigationBarColor,
                Color.BLACK
            )
        }
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

}