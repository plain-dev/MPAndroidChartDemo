package demo.android.charting.expansion.buffer

import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import demo.android.charting.expansion.data.RangeEntry

class RangeBuffer(
    size: Int, dataSetCount: Int,
    containsStacks: Boolean
) : BarBuffer(size, dataSetCount, containsStacks) {

    override fun feed(data: IBarDataSet) {

        val size = data.entryCount * phaseX
        val barWidthHalf = mBarWidth / 2f

        var i = 0
        while (i < size) {
            val e = data.getEntryForIndex(i) as? RangeEntry
            if (e == null) {
                i++
                continue
            }
            val x = e.x
            var y = e.y
            val y2 = e.y2
            val values = e.yVals
            if (!mContainsStacks || values == null) {
                val left = x - barWidthHalf
                val right = x + barWidthHalf
                var bottom: Float
                var top: Float

                //if (mInverted) {
                //    bottom = if (y >= 0) y else 0f
                //    top = if (y <= 0) y else 0f
                //} else {
                //    top = if (y >= 0) y else 0f
                //    bottom = if (y <= 0) y else 0f
                //}

                /*
                ------------------------------------

                            📌 modify

                ------------------------------------
                 */
                if (mInverted) {
                    top = y
                    bottom = y2
                } else {
                    top = y2
                    bottom = y
                }

                // multiply the height of the rect with the phase
                if (top > 0) top *= phaseY else bottom *= phaseY
                addBar(left, top, right, bottom)
            } else {
                var posY = 0f
                var negY = -e.negativeSum
                var yStart = 0f

                // fill the stack
                for (k in values.indices) {
                    val value = values[k]
                    if (value == 0.0f && (posY == 0.0f || negY == 0.0f)) {
                        // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                        y = value
                        yStart = y
                    } else if (value >= 0.0f) {
                        y = posY
                        yStart = posY + value
                        posY = yStart
                    } else {
                        y = negY
                        yStart = negY + Math.abs(value)
                        negY += Math.abs(value)
                    }
                    val left = x - barWidthHalf
                    val right = x + barWidthHalf
                    var bottom: Float
                    var top: Float
                    if (mInverted) {
                        bottom = if (y >= yStart) y else yStart
                        top = if (y <= yStart) y else yStart
                    } else {
                        top = if (y >= yStart) y else yStart
                        bottom = if (y <= yStart) y else yStart
                    }

                    // multiply the height of the rect with the phase
                    top *= phaseY
                    bottom *= phaseY
                    addBar(left, top, right, bottom)
                }
            }
            i++
        }

        reset()
    }

}