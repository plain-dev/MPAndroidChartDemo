package demo.android.mpchart.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import demo.android.mpchart.MainActivity
import demo.android.mpchart.R

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