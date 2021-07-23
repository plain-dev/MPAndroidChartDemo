package demo.android.mpchart.barchart

import android.graphics.Typeface
import androidx.appcompat.widget.Toolbar
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import demo.android.charting.expansion.charts.RangeChart
import demo.android.charting.expansion.charts.RoundedBarChart
import demo.android.charting.expansion.data.RangeEntry
import demo.android.common.util.px
import demo.android.common.util.toColorByThemeAttr
import demo.android.common.util.toStringByRes
import demo.android.mpchart.R
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.entity.ChartData
import demo.android.mpchart.marker.NormalMarkerView
import demo.android.mpchart.util.ANIMATE_DURATION_MILLIS

class BarChartActivity : BaseActivity(R.layout.activity_bar_chart) {

    private val labelTextColor by lazy {
        R.attr.chartLabelColor.toColorByThemeAttr(this)
    }

    private lateinit var barChart: RoundedBarChart
    private lateinit var rangeChart: RangeChart

    override fun initial() {
        barChart = findViewById(R.id.barChart)
        rangeChart = findViewById(R.id.rangeChart)
        title = R.string.bar_chart_title.toStringByRes()
        setChartBaseStyle(barChart)
        setChartBaseStyle(rangeChart)
        showChart(barChart)
        showChartByRange(rangeChart)
    }

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

    override fun isBack() = true

    private fun setChartBaseStyle(barChart: RoundedBarChart) = barChart.apply {
        // Use default.

        setScaleEnabled(false)

        setNoDataText(R.string.chart_no_data.toStringByRes())
        setNoDataTextColor(R.attr.bodyTextColor.toColorByThemeAttr(this@BarChartActivity))
        setNoDataTextTypeface(Typeface.DEFAULT_BOLD)

        // Boarders
        //setDrawBorders(true)
        //setBorderColor("#333333".toColorInt())

        description.isEnabled = false

        legend.isEnabled = false

        axisRight.isEnabled = false

        axisLeft.apply {
            setDrawGridLines(true)
            setDrawAxisLine(false)
            //gridLineWidth = 1f.px
            gridColor = R.attr.chartGridLineColor.toColorByThemeAttr(this@BarChartActivity)
            textColor = labelTextColor
            xOffset = 5f.px
            //yOffset = 5f.px
        }

        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = labelTextColor
            labelRotationAngle = 45f
            //xOffset = 5f.px
            yOffset = 5f.px
        }

        //isRoundedBar = false
    }

    private fun showChart(barChart: BarChart) {
        val simpleData = getSimpleData()
        val entryList = mutableListOf<BarEntry>()
        val dateList = mutableListOf<String>()
        simpleData.forEachIndexed { i, data ->
            val yValue: Float = data.yVal1.toFloat()
            val xValue: String = data.xVal
            // x -> index y -> yVal1
            entryList.add(BarEntry(i.toFloat(), yValue))
            dateList.add(xValue)
        }

        if (entryList.isNotEmpty() && dateList.isNotEmpty()) {
            // Value Format
            val indexAxisValueFormatter = IndexAxisValueFormatter(dateList)

            //lineChart.axisLeft.valueFormatter
            barChart.xAxis.valueFormatter = indexAxisValueFormatter

            val barDataSet = BarDataSet(entryList, "").apply {
                //valueFormatter
                //highLightColor
                //highlightLineWidth = 1f.px
                setDrawValues(false)
                //barBorderWidth = 10f
                color = R.attr.chartLineSetColorDefault.toColorByThemeAttr(this@BarChartActivity)
                valueTextColor = labelTextColor
            }

            val barData = BarData(barDataSet).apply {
                barWidth = 0.2f
            }
            barChart.data = barData

            val markerView = NormalMarkerView(this)
            barChart.setDrawMarkers(true)
            barChart.marker = markerView

            //barChart.invalidate()
            barChart.animateXY(ANIMATE_DURATION_MILLIS, ANIMATE_DURATION_MILLIS)
        }
    }

    private fun showChartByRange(rangeChart: RangeChart) {
        val simpleData = getSimpleDataByRange()
        val entryList = mutableListOf<BarEntry>()
        val dateList = mutableListOf<String>()
        simpleData.forEachIndexed { i, data ->
            val yValueHigh: Float = data.yVal1.toFloat()
            val yValueLow: Float = data.yVal2.toFloat()
            val xValue: String = data.xVal
            // x -> index y -> yVal1 y2 -> yVal2
            // ⚠
            entryList.add(RangeEntry(i.toFloat(), yValueHigh, yValueLow))
            dateList.add(xValue)
        }

        rangeChart.axisLeft.apply {
            //axisMaximum = 300f
            axisMinimum = -100f
            labelCount = 7
        }

        if (entryList.isNotEmpty() && dateList.isNotEmpty()) {
            // Value Format
            val indexAxisValueFormatter = IndexAxisValueFormatter(dateList)

            //lineChart.axisLeft.valueFormatter
            rangeChart.xAxis.valueFormatter = indexAxisValueFormatter

            val barDataSet = BarDataSet(entryList, "").apply {
                //valueFormatter
                //highLightColor
                //highlightLineWidth = 1f.px
                setDrawValues(false)
                color = R.attr.chartLineSetColorDefault.toColorByThemeAttr(this@BarChartActivity)
                valueTextColor = labelTextColor
            }

            val barData = BarData(barDataSet).apply {
                barWidth = 0.2f
            }
            rangeChart.data = barData

            val markerView = NormalMarkerView(this)
            rangeChart.setDrawMarkers(true)
            rangeChart.marker = markerView

            //rangeChart.invalidate()
            rangeChart.animateXY(ANIMATE_DURATION_MILLIS, ANIMATE_DURATION_MILLIS)
        }
    }

    private fun getSimpleData(): MutableList<ChartData> {
        return mutableListOf<ChartData>().apply {
            add(ChartData(yVal1 = "10", xVal = "06-12"))
            add(ChartData(yVal1 = "13", xVal = "06-13"))
            add(ChartData(yVal1 = "18", xVal = "06-14"))
            add(ChartData(yVal1 = "30", xVal = "06-15"))
            add(ChartData(yVal1 = "60", xVal = "06-16"))
            add(ChartData(yVal1 = "80", xVal = "06-17"))
            add(ChartData(yVal1 = "101", xVal = "06-18"))
            add(ChartData(yVal1 = "202", xVal = "06-19"))
        }
    }

    private fun getSimpleDataByRange(): MutableList<ChartData> {
        return mutableListOf<ChartData>().apply {
            add(ChartData(yVal1 = "10", yVal2 = "-50", xVal = "06-12"))
            add(ChartData(yVal1 = "20", yVal2 = "-80", xVal = "06-13"))
            add(ChartData(yVal1 = "20", yVal2 = "-90", xVal = "06-14"))
            add(ChartData(yVal1 = "30", yVal2 = "-5", xVal = "06-15"))
            add(ChartData(yVal1 = "60", yVal2 = "6", xVal = "06-16"))
            add(ChartData(yVal1 = "80", yVal2 = "20", xVal = "06-17"))
            add(ChartData(yVal1 = "101", yVal2 = "30", xVal = "06-18"))
            add(ChartData(yVal1 = "202", yVal2 = "100", xVal = "06-19"))
        }
    }
}