package demo.android.mpchart

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import demo.android.mpchart.barchart.BarChartActivity
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.bigdecimal.BigDecimalChartActivity

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