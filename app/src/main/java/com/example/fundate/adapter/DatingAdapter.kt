package com.example.fundate.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundate.activity.ChatActivity
import com.example.fundate.databinding.ItemUserLayoutBinding
import com.example.fundate.model.UserModel

class DatingAdapter(
    private val context: Context,
    private val list: ArrayList<UserModel>
) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding: ItemUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {
        return DatingViewHolder(
            ItemUserLayoutBinding.inflate(
                LayoutInflater.from(context), parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.name.text = list[position].name.toString()
        holder.binding.email.text = list[position].email.toString()
        Glide.with(context).load(list[position].image).into(holder.binding.userImage)

        holder.binding.chat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", list[position].number)
            context.startActivity(intent)
        }
    }
}