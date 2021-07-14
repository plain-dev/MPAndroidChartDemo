package demo.android.mpchart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.button.MaterialButton
import demo.android.mpchart.bigdecimal.BigDecimalChartActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

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