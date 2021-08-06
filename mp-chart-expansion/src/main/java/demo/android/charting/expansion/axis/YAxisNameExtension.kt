package demo.android.charting.expansion.axis

import android.graphics.Color
import android.graphics.Paint
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import demo.android.common.util.quickErrorLog

/*
--------------------------------------------------------------------------

                            🏗 TODO 待办事项

        - [❌] 修改 `MPAndroidChart` 源码实现绘制多个 `Description`
        - [❌] 检查各种情况坐标点

--------------------------------------------------------------------------
 */

private const val DEFAULT_RESERVED_SPACE = 30f

private const val LOG_TAG = "YAxisNameExtension"

private const val DEBUG = false

sealed class Location
object LocationStart : Location()
object LocationCenter : Location()
object LocationEnd : Location()

sealed class Align
object AlignLeft : Align()
object AlignCenter : Align()
object AlignRight : Align()

sealed class VerticalAlign
object VerticalAlignTop : VerticalAlign()
object VerticalAlignCenter : VerticalAlign()
object VerticalAlignBottom : VerticalAlign()

class NameTextStyle(
    @Px val size: Float = 10f,
    @ColorInt val color: Int = Color.parseColor("#FF666666"),
    val align: Align = AlignCenter,
    val verticalAlign: VerticalAlign = VerticalAlignCenter
)

/**
 * 设置 Y 轴名称
 */
@JvmOverloads
fun BarLineChartBase<*>?.setYAxisName(
    yAxis: YAxis?,
    name: String,
    nameLocation: Location = LocationEnd,
    nameTextStyle: NameTextStyle,
    tag: String = ""
) {
    if (this == null || yAxis == null) return

    // 防止遮挡文本
    checkParentClipChart()

    // Set some auxiliary lines and colors for debugging
    setDebuggingAssist()

    // 预留空间
    setReservedSpace(yAxis, nameLocation)

    // 设置描述
    postSetDescription(yAxis, name, nameLocation, nameTextStyle, tag)
}

/**
 * 以 Post 的方式设置描述
 *
 * - 确保获取到正确的位置信息
 */
private fun BarLineChartBase<*>.postSetDescription(
    yAxis: YAxis,
    name: String,
    nameLocation: Location,
    nameTextStyle: NameTextStyle,
    tag: String
) = post {
    val description = Description().apply {
        isEnabled = true
        setNameTextStyleAndLocation(
            chart = this@postSetDescription,
            yAxis = yAxis,
            name = name,
            nameLocation = nameLocation,
            nameTextStyle = nameTextStyle,
            tag = tag
        )
    }
    //addDescription(description) // TODO：修改源码
}

/**
 * 设置预留空间
 */
private fun BarLineChartBase<*>.setReservedSpace(yAxis: YAxis, nameLocation: Location) {
    if (nameLocation == LocationEnd) {
        extraTopOffset = DEFAULT_RESERVED_SPACE
    }
    val axisDependency: YAxis.AxisDependency = yAxis.axisDependency
    if (nameLocation == LocationCenter) {
        when (axisDependency) {
            YAxis.AxisDependency.LEFT -> {
                extraLeftOffset = DEFAULT_RESERVED_SPACE
            }
            YAxis.AxisDependency.RIGHT -> {
                extraRightOffset = DEFAULT_RESERVED_SPACE
            }
        }
    }
}

/**
 * 设置文本内容、样式和位置
 */
private fun Description.setNameTextStyleAndLocation(
    chart: BarLineChartBase<*>,
    yAxis: YAxis,
    name: String,
    nameLocation: Location,
    nameTextStyle: NameTextStyle,
    tag: String = ""
) {

    //<editor-fold desc="📌 设置文本内容、样式">
    text = name
    textColor = nameTextStyle.color
    textSize = nameTextStyle.size
    //</editor-fold>

    //<editor-fold desc="📌 一些坐标信息（计算需要）">

    val chartWidth = chart.width
    val chartHeight = chart.height
    val viewPortHandler: ViewPortHandler = chart.viewPortHandler
    val offsetLeft = viewPortHandler.offsetLeft()
    val offsetTop = viewPortHandler.offsetTop()
    val offsetRight = viewPortHandler.offsetRight()
    val offsetBottom = viewPortHandler.offsetBottom()

    val contentLeft = viewPortHandler.contentLeft()
    val contentTop = viewPortHandler.contentTop()
    val contentRight = viewPortHandler.contentRight()
    val contentBottom = viewPortHandler.contentBottom()
    val descXOffset = xOffset
    val descYOffset = yOffset

    val extraLeftOffset = chart.extraLeftOffset
    val extraRightOffset = chart.extraRightOffset
    //</editor-fold>

    //<editor-fold desc="📌 设置文本对齐方式">
    textAlign = when (nameTextStyle.align) {
        AlignLeft -> Paint.Align.LEFT
        AlignCenter -> Paint.Align.CENTER
        AlignRight -> Paint.Align.RIGHT
    }
    //</editor-fold>

    //<editor-fold desc="📌 设置文本位置">
    // 获取 Y 轴位置，Left or Right
    val axisDependency: YAxis.AxisDependency = yAxis.axisDependency
    when (nameLocation) {
        //<editor-fold desc="📌 计算文本位置：Y 轴上方">
        LocationEnd -> {
            val nameX = getXPos(axisDependency, contentLeft, contentRight)
            val nameY = when (nameTextStyle.verticalAlign) {
                VerticalAlignTop -> {
                    contentTop - descYOffset
                }
                VerticalAlignCenter -> {
                    contentTop / 2 + descYOffset * 2
                }
                VerticalAlignBottom -> {
                    textSize
                }
            }
            nameX to nameY
        }
        //</editor-fold>
        //<editor-fold desc="📌 计算文本位置：Y 轴中间">
        LocationCenter -> {
            // 如果处于居中位置，则旋转-90度绘制
            //degrees = -90f // TODO：修改源码
            val nameX = when (nameTextStyle.verticalAlign) {
                VerticalAlignTop -> {
                    when (axisDependency) {
                        YAxis.AxisDependency.LEFT -> {
                            textSize
                        }
                        YAxis.AxisDependency.RIGHT -> {
                            chartWidth - textSize / 2
                        }
                    }
                }
                VerticalAlignCenter -> {
                    // TODO Check pos
                    when (axisDependency) {
                        YAxis.AxisDependency.LEFT -> {
                            extraLeftOffset
                        }
                        YAxis.AxisDependency.RIGHT -> {
                            chartWidth - extraRightOffset / 2
                        }
                    }
                }
                VerticalAlignBottom -> {
                    // TODO Check pos
                    when (axisDependency) {
                        YAxis.AxisDependency.LEFT -> {
                            extraLeftOffset + textSize
                        }
                        YAxis.AxisDependency.RIGHT -> {
                            chartWidth - extraRightOffset
                        }
                    }
                }
            }
            // TODO 待检查坐标
            val nameY = contentBottom / 2 + textSize // TODO Check maybe: + descYOffset * 2
            nameX to nameY
        }
        //</editor-fold>
        //<editor-fold desc="📌 计算文本位置：Y 轴下方">
        LocationStart -> {
            val nameX = getXPos(axisDependency, contentLeft, contentRight)
            // TODO 待检查坐标
            val nameY = when (nameTextStyle.verticalAlign) {
                VerticalAlignTop -> {
                    contentBottom + offsetBottom - textSize
                }
                VerticalAlignCenter -> {
                    contentBottom + offsetBottom / 2
                }
                VerticalAlignBottom -> {
                    contentBottom + textSize
                }
            }
            nameX to nameY
        }
        //</editor-fold>
    }.apply {
        setPosition(first, second)
    }
    //</editor-fold>


    // Debugging
    ("[Chart Description ($tag)] ==>> " +
            "chartWidth: ${chartWidth}px | " +
            "chartHeight: ${chartHeight}px | " +
            "descXOffset: ${descXOffset}px | " +
            "descYOffset: ${descYOffset}px").quickErrorLog(LOG_TAG)
    ("[Chart Description ($tag)] ==>> " +
            "offsetLeft: ${offsetLeft}px | " +
            "offsetTop: ${offsetTop}px | " +
            "offsetRight: ${offsetRight}px | " +
            "offsetBottom: ${offsetBottom}px").quickErrorLog(LOG_TAG)
}

/**
 * 获取文本 X 位置，适用于 [LocationEnd] 和 [LocationStart]
 */
private fun getXPos(
    axisDependency: YAxis.AxisDependency,
    contentLeft: Float,
    contentRight: Float
) = when (axisDependency) {
    YAxis.AxisDependency.LEFT -> contentLeft
    YAxis.AxisDependency.RIGHT -> contentRight
}

/**
 * 设置 Debug 辅助线、颜色等
 */
private fun BarLineChartBase<*>.setDebuggingAssist(enabled: Boolean = DEBUG) {
    if (enabled) {
        axisLeft.apply {
            setDrawAxisLine(true)
            axisLineColor = Color.parseColor("#8049AFF0")
            axisLineWidth = Utils.convertDpToPixel(2f)
        }
        axisRight.apply {
            setDrawAxisLine(true)
            axisLineColor = Color.parseColor("#8049AFF0")
            axisLineWidth = Utils.convertDpToPixel(2f)
        }
        setBackgroundColor(Color.parseColor("#60C4C9CF"))
        setDrawBorders(true)
        setBorderColor(Color.parseColor("#FF5533"))
    }
}

/**
 * 检查 Parent 裁切 Chart
 */
private fun BarLineChartBase<*>.checkParentClipChart() {
    (parent as? ViewGroup)?.apply {
        clipChildren = false
        clipToPadding = false
    }
}