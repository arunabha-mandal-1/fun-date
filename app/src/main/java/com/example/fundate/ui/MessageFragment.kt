package com.example.fundate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.fundate.R
import com.example.fundate.adapter.UserMessageAdapter
import com.example.fundate.databinding.FragmentMessageBinding
import com.example.fundate.model.UserModel
import com.example.fundate.ui.DatingFragment.Companion.list
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding
    private lateinit var recyclerViewAdapter: UserMessageAdapter
    private var list1: ArrayList<String> = arrayListOf()
    private var list2: ArrayList<String> = arrayListOf()
    private var dummyList: ArrayList<UserModel> = arrayListOf(UserModel(name = "No users in your chat"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater)

        getData()

        return binding.root
    }

    private fun getData() {
        val currentId = Firebase.auth.currentUser!!.phoneNumber
        Firebase.database.getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list1 = arrayListOf()
                    list2 = arrayListOf()
                    for(data in snapshot.children){
                        if(data.key!!.contains(currentId!!)){
                            list2.add(data.key!!)
                            list1.add(data.key!!.replace(currentId, ""))
                        }
                    }
                    if(isAdded){
                        setRecyclerViewAdapter()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun setRecyclerViewAdapter() {
        // Check if the fragment is still attached before setting the adapter
        if (isAdded) {
            // Initialize and set your RecyclerView adapter
            recyclerViewAdapter = UserMessageAdapter(requireContext(), list1, list2)
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }

}