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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var bio: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase Auth, Database, etc.
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        saveButton = view.findViewById(R.id.saveBtn)
        fName = view.findViewById(R.id.etFirstName)
        lName = view.findViewById(R.id.etLastName)
        bio = view.findViewById(R.id.etBio)

        saveButton.setOnClickListener {
            //saveProfile(uid)
        }

        // Set default image URI
        //imageUri = Uri.parse("android.resource://${requireContext().packageName}/${R.drawable.ic_human}")

        return view
    }

//    private fun saveProfile(uid: String?) {
//        val firstName = fName.text.toString()
//        val lastName = lName.text.toString()
//        val bio = bio.text.toString()
//
//        val user = User(firstName, lastName, bio, age, school, role, year, field)
//        if (uid != null) {
//            databaseReference.child(uid).setValue(user).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    uploadProfilePic()
//                } else {
//                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    private fun uploadProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference("Users/${auth.currentUser?.uid}")
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(context, "Profile successfully updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload the image", Toast.LENGTH_SHORT).show()
        }
    }

}