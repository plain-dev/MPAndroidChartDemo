@file:Suppress("ViewConstructor")

package demo.android.mpchart.marker

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import demo.android.common.util.px
import demo.android.mpchart.util.toRealTimestamp
import demo.android.common.util.toStringByRes
import demo.android.mpchart.R
import java.text.SimpleDateFormat
import java.util.*

class BigDecimalMarkerView(
    context: Context,
    dateFormatStr: String = R.string.full_date_format.toStringByRes()
) : MarkerView(
    context,
    R.layout.layout_big_decimal_marker
) {

    private val dateFormat by lazy {
        SimpleDateFormat(dateFormatStr, Locale.CHINA)
    }

    var firstValue: Long = 0L

    private var tvContent: TextView = findViewById(R.id.tvContent)
    private var cardView: MaterialCardView = findViewById(R.id.cardView)

    init {
        setCardViewStyle(cardView)
    }

    private fun setCardViewStyle(cardView: MaterialCardView) {
        cardView.shapeAppearanceModel = cardView.shapeAppearanceModel.toBuilder().apply {
            setAllCorners(CornerFamily.ROUNDED, 10f.px)
        }.build()
    }

    override fun refreshContent(entry: Entry, highlight: Highlight) {
        val realTimestamp =
            (entry.y.toLong() + firstValue).toRealTimestamp(entry.x.toInt())

        tvContent.text = dateFormat.format(Date(realTimestamp))

        super.refreshContent(entry, highlight)
    }

    override fun getOffset(): MPPointF =
        MPPointF.getInstance(-(width / 2f), height / 3f)

}