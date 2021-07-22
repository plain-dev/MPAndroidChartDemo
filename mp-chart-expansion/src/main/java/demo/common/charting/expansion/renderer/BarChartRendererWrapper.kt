package demo.common.charting.expansion.renderer

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import demo.common.charting.expansion.buffer.RangeBuffer
import demo.common.charting.expansion.data.RangeEntry
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

    override fun drawHighlighted(c: Canvas, indices: Array<out Highlight>) {
        val barData = mChart.barData

        for (high in indices) {
            val set = barData.getDataSetByIndex(high.dataSetIndex)
            if (set == null || !set.isHighlightEnabled) continue
            val e = set.getEntryForXValue(high.x, high.y)
            if (!isInBoundsX(e, set)) continue
            val trans = mChart.getTransformer(set.axisDependency)
            mHighlightPaint.color = set.highLightColor
            mHighlightPaint.alpha = set.highLightAlpha
            val isStack = high.stackIndex >= 0 && e.isStacked
            var y1: Float
            var y2: Float
            if (isStack) {
                if (mChart.isHighlightFullBarEnabled) {
                    y1 = e.positiveSum
                    y2 = -e.negativeSum
                } else {
                    val range = e.ranges[high.stackIndex]
                    y1 = range.from
                    y2 = range.to
                }
            } else {
                y1 = e.y
                //y2 = 0f
                // 📌 modify
                y2 = if (e is RangeEntry) e.y2 else 0f
            }
            prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)
            setHighlightDrawPos(high, mBarRect)

            c.drawRect(mBarRect, mHighlightPaint)
        }
    }

}