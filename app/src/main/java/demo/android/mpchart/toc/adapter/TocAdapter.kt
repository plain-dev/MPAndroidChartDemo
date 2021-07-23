package demo.android.mpchart.toc.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import demo.android.mpchart.R
import demo.android.mpchart.toc.data.Toc

class TocAdapter(tocData: MutableList<Toc>) : BaseQuickAdapter<Toc, BaseViewHolder>(
    layoutResId = R.layout.layout_item_toc,
    data = tocData
) {

    override fun convert(holder: BaseViewHolder, item: Toc) {
        holder.getView<ImageView>(R.id.ivIcon).setImageResource(item.icon)
        holder.getView<TextView>(R.id.tvTitle).setText(item.title)
    }

}