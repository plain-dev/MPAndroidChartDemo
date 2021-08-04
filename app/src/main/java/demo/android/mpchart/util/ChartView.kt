package demo.android.mpchart.util

import android.graphics.Paint
import android.util.Log
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.BarLineChartBase
import demo.android.common.util.px

private const val LOG_TAG = "[Chart Description]"

sealed class DescriptionPosition
object YAxisLeftTop : DescriptionPosition()
object YAxisRightTop : DescriptionPosition()
// TODO Other positions ......

fun showChartLabel(
    chart: BarLineChartBase<*>?,
    label: String,
    color: Int,
    position: DescriptionPosition = YAxisLeftTop,
    debug: Boolean = false
) {
    chart?.apply {
        /*
        ----------------------------------------------------------

            Set some auxiliary lines and colors for debugging

        ----------------------------------------------------------
         */
        if (debug) {
            axisLeft.apply {
                setDrawAxisLine(true)
                axisLineColor = "#8049AFF0".toColorInt()
                axisLineWidth = 2f.px
            }
            axisRight.apply {
                setDrawAxisLine(true)
                axisLineColor = "#8049AFF0".toColorInt()
                axisLineWidth = 2f.px
            }
            setBackgroundColor("#C4C9CF".toColorInt())
            setDrawBorders(true)
            setBorderColor("#FF5533".toColorInt())
        }

        extraTopOffset = 30f
        post {
            description?.apply {

                isEnabled = true

                (parent as ViewGroup).clipChildren = false

                // Text Style
                text = label
                textSize = 12f
                textColor = color

                val chartWidth = width
                val chartHeight = height
                val offsetLeft = viewPortHandler.offsetLeft()
                val offsetTop = viewPortHandler.offsetTop()
                val offsetRight = viewPortHandler.offsetRight()
                val offsetBottom = viewPortHandler.offsetBottom()
                val descXOffset = xOffset
                val descYOffset = yOffset

                when (position) {
                    YAxisLeftTop -> {
                        textAlign = Paint.Align.LEFT
                        val descX = offsetLeft / 2
                        val descY = offsetTop - 10f.px
                        descX to descY
                    }
                    YAxisRightTop -> {
                        textAlign = Paint.Align.RIGHT
                        val descX = width - offsetRight / 2
                        val descY = offsetTop - 10f.px
                        descX to descY
                    }
                }.apply {
                    setPosition(first, second)
                }

                if (debug) {
                    Log.e(
                        LOG_TAG,
                        "chartWidth: ${chartWidth}px | chartHeight: ${chartHeight}px"
                    )
                    Log.e(
                        LOG_TAG,
                        "offsetLeft: ${offsetLeft}px | " +
                                "offsetTop: ${offsetTop}px | " +
                                "offsetRight: ${offsetRight}px | " +
                                "offsetBottom: ${offsetBottom}px"
                    )
                    Log.e(
                        LOG_TAG,
                        "descXOffset: ${descXOffset}px | descYOffset: ${descYOffset}px"
                    )
                }

                invalidate()
            }
        }
    }
}