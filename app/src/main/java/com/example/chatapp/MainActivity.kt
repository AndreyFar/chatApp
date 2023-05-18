package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * This activity represent showing list of friends
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_3)))
        supportActionBar?.title = "FRIENDS"

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        //zobrazenie vsetkych zaregistrovanych userov
        dbRef.child("user").addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val curUser = postSnapshot.getValue(User::class.java)

                    //podmienka, kt. zaruci, ze do friend listu neprida, prave prihlaseneho user
                    if(auth.currentUser?.email != curUser?.email){
                        userList.add(curUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    /**
     * represent for showing a menu
     *  - menu consist of one button for log out the user
     *
     * @param menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * represent for logging out the user
     *  - when user is log out, will be show login activity
     *
     * @param item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            auth.signOut()

            val target = Intent(this@MainActivity, LoginUserActivity::class.java)
            finish()
            startActivity(target)

            return true
        }
        return true
    }
}