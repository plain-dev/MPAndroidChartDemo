package demo.common.mpchart.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import demo.common.mpchart.MainActivity
import demo.common.mpchart.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.postOnAnimationDelayed(window.decorView, {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1000)
    }

}