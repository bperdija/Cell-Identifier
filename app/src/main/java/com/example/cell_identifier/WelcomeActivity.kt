package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Handle SignUpButton click
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        val signInButton = findViewById<TextView>(R.id.signIn)
        signInButton.setOnClickListener {
            // Handle SignIn clickable text click
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
    }
}
