package com.example.chatapp

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * This activity represent chat(messages) with chosen friend
 *
 */
class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var dbRef: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_2)))

        //na hornej liste zobraz meno kamarata
        val name = intent.getStringExtra("name")
        supportActionBar?.title = name

        val receiverUid = intent.getStringExtra("id")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //zobrazenie sprav s kamaratom
        dbRef.child("chats").child(receiverRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for (postSnapshot in snapshot.children) {

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        //pridanie spravy do databazy
        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            val messageObj = Message(message, FirebaseAuth.getInstance().currentUser?.uid)

            if(message.isNotEmpty()) {
                dbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObj).addOnSuccessListener {
                        dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObj)
                    }

                messageBox.setText("")
            }

        }

    }
}