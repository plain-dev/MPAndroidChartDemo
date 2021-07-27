package demo.android.mpchart

import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import demo.android.common.util.calculateGridSpanCount
import demo.android.common.util.px
import demo.android.mpchart.barchart.BarChartActivity
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.base.itemdecoration.GridSpacingItemDecoration
import demo.android.mpchart.bigdecimal.BigDecimalChartActivity
import demo.android.mpchart.toc.adapter.TocAdapter
import demo.android.mpchart.toc.data.Toc

class MainActivity : BaseActivity(R.layout.activity_main) {

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

    override fun initial() {
        rvToc = findViewById(R.id.rvToc)
        setList(rvToc)
        handleWindowInsets()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
            rvToc
        ) { v, insets ->
            // Recommend this method
            val navigationBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(bottom = navigationBarInsets.bottom)
            insets
        }
    }

    private fun setList(rvToc: RecyclerView) = with(rvToc) {
        layoutManager = GridLayoutManager(context, getSpanCount())
        adapter = tocAdapter
        addItemDecoration(
            GridSpacingItemDecoration(
                spanCount = getSpanCount(),
                spacing = 10f.px.toInt(),
                includeEdge = true
            )
        )
    }

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

    private fun getSpanCount() = calculateGridSpanCount(itemSize = 150f.px.toInt())

}