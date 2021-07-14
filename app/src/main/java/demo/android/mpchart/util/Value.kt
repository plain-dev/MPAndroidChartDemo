@file:Suppress("NOTHING_TO_INLINE")

package demo.android.mpchart.util

/**
 * 还原真实时间戳
 *
 * - [this] 原始时间戳
 * - [index] 时间戳对应 X 轴下标，加上相应天数
 */
inline fun Long.toRealTimestamp(index: Int) =
    this * 1000 + index * 24 * 60 * 60 * 1000
