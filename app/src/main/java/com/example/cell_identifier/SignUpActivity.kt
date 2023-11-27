package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cell_identifier.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val firstName = binding.firstNameEt.text.toString()
            val lastName = binding.lastNameEt.text.toString()
            val bio = binding.bioEt.text.toString()
            val age = binding.ageEt.text.toString().toIntOrNull() ?: 0
            val school = binding.schoolEt.text.toString()
            val role = binding.roleEt.text.toString()
            val year = binding.yearEt.text.toString().toIntOrNull() ?: 0
            val field = binding.fieldEt.text.toString()

            if (pass == confirmPass) {
                // Create a User instance with user input
                val user = User(firstName, lastName, bio, age, school, role, year, field)

                // Attempt to create a new user using Firebase Authentication
                fireBaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // If successful, navigate to the Sign In screen
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If there's an error, display a Toast with the error message
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Display a Toast if password and confirm password do not match
                Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
