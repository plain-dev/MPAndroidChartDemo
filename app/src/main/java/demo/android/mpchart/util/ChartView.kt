package demo.android.mpchart.util

import android.graphics.Paint
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarLineChartBase
import kotlin.math.max

fun showYAxisTopLabel(chart: BarLineChartBase<*>?, label: String, color: Int) {
    chart?.apply {
        extraTopOffset = 20f
        post {
            description?.apply {

                isEnabled = true

                (parent as ViewGroup).clipChildren = false

                // Text Style
                text = label
                textSize = 12f
                textAlign = Paint.Align.LEFT
                textColor = color

                // Reserved space

                val x = max(axisLeft.xOffset, xOffset) + textSize

                val y = max(yOffset, extraTopOffset / 2)

                setPosition(x, y)

                invalidate()
            }
        }
    }
}