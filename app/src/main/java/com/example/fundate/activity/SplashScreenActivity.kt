package com.example.fundate.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.fundate.MainActivity
import com.example.fundate.R
import com.example.fundate.auth.LogInActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val user = Firebase.auth.currentUser

        Handler().postDelayed({
            if (user == null){
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 1000)
    }
}