@file:Suppress("NOTHING_TO_INLINE")

package demo.android.common.util

import android.util.Log

inline fun String.quickDebugLog(tag: String) = Log.d(tag, this)

inline fun String.quickErrorLog(tag: String) = Log.e(tag, this)