package com.example.harrypotter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.harrypotter.Database.OverallDatabase

class SplashActivity : AppCompatActivity() {

    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { !isReady }

        val database = OverallDatabase.getDatabase(this)

        database.loginDao().getLoginAll().observe(this@SplashActivity, {
            if (it.size == 0) {
                startActivity(Intent(this, LoginPage::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            isReady = true
            finish()
        })
    }
}