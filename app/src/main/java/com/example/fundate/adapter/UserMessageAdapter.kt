package com.example.fundate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundate.R
import com.example.fundate.activity.ChatActivity
import com.example.fundate.databinding.MessageItemLayoutBinding
import com.example.fundate.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class UserMessageAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val chatKeyList: ArrayList<String>
) : RecyclerView.Adapter<UserMessageAdapter.UserMessageViewHolder>() {
    inner class UserMessageViewHolder(val binding: MessageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMessageViewHolder {
        return UserMessageViewHolder(
            MessageItemLayoutBinding.inflate(
                LayoutInflater.from(context), parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserMessageViewHolder, position: Int) {

        Firebase.database.getReference("users")
            .child(list[position]).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val data = snapshot.getValue(UserModel::class.java)
                        holder.binding.userName.text = data!!.name.toString()
                        Glide.with(context)
                            .load(data.image)
                            .placeholder(R.drawable.user)
                            .into(holder.binding.userImage)
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }

            })

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("chatId", chatKeyList[position])
            intent.putExtra("userId", list[position])
            context.startActivity(intent)
        }
    }
}