package demo.android.mpchart.multipleyaxis

import android.graphics.Typeface
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.model.GradientColor
import demo.android.common.util.px
import demo.android.common.util.toColorByThemeAttr
import demo.android.common.util.toStringByRes
import demo.android.mpchart.R
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.entity.ChartData
import demo.android.mpchart.marker.NormalMarkerView
import demo.android.mpchart.util.ANIMATE_DURATION_MILLIS

class MultipleYAxisChartActivity : BaseActivity(R.layout.activity_multiple_y_axis_chart) {

    private val labelTextColor by lazy {
        R.attr.chartLabelColor.toColorByThemeAttr(this)
    }

    private lateinit var combinedChart: CombinedChart

    override fun initial() {
        combinedChart = findViewById(R.id.combinedChart)
        title = R.string.multiple_y_axis_chart_title.toStringByRes()
        setChartBaseStyle(combinedChart)
        showChart(combinedChart)
    }

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

    override fun isBack() = true

    private fun setChartBaseStyle(lineChart: CombinedChart) = lineChart.apply {
        // Use default.

        setScaleEnabled(false)

        setNoDataText(R.string.chart_no_data.toStringByRes())
        setNoDataTextColor(R.attr.bodyTextColor.toColorByThemeAttr(this@MultipleYAxisChartActivity))
        setNoDataTextTypeface(Typeface.DEFAULT_BOLD)

        extraBottomOffset = 10f

        // Boarders
        //setDrawBorders(true)
        //setBorderColor("#333333".toColorInt())

        description.isEnabled = false

        legend.isEnabled = false

        //axisRight.isEnabled = false
        axisRight.apply {
            isEnabled = true
            setDrawGridLines(true)
            setDrawAxisLine(false)
            //gridLineWidth = 1f.px
            gridColor =
                R.attr.chartGridLineColor.toColorByThemeAttr(this@MultipleYAxisChartActivity)
            textColor = labelTextColor
            xOffset = 5f.px
        }

        axisLeft.apply {
            setDrawGridLines(true)
            setDrawAxisLine(false)
            //gridLineWidth = 1f.px
            gridColor =
                R.attr.chartGridLineColor.toColorByThemeAttr(this@MultipleYAxisChartActivity)
            textColor = labelTextColor
            xOffset = 5f.px
        }

        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = labelTextColor
            labelRotationAngle = 45f
            yOffset = 5f.px
        }
    }

    private fun showChart(combinedChart: CombinedChart) {
        val simpleData = getSimpleData()
        val barEntryList = mutableListOf<BarEntry>()
        val lineEntryList = mutableListOf<Entry>()
        val dateList = mutableListOf<String>()
        simpleData.forEachIndexed { i, data ->
            val yValue1 = data.yVal1.toFloat() // Bar Y Axis
            val yValue2 = data.yVal2.toFloat() // Line Y Axis
            val xValue: String = data.xVal // XAxis
            barEntryList.add(BarEntry(i.toFloat(), yValue1))
            lineEntryList.add(Entry(i.toFloat(), yValue2))
            dateList.add(xValue)
        }

        if (barEntryList.isNotEmpty() && lineEntryList.isNotEmpty() && dateList.isNotEmpty()) {

            combinedChart.axisLeft.let {
                it.axisMinimum = 0f
                it.granularity = 1f
                it.labelCount = 5
            }

            combinedChart.axisRight.let {
                it.axisMinimum = 0f
                it.granularity = 1f
                it.labelCount = 5
            }

            combinedChart.axisLeft.axisLabel = AxisLabel(
                isEnabled = true,
                location = LocationEnd,
                name = "次/分钟",
                align = AlignLeft,
                verticalAlign = VerticalAlignCenter
            )

            combinedChart.axisRight.axisLabel = AxisLabel(
                isEnabled = true,
                location = LocationCenter,
                name = "次/小时",
                align = AlignRight,
                verticalAlign = VerticalAlignCenter
            )

            // Value Format
            val indexAxisValueFormatter = IndexAxisValueFormatter(dateList)
            combinedChart.xAxis.valueFormatter = indexAxisValueFormatter

            val barDataSet = BarDataSet(barEntryList, "").apply {
                setDrawValues(false)
                setGradientColor("#0EAEFF".toColorInt(), "#32E6FF".toColorInt())
                valueTextColor = labelTextColor

                // 💭 `BarChart` depends on the left y axis
                axisDependency = YAxis.AxisDependency.LEFT
            }

            val lineDataSet = LineDataSet(lineEntryList, "").apply {
                highLightColor =
                    R.attr.chartLineSetHighLightLineColor.toColorByThemeAttr(this@MultipleYAxisChartActivity)
                //highlightLineWidth = 1f.px
                setDrawValues(false)
                setDrawCircleHole(true)
                setDrawCircles(true)
                circleRadius = 2f.px
                circleHoleRadius = 1f.px
                setCircleColor(R.attr.chartLineSetCircleColor.toColorByThemeAttr(this@MultipleYAxisChartActivity))
                circleHoleColor =
                    R.attr.chartLineSetCircleHoleColor.toColorByThemeAttr(this@MultipleYAxisChartActivity)
                color =
                    R.attr.chartLineSetColorDefault.toColorByThemeAttr(this@MultipleYAxisChartActivity)
                valueTextColor = labelTextColor
                lineWidth = 1.5f.px
                mode = LineDataSet.Mode.LINEAR

                // 💭 `LineChart` depends on the right y axis
                axisDependency = YAxis.AxisDependency.RIGHT
            }

            val barData = BarData(barDataSet).apply {
                barWidth = 0.2f
                combinedChart.xAxis.let {
                    // ⚠ Prevent occlusion on the left and right sides
                    it.axisMinimum = -0.5f
                    it.axisMaximum = xMax + 0.5f
                }
            }
            val lineData = LineData(lineDataSet)

            val combinedData = CombinedData().apply {
                setData(barData)
                setData(lineData)
            }

            combinedChart.data = combinedData

            val markerView = NormalMarkerView(this)
            combinedChart.setDrawMarkers(true)
            combinedChart.marker = markerView

            combinedChart.animateXY(ANIMATE_DURATION_MILLIS, ANIMATE_DURATION_MILLIS)
        }
    }

    private fun getSimpleData(): MutableList<ChartData> {
        return mutableListOf<ChartData>().apply {
            add(ChartData(yVal1 = "20", yVal2 = "2", xVal = "06-05"))
            add(ChartData(yVal1 = "30", yVal2 = "5", xVal = "06-06"))
            add(ChartData(yVal1 = "100", yVal2 = "6", xVal = "06-07"))
            add(ChartData(yVal1 = "80", yVal2 = "10", xVal = "06-08"))
            add(ChartData(yVal1 = "101", yVal2 = "20", xVal = "06-09"))
            add(ChartData(yVal1 = "202", yVal2 = "30", xVal = "06-10"))
        }
    }

}