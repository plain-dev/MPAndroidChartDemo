package demo.common.charting.expansion.charts

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.highlight.BarHighlighter
import demo.common.charting.expansion.renderer.BarChartRendererWrapper
import demo.common.charting.expansion.renderer.RoundedBarChartRenderer
import demo.common.charting.expansion.type.Bar
import demo.common.charting.expansion.type.BarBufferType

open class RoundedBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BarChart(context, attrs, defStyle) {

    var isRoundedBar: Boolean = false
        set(value) {
            field = value
            if (mRenderer != null) {
                mRenderer = getBarChartRenderer(value)
            }
        }

    private fun getBarChartRenderer(roundedBar: Boolean) = if (roundedBar) {
        // Rounded Rectangle
        RoundedBarChartRenderer(this, mAnimator, mViewPortHandler, getBarBufferType())
    } else {
        // Right-angled rectangle
        BarChartRendererWrapper(this, mAnimator, mViewPortHandler, getBarBufferType())
    }

    override fun init() {
        // default `true`
        isRoundedBar = true
        super.init()
        mRenderer = getBarChartRenderer(isRoundedBar)
        setHighlighter(BarHighlighter(this))
        xAxis.spaceMin = 0.5f
        xAxis.spaceMax = 0.5f
    }

    protected open fun getBarBufferType(): BarBufferType = Bar

}