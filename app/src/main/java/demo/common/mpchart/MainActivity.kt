package demo.common.mpchart

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import demo.common.mpchart.barchart.BarChartActivity
import demo.common.mpchart.base.BaseActivity
import demo.common.mpchart.bigdecimal.BigDecimalChartActivity

class MainActivity : BaseActivity(R.layout.activity_main), View.OnClickListener {

    override fun initial() {
        findViewById<MaterialButton>(R.id.btnStartBigDecimalChart).setOnClickListener(this)
        findViewById<MaterialButton>(R.id.btnStartBarChart).setOnClickListener(this)
    }

    override fun onClick(v: View) = when (v.id) {
        R.id.btnStartBigDecimalChart -> {
            startActivity(Intent(this, BigDecimalChartActivity::class.java))
        }
        R.id.btnStartBarChart -> {
            startActivity(Intent(this, BarChartActivity::class.java))
        }
        else -> {

        }
    }

    override fun getToolbar(): Toolbar = findViewById(R.id.toolbar)

}