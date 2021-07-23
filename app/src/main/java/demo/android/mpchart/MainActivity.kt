package demo.android.mpchart

import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import demo.android.common.util.dp
import demo.android.common.util.getDeviceWidth
import demo.android.common.util.px
import demo.android.mpchart.barchart.BarChartActivity
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.base.itemdecoration.GridSpacingItemDecoration
import demo.android.mpchart.bigdecimal.BigDecimalChartActivity
import demo.android.mpchart.toc.adapter.TocAdapter
import demo.android.mpchart.toc.data.Toc

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {

        private const val GRID_SPAN_COUNT_MIN = 1
        private const val GRID_SPAN_COUNT_MAX = 6

    }

    private lateinit var rvToc: RecyclerView

    private val defaultToc = Toc(title = R.string.unknown)

    private val tocData by lazy(LazyThreadSafetyMode.NONE) {
        mutableListOf<Toc>().apply {
            add(
                Toc(
                    title = R.string.start_big_decimal_chart,
                    icon = R.drawable.ic_line_chart,
                    targetClass = BigDecimalChartActivity::class.java
                )
            )
            add(
                Toc(
                    title = R.string.start_bar_chart,
                    icon = R.drawable.ic_bar_chart,
                    targetClass = BarChartActivity::class.java
                )
            )
            // Placeholder
            (0..10).forEach { _ ->
                add(defaultToc)
            }
        }
    }

    private val tocAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TocAdapter(tocData).apply {
            setOnItemClickListener { _, _, position ->
                tocData[position].targetClass?.let {
                    startActivity(Intent(this@MainActivity, it))
                }
            }
        }
    }

    private val gridSpacingItemDecoration by lazy(LazyThreadSafetyMode.NONE) {
        GridSpacingItemDecoration(
            spanCount = getSpanCount(),
            spacing = 10f.px.toInt(),
            includeEdge = true
        )
    }

    override fun initial() {
        rvToc = findViewById(R.id.rvToc)
        setList(rvToc)
    }

    private fun setList(rvToc: RecyclerView) = with(rvToc) {
        layoutManager = GridLayoutManager(context, getSpanCount())
        adapter = tocAdapter
        addItemDecoration(gridSpacingItemDecoration)
    }

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

    private fun getSpanCount(): Int {
        return calculateGridSpanCount()
    }

    private fun calculateGridSpanCount() = MathUtils.clamp(
        getDeviceWidth() / 150f.px.toInt(),
        GRID_SPAN_COUNT_MIN,
        GRID_SPAN_COUNT_MAX
    )

}