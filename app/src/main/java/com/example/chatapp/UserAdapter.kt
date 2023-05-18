package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * This class represent showing layout for list of friends
 *  - in function onBindViewHolder we choose which person we want to write to
 *
 */
class UserAdapter(val context: Context, val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val curUser = userList[position]

        //ked si vyberame osobu s ktorou chceme chatovat zo zoznamu friends
        holder.textName.text = curUser.name
        holder.itemView.setOnClickListener {
            val target = Intent(context, ChatActivity::class.java)

            target.putExtra("name", curUser.name)
            target.putExtra("id",  curUser.uniqueId)

            context.startActivity(target)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }
}