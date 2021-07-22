package demo.android.charting.expansion.charts

import android.content.Context
import android.util.AttributeSet
import demo.android.charting.expansion.type.Range

class RangeChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RoundedBarChart(context, attrs, defStyle) {

    override fun getBarBufferType() = Range

}