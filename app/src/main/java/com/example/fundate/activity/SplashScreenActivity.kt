package com.example.fundate.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.fundate.MainActivity
import com.example.fundate.R
import com.example.fundate.auth.LogInActivity
import com.example.fundate.auth.RegisterActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val flag = sharedPref.getBoolean("isRegistered", false)

        val user = Firebase.auth.currentUser

        Handler().postDelayed({
            if (user == null) {
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            } else {
                if (flag) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else {
                    startActivity(Intent(this, RegisterActivity::class.java))
                    finish()
                }
            }
        }, 1000)
    }
}