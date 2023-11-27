package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cell_identifier.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.submitButton.setOnClickListener {
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

                // Attempt to create a new user using Firebase Authentication
                fireBaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // If successful, navigate to the Sign In screen
                            auth = FirebaseAuth.getInstance()
                            val uid = auth.currentUser?.uid
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            saveProfile(uid)
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

    private fun saveProfile(uid: String?) {
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

        val user = User(firstName, lastName, email, bio, age, school, role, year, field)
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    //uploadProfilePic()
                } else {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
