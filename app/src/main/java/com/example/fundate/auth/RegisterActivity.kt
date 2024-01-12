package com.example.fundate.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.example.fundate.MainActivity
import com.example.fundate.R
import com.example.fundate.databinding.ActivityLogInBinding
import com.example.fundate.databinding.ActivityRegisterBinding
import com.example.fundate.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var dialog: AlertDialog
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.cvUploadImage.setImageURI(imageUri)
    }
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        auth = Firebase.auth
        database = Firebase.database
        storage = Firebase.storage

        dialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        binding.cvUploadImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.btnContinue.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.userName.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userLocation.text.toString().isEmpty()
            || imageUri == null
        ) {
            Toast.makeText(this@RegisterActivity, "Please enter all details", Toast.LENGTH_SHORT)
                .show()
        } else if (!binding.cbTermsCond.isChecked) {
            Toast.makeText(this@RegisterActivity, "Accept Terms and Conditions", Toast.LENGTH_SHORT)
                .show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()
        val storageRef = storage.getReference("profile")
            .child(auth.currentUser!!.phoneNumber!!)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener {
                        storeData(it)
                    }
                    .addOnFailureListener {
                        dialog.dismiss()
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUri: Uri?) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            val data = UserModel(
                name = binding.userName.text.toString(),
                email = binding.userEmail.text.toString(),
                city = binding.userLocation.text.toString(),
                image = imageUri.toString(),
                number = auth.currentUser?.phoneNumber.toString(),
                fcmToken = token
            )
            database.getReference("users")
                .child(auth.currentUser!!.phoneNumber!!)
                .setValue(data)
                .addOnCompleteListener {
                    dialog.dismiss()
                    if (it.isSuccessful) {
                        editor.apply {
                            putBoolean("isRegistered", true)
                            apply()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration successful!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
        })
    }
}

