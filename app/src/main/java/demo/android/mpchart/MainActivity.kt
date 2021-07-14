package demo.android.mpchart

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.button.MaterialButton
import demo.android.mpchart.base.BaseActivity
import demo.android.mpchart.bigdecimal.BigDecimalChartActivity

class MainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MaterialButton>(R.id.btnStartBigDecimalChart).setOnClickListener(this)
    }

    override fun onClick(v: View) = when (v.id) {
        R.id.btnStartBigDecimalChart -> {
            startActivity(Intent(this, BigDecimalChartActivity::class.java))
        }
        else -> {

        }
    }

}