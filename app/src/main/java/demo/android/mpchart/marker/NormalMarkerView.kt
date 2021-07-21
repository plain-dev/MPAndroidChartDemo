package demo.android.mpchart.marker

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import demo.android.charting.expansion.data.RangeEntry
import demo.android.mpchart.R
import demo.android.mpchart.util.px
import java.util.*

class NormalMarkerView(context: Context) : MarkerView(context, R.layout.layout_normal_marker) {

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
        val str = StringBuilder()
        if (entry is RangeEntry) {
            val y = entry.y
            val y2 = entry.y2
            str.append(y)
            str.append("/")
            str.append(y2)
        } else {
            val y = entry.y
            str.append(y)
        }

        tvContent.text = str.toString()

        super.refreshContent(entry, highlight)
    }

    override fun getOffset(): MPPointF =
        MPPointF.getInstance(-(width / 2f), height / 3f)

}