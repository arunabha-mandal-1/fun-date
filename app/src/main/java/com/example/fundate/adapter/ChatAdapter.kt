package com.example.fundate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundate.R
import com.example.fundate.model.ChatModel
import com.example.fundate.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    val context: Context,
    val list: ArrayList<ChatModel>
): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    val MSG_TYPE_RIGHT = 0
    val MSG_TYPE_LEFT = 1

    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val mesageText = itemView.findViewById<TextView>(R.id.chatMessage)
        val userImage = itemView.findViewById<CircleImageView>(R.id.chatImage)
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].senderId == Firebase.auth.currentUser!!.phoneNumber){
            MSG_TYPE_RIGHT
        }else{
            MSG_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){
            ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_receiver_message, parent, false))
        }else{
            ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sender_message, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.mesageText.text = list[position].message.toString()
        Firebase.database.getReference("users")
            .child(list[position].senderId!!).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val data = snapshot.getValue(UserModel::class.java)
                        Glide.with(context)
                            .load(data!!.image)
                            .placeholder(R.drawable.user)
                            .into(holder.userImage)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}