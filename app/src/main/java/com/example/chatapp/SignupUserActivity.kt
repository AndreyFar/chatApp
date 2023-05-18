package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * This activity represent signup new user in chat app
 *  - include 3 editTexts for name, email and password, and 1 Button for signup
 *
 */
class SignupUserActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_user)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if(name.isEmpty()) {
                Toast.makeText(this@SignupUserActivity, "Please enter the name", Toast.LENGTH_SHORT).show()
            } else if(email.isEmpty()) {
                Toast.makeText(this@SignupUserActivity, "Please enter the email", Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()) {
                Toast.makeText(this@SignupUserActivity, "Please enter the password", Toast.LENGTH_SHORT).show()
            } else {
                signUp(name, email, password)
            }
        }
    }

    /**
     * represent for signing the new user
     *  - if signing success, then show login activity to log in user
     *  - if signing fails, then display a message to the user
     *
     * @param name users name for signing
     * @param email users email for signing
     * @param password users password for signing
     */
    private fun signUp(name: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // If Sign in success
                    addUserToDatabase(name, email, auth.currentUser?.uid!!)
                    val target = Intent(this@SignupUserActivity, MainActivity::class.java)
                    finish()
                    startActivity(target)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignupUserActivity, "Failed to sign up the user", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * add user to the database
     *
     * @param name users name
     * @param email users email
     * @param uniqueId users id
     */
    private fun addUserToDatabase(name: String, email: String, uniqueId: String) {
        dbRef = FirebaseDatabase.getInstance().getReference()
        dbRef.child("user").child(uniqueId).setValue(User(name, email, uniqueId))

    }


}