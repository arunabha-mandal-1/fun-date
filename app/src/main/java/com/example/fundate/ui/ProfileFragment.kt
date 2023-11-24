package com.example.fundate.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.fundate.R
import com.example.fundate.activity.EditProfileActivity
import com.example.fundate.auth.LogInActivity
import com.example.fundate.databinding.FragmentProfileBinding
import com.example.fundate.model.UserModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var database: FirebaseDatabase = Firebase.database
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        dialog.show()

        database.getReference("users")
            .child(auth.currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue(UserModel::class.java)

                    // set fields
                    binding.name.setText(data!!.name.toString())
                    binding.email.setText(data.email.toString())
//                    binding.phone.setText(data!!.number.toString())
                    binding.phone.setText(auth.currentUser!!.phoneNumber.toString())
                    binding.city.setText(data.city.toString())

                    // set image
                    Glide.with(requireContext())
                        .load(data.image)
                        .placeholder(R.drawable.img_date_girl)
                        .into(binding.userImage)

                    dialog.dismiss()
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        binding.logOut.setOnClickListener {
            auth.signOut()
            if(auth.currentUser == null){
                startActivity(Intent(requireContext(), LogInActivity::class.java))
                requireActivity().finish()
            }
        }

        binding.editProfile.setOnClickListener {
            // implement this
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }


        return binding.root
    }

}