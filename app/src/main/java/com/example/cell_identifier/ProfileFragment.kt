package com.example.cell_identifier

import android.os.Bundle
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private lateinit var saveButton: Button
    private lateinit var fName: TextView
    private lateinit var lName: TextView

    private lateinit var fullName: TextView
    private lateinit var email: TextView
    private lateinit var bio: TextView
    private lateinit var age: TextView
    private lateinit var school: TextView
    private lateinit var role: TextView
    private lateinit var year: TextView
    private lateinit var field: TextView

    private lateinit var uid: String
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        fullName = view.findViewById(R.id.fullNameEt)
        email = view.findViewById(R.id.emailEt)
        bio = view.findViewById(R.id.bioEt)
        age = view.findViewById(R.id.ageEt)
        school = view.findViewById(R.id.schoolEt)
        role = view.findViewById(R.id.roleEt)
        year = view.findViewById(R.id.yearEt)
        field = view.findViewById(R.id.fieldEt)
        saveButton = view.findViewById(R.id.submit_button)

        // Initialize Firebase Auth, Database, etc.
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()) {
            getUserData()
        }

        saveButton.setOnClickListener {
            //updateProfile(uid)
        }

        // Set default image URI
        //imageUri = Uri.parse("android.resource://${requireContext().packageName}/${R.drawable.ic_human}")

        return view
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(User::class.java)!!
                fullName.setText(user.firstName + " " + user.lastName)
                email.setText(user.email )
                bio.setText(user.bio )
                age.setText(user.age.toString() )
                school.setText(user.school )
                role.setText(user.role )
                year.setText(user.year.toString() )
                field.setText(user.field )

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateProfile(uid: String?) {
        val firstName = fName.text.toString()
        val lastName = lName.text.toString()
        val email = email.text.toString()
        val bio = bio.text.toString()
        val age = age.text.toString().toIntOrNull() ?: 0
        val school = school.text.toString()
        val role = role.text.toString()
        val year = year.text.toString().toIntOrNull() ?: 0
        val field = field.text.toString()


        val user = User(firstName, lastName, email, bio, age, school, role, year, field)
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    uploadProfilePic()
                } else {
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference("Users/${auth.currentUser?.uid}")
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(context, "Profile successfully updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload the image", Toast.LENGTH_SHORT).show()
        }
    }

}