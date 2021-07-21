package demo.android.charting.expansion.charts

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.highlight.BarHighlighter
import com.github.mikephil.charting.renderer.BarChartRenderer
import demo.android.charting.expansion.renderer.BarChartRendererWrapper
import demo.android.charting.expansion.renderer.RoundedBarChartRenderer
import demo.android.charting.expansion.type.Bar
import demo.android.charting.expansion.type.BarBufferType

open class RoundedBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BarChart(context, attrs, defStyle) {

    var isRoundedBar: Boolean = true
        set(value) {
            field = value
            if (mRenderer != null) {
                mRenderer = changeRenderer(value)
                invalidate()
            }
        }

    private fun changeRenderer(rounderBar: Boolean): BarChartRenderer {
        return if (rounderBar) {
            RoundedBarChartRenderer(this, mAnimator, mViewPortHandler, getBarBufferType())
        } else {
            BarChartRendererWrapper(this, mAnimator, mViewPortHandler, getBarBufferType())
        }
    }

    override fun init() {
        super.init()
        mRenderer = changeRenderer(true)
        setHighlighter(BarHighlighter(this))
        xAxis.spaceMin = 0.5f
        xAxis.spaceMax = 0.5f
    }

    protected open fun getBarBufferType(): BarBufferType = Bar

}