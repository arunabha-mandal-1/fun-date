package com.example.fundate.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fundate.adapter.ChatAdapter
import com.example.fundate.databinding.ActivityChatBinding
import com.example.fundate.model.ChatModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var senderId: String? = null
    private var chatId: String? = null
    val currentTime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    val currentDate = SimpleDateFormat("HH-mm a", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verifyChatId()

        binding.sendMessage.setOnClickListener {
            if (binding.message.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            } else {
                storeData(binding.message.text.toString())
            }
        }
    }

    private fun verifyChatId() {
        val receiverId = intent.getStringExtra("userId")
        senderId = Firebase.auth.currentUser!!.phoneNumber

        // new pk
        chatId = senderId + receiverId
        val reverseChatId = receiverId + senderId

        val ref = Firebase.database.getReference("chats")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(chatId!!)) {
                    getData(chatId)
                } else if (snapshot.hasChild(reverseChatId)) {
                    chatId = reverseChatId
                    getData(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getData(chatId: String?) {
        Firebase.database.getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<ChatModel>()
                    for (show in snapshot.children) {
                        list.add(show.getValue(ChatModel::class.java)!!)
                    }

                    binding.chatRecyclerView.adapter = ChatAdapter(this@ChatActivity, list)
                }
            })
    }

    private fun storeData(msg: String) {
        val map = hashMapOf<String, String>()
        map["message"] = msg
        map["senderId"] = senderId!!
//        map["receiverId"] = receiverId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate
        val ref = Firebase.database.getReference("chats").child(chatId!!)
        ref.child(ref.push().key!!).setValue(map).addOnCompleteListener {
            if (it.isSuccessful) {
                binding.message.text = null
                Toast.makeText(this@ChatActivity, "Message sent!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@ChatActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}