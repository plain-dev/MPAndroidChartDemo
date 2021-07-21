package demo.android.charting.expansion.renderer

import android.graphics.*
import android.util.Log
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import demo.android.charting.expansion.buffer.RangeBuffer
import demo.android.charting.expansion.data.RangeEntry
import demo.android.charting.expansion.type.Bar
import demo.android.charting.expansion.type.BarBufferType
import demo.android.charting.expansion.type.Range
import kotlin.math.ceil

class RoundedBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val barBufferType: BarBufferType = Bar
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarShadowRectBuffer = RectF()

    private val mTempRectF = RectF()

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

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = Utils.convertDpToPixel(dataSet.barBorderWidth)

        val drawBorder = dataSet.barBorderWidth > 0f

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled) {
            mShadowPaint.color = dataSet.barShadowColor
            val barData = mChart.barData
            val barWidth = barData.barWidth
            val barWidthHalf = barWidth / 2.0f
            var x: Float
            var i = 0
            val count = ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt()
                .coerceAtMost(dataSet.entryCount)
            while (i < count) {
                val e = dataSet.getEntryForIndex(i)
                x = e.x
                mBarShadowRectBuffer.left = x - barWidthHalf
                mBarShadowRectBuffer.right = x + barWidthHalf
                trans.rectValueToPixel(mBarShadowRectBuffer)
                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    i++
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()
                c.drawRect(mBarShadowRectBuffer, mShadowPaint)
                i++
            }
        }

        // initialize the buffer
        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)

        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val isSingleColor = dataSet.colors.size == 1

        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }

        var j = 0
        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                continue
            }
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.color = dataSet.getColor(j / 4)
            }
            if (dataSet.gradientColor != null) {
                val gradientColor = dataSet.gradientColor
                mRenderPaint.shader = LinearGradient(
                    buffer.buffer[j],
                    buffer.buffer[j + 3],
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    gradientColor.startColor,
                    gradientColor.endColor,
                    Shader.TileMode.MIRROR
                )
            }
            if (dataSet.gradientColors != null) {
                mRenderPaint.shader = LinearGradient(
                    buffer.buffer[j],
                    buffer.buffer[j + 3],
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    dataSet.getGradientColor(j / 4).startColor,
                    dataSet.getGradientColor(j / 4).endColor,
                    Shader.TileMode.MIRROR
                )
            }
            //c.drawRect(
            //    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
            //    buffer.buffer[j + 3], mRenderPaint
            //)

            /*
            ------------------------------------

                        📌 modify

            ------------------------------------
            */
            mTempRectF[
                    buffer.buffer[j],
                    buffer.buffer[j + 1],
                    buffer.buffer[j + 2]
            ] = buffer.buffer[j + 3]
            Log.d("RectF", "Bar ===>> $mTempRectF")
            val radius = (buffer.buffer[j + 2] - buffer.buffer[j]) / 2
            when (barBufferType) {
                Range -> {
                    c.drawRoundRect(mTempRectF, radius, radius, mRenderPaint)
                }
                Bar -> {
                    val corners = floatArrayOf(
                        radius, radius,   // Top left radius in px
                        radius, radius,   // Top right radius in px
                        0f, 0f,     // Bottom right radius in px
                        0f, 0f      // Bottom left radius in px
                    )
                    val path = Path()
                    path.addRoundRect(mTempRectF, corners, Path.Direction.CW)
                    c.drawPath(path, mRenderPaint)
                }
            }

            // TODO Implement rounded borders
            if (drawBorder) {
                c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mBarBorderPaint
                )
            }
            j += 4
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

            Log.d("RectF", "Highlight ===>> $mBarRect")

            /*
            ------------------------------------

                        📌 modify

            ------------------------------------
            */
            val radius = (mBarRect.right - mBarRect.left) / 2
            when (barBufferType) {
                Range -> {
                    c.drawRoundRect(mBarRect, radius, radius, mHighlightPaint)
                }
                Bar -> {
                    val corners = floatArrayOf(
                        radius, radius,   // Top left radius in px
                        radius, radius,   // Top right radius in px
                        0f, 0f,     // Bottom right radius in px
                        0f, 0f      // Bottom left radius in px
                    )
                    val path = Path()
                    path.addRoundRect(mBarRect, corners, Path.Direction.CW)
                    c.drawPath(path, mHighlightPaint)
                }
            }
        }
    }

}