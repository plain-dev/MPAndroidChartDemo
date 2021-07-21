package demo.common.charting.expansion.renderer

import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import demo.common.charting.expansion.buffer.RangeBuffer
import demo.common.charting.expansion.type.Bar
import demo.common.charting.expansion.type.BarBufferType
import demo.common.charting.expansion.type.Range

class BarChartRendererWrapper(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val barBufferType: BarBufferType = Bar
) : BarChartRenderer(chart, animator, viewPortHandler) {

    override fun initBuffers() {
        val barData = mChart.barData
        mBarBuffers = arrayOfNulls(barData.dataSetCount)
        for (i in mBarBuffers.indices) {
            val set = barData.getDataSetByIndex(i)
            when (barBufferType) {
                Range -> {
                    mBarBuffers[i] = RangeBuffer(
                        set.entryCount * 4 * if (set.isStacked) set.stackSize else 1,
                        barData.dataSetCount,
                        set.isStacked
                    )
                }
                Bar -> {
                    mBarBuffers[i] = BarBuffer(
                        set.entryCount * 4 * if (set.isStacked) set.stackSize else 1,
                        barData.dataSetCount,
                        set.isStacked
                    )
                }
            }
        }
    }

}