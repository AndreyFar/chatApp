package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * This activity represent login user in chat app
 *  - this activity will show as first when user start this chat app
 *  - include 2 editTexts for email and password, and 2 Buttons for login and signup
 *
 */
class LoginUserActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogIn: Button
    private lateinit var btnSignUp: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_user)
        supportActionBar?.hide()    //schova hornu listu s nazvom aplikacie

        auth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogIn = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val target= Intent(this, SignupUserActivity::class.java)
            startActivity(target)
        }

        btnLogIn.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if(email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this@LoginUserActivity, "Please enter the email and the password", Toast.LENGTH_SHORT).show()
            } else if(email.isEmpty()){
                Toast.makeText(this@LoginUserActivity, "Please enter the email", Toast.LENGTH_SHORT).show()
            } else if(password.isEmpty()) {
                Toast.makeText(this@LoginUserActivity, "Please enter the password", Toast.LENGTH_SHORT).show()
            } else  {
                login(email, password)
            }
        }
    }


    /**
     * represent for logging the user
     *  - if logging success, then show list of friends
     *  - if logging fails, then display a message to the user
     *
     * @param email users email for logging
     * @param password users password for logging
     */
    private fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // If logging success
                    val target = Intent(this@LoginUserActivity, MainActivity::class.java)
                    finish()
                    startActivity(target)
                } else {
                    // If logging fails, display a message to the user
                    Toast.makeText(this@LoginUserActivity, "User does not exist, you have to sign up", Toast.LENGTH_SHORT).show()
                }
            }
    }

}