package demo.android.mpchart.bigdecimal

import android.graphics.Typeface
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.R
import demo.android.mpchart.entity.ChartData
import demo.android.mpchart.formatter.TimestampValueFormat
import demo.android.mpchart.marker.BigDecimalMarkerView
import demo.android.mpchart.util.*
import java.text.SimpleDateFormat
import java.util.*

class BigDecimalChartActivity : BaseActivity(R.layout.activity_big_decimal_chart) {

    private val labelTextColor by lazy {
        R.attr.chartLabelColor.toColorByThemeAttr(this)
    }

    private val dateFormat by lazy {
        SimpleDateFormat(R.string.full_date_format.toStringByRes(), Locale.CHINA)
    }

    private lateinit var lineChart: LineChart

    private lateinit var tvDataInfo: TextView

    private var firstValue: Long = 0L

    override fun initial() {
        lineChart = findViewById(R.id.lineChart)
        tvDataInfo = findViewById(R.id.tvDataInfo)
        title = R.string.big_decimal_chart_title.toStringByRes()
        setChartBaseStyle(lineChart)
        showChart(lineChart)
        showDataInfo(tvDataInfo)
    }

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

    override fun isBack() = true

    private fun setChartBaseStyle(lineChart: LineChart) = lineChart.apply {
        // Use default.

        setScaleEnabled(false)

        setNoDataText(R.string.chart_no_data.toStringByRes())
        setNoDataTextColor(R.attr.bodyTextColor.toColorByThemeAttr(this@BigDecimalChartActivity))
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
            gridColor = R.attr.chartGridLineColor.toColorByThemeAttr(this@BigDecimalChartActivity)
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

    }

    private fun showChart(lineChart: LineChart) {
        val simpleData = getSimpleData()
        val entryList = mutableListOf<Entry>()
        val dateList = mutableListOf<String>()
        simpleData.forEachIndexed { i, data ->
            val yValue: Long = data.yVal1.toLong()
            val xValue: String = data.xVal
            firstValue = simpleData[0].yVal1.toLong()
            val virtualValue = (yValue - firstValue).toFloat()
            entryList.add(Entry(i.toFloat(), virtualValue, firstValue))
            dateList.add(xValue)
        }

        if (entryList.isNotEmpty() && dateList.isNotEmpty()) {
            // Value Format
            val timestampValueFormat1 =
                TimestampValueFormat(firstValue, R.string.time_format.toStringByRes())
            val timestampValueFormat2 =
                TimestampValueFormat(firstValue, R.string.little_date_format.toStringByRes())
            val indexAxisValueFormatter = IndexAxisValueFormatter(dateList)

            lineChart.axisLeft.valueFormatter = timestampValueFormat1
            lineChart.xAxis.valueFormatter = indexAxisValueFormatter

            val lineDataSet = LineDataSet(entryList, "").apply {
                valueFormatter = timestampValueFormat2
                highLightColor =
                    R.attr.chartLineSetHighLightLineColor.toColorByThemeAttr(this@BigDecimalChartActivity)
                //highlightLineWidth = 1f.px
                setDrawValues(false)
                setDrawCircleHole(true)
                setDrawCircles(true)
                circleRadius = 2f.px
                circleHoleRadius = 1f.px
                setCircleColor(R.attr.chartLineSetCircleColor.toColorByThemeAttr(this@BigDecimalChartActivity))
                circleHoleColor =
                    R.attr.chartLineSetCircleHoleColor.toColorByThemeAttr(this@BigDecimalChartActivity)
                color =
                    R.attr.chartLineSetColorDefault.toColorByThemeAttr(this@BigDecimalChartActivity)
                valueTextColor = labelTextColor
                lineWidth = 1.5f.px
                mode = LineDataSet.Mode.LINEAR
            }

            val lineData = LineData(lineDataSet)
            lineChart.data = lineData

            val markerView = BigDecimalMarkerView(this).apply {
                firstValue = this@BigDecimalChartActivity.firstValue
                chartView = lineChart
            }
            lineChart.setDrawMarkers(true)
            lineChart.marker = markerView

            lineChart.invalidate()
        }
    }

    private fun showDataInfo(tvDataInfo: TextView) {
        val str = StringBuilder()
        val simpleData = getSimpleData()
        simpleData.forEachIndexed { index, data ->
            str.append(
                "\uD83D\uDCC5 ${data.xVal} ⏰ ${
                    dateFormat.format(
                        Date(
                            data.yVal1.toLong().toRealTimestamp(index)
                        )
                    )
                }"
            )
            if (index != simpleData.size - 1) {
                str.append("\n")
            }
        }
        tvDataInfo.text = str.toString()
    }

    private fun getSimpleData(): MutableList<ChartData> {
        // 时间戳要求
        // 数值过大去掉后面 000
        // 归到同一天
        return mutableListOf<ChartData>().apply {

            add(ChartData("1623521449", "06-12"))
            add(ChartData("1623520427", "06-13"))
            add(ChartData("1623510435", "06-14"))
            add(ChartData("1623510725", "06-15"))
            add(ChartData("1623509282", "06-16"))
            add(ChartData("1623513979", "06-17"))
            add(ChartData("1623519397", "06-18"))
            add(ChartData("1623500522", "06-19"))

            //add(ChartData("1623509282", "06-12"))
            //add(ChartData("1623513979", "06-13"))
            //add(ChartData("1623519397", "06-14"))
            //add(ChartData("1623500522", "06-15"))
            //add(ChartData("1623510725", "06-16"))

        }
    }

}