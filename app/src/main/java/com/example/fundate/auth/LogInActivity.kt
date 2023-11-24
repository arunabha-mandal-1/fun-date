package com.example.fundate.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fundate.MainActivity
import com.example.fundate.R
import com.example.fundate.databinding.ActivityLogInBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var dialog: AlertDialog
    val auth = Firebase.auth
    private var verificationID: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        binding.sendOtp.setOnClickListener {
            if (binding.userNumber.text!!.isEmpty()) {
                binding.userNumber.error = "Please enter your number"
            } else {
                sendOtp(binding.userNumber.text.toString())
                binding.sendOtp.isClickable = false
                binding.sendOtp.text = "Sending..."
            }
        }

        binding.verifyOtp.setOnClickListener {
            if (binding.userOtp.text!!.isEmpty()) {
                binding.userOtp.error = "Please enter your OTP"
            } else {
                verifyOtp(binding.userOtp.text.toString())
                binding.verifyOtp.isClickable = false
                binding.verifyOtp.text = "Verifying..."
            }
        }

//        if(auth.currentUser != null){
//            Log.d("Signed", "There is an user!")
//        }else{
//            Log.d("Signed", "There is no user!")
//        }
    }

    private fun sendOtp(number: String) {
        dialog.show()
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @SuppressLint("SetTextI18n")
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                binding.sendOtp.isClickable = true // optional
                binding.sendOtp.text = "Send OTP" // optional
                signInWithPhoneAuthCredential(p0)
            }

            @SuppressLint("SetTextI18n")
            override fun onVerificationFailed(p0: FirebaseException) {
                dialog.dismiss()
                binding.sendOtp.isClickable = true
                binding.sendOtp.text = "Send OTP"
                Toast.makeText(this@LogInActivity, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                dialog.dismiss()
                this@LogInActivity.verificationID = p0
                Toast.makeText(this@LogInActivity, "OTP sent!", Toast.LENGTH_SHORT).show()

                binding.sendOtp.isClickable = true
                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91 $number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    @SuppressLint("SetTextI18n")
    private fun verifyOtp(otp: String) {
        dialog.show()
        binding.verifyOtp.text = "Verifying..."
        val credential = PhoneAuthProvider.getCredential(verificationID!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    @SuppressLint("SetTextI18n")
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                binding.verifyOtp.text = "Verify"
                binding.verifyOtp.isClickable = true
                if (task.isSuccessful) {
                    checkUserExist(binding.userNumber.text.toString())
                } else {
                    dialog.dismiss()
                    Toast.makeText(this@LogInActivity, task.exception!!.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun checkUserExist(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child("+91$number")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dialog.dismiss()
                    if (snapshot.exists()) {
                        startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity((Intent(this@LogInActivity, RegisterActivity::class.java)))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LogInActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}