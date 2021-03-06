package demo.android.mpchart.formatter

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import demo.android.mpchart.util.toRealTimestamp
import demo.android.common.util.toStringByRes
import demo.android.mpchart.R
import java.text.SimpleDateFormat
import java.util.*

class TimestampValueFormat(
    private val firstValue: Long,
    private val dateFormatStr: String = R.string.full_date_format.toStringByRes()
) : ValueFormatter() {

    private val dateFormat by lazy {
        SimpleDateFormat(dateFormatStr, Locale.CHINA)
    }

    override fun getPointLabel(entry: Entry): String {
        val realTimestamp =
            (entry.y.toLong() + firstValue).toRealTimestamp(entry.x.toInt())
        return dateFormat.format(Date(realTimestamp))
    }

    override fun getFormattedValue(value: Float): String {
        val realValue = (value.toLong() + firstValue) * 1000
        return dateFormat.format(Date(realValue))
    }

}