package com.example.cell_identifier

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import android.Manifest
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button

    //private lateinit var fName: TextView
    //private lateinit var lName: TextView

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

    private lateinit var profileImageView: ImageView
    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            // Convert bitmap to Uri and store it in imageUri
            val tempUri = getImageUriFromBitmap(requireContext(), bitmap)
            tempUri?.let {
                imageUri = it
                profileImageView.setImageBitmap(bitmap)
                // You can also upload the image to Firebase here if desired
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(context, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        storageReference = FirebaseStorage.getInstance().reference

        fullName = view.findViewById(R.id.fullNameEt)
        email = view.findViewById(R.id.emailEt)
        bio = view.findViewById(R.id.bioEt)
        age = view.findViewById(R.id.ageEt)
        school = view.findViewById(R.id.schoolEt)
        role = view.findViewById(R.id.roleEt)
        year = view.findViewById(R.id.yearEt)
        field = view.findViewById(R.id.fieldEt)
        saveButton = view.findViewById(R.id.submit_button)
        logoutButton = view.findViewById(R.id.logout_button)

        // Initialize Firebase Auth, Database, etc.
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()) {
            getUserData()
        }



        profileImageView = view.findViewById(R.id.profile_image_view)
        profileImageView.setOnClickListener {
            checkCameraPermission()
        }

        saveButton.setOnClickListener {
            updateProfile(uid)
        }

        logoutButton.setOnClickListener {
            // logout
            FirebaseAuth.getInstance().signOut()
            val logoutIntent = Intent(context, WelcomeActivity::class.java)
            startActivity(logoutIntent)
        }


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

                if (user.profilePictureUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(user.profilePictureUrl)
                        .into(profileImageView)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            openCamera()
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
    }


    private fun updateProfile(uid: String?) {
        val fullNameText = fullName.text.toString()
        val nameParts = fullNameText.split(" ")

        val firstName = if (nameParts.isNotEmpty()) nameParts[0] else ""
        val lastName = if (nameParts.size > 1) nameParts.last() else ""

        val userEmail = email.text.toString()
        val userBio = bio.text.toString()
        val userAge = age.text.toString().toIntOrNull() ?: 0
        val userSchool = school.text.toString()
        val userRole = role.text.toString()
        val userYear = year.text.toString().toIntOrNull() ?: 0
        val userField = field.text.toString()

        // Create User object without profile picture URL first
        val user = User(firstName, lastName, userEmail, userBio, userAge, userSchool, userRole, userYear, userField, "")

        if (uid != null) {
            if (this::imageUri.isInitialized) {
                // If imageUri is not null, upload profile picture first
                uploadProfilePic(uid, user)
            } else {
                // If no image to upload, just update the user info
                updateUserInfo(uid, user)
            }
        }
    }

    private fun uploadProfilePic(uid: String, user: User) {
        val profileImageRef = storageReference.child("profile_images/$uid.jpg")
        profileImageRef.putFile(imageUri!!).addOnSuccessListener {
            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                val profilePicUrl = uri.toString()
                // Update user object with profile picture URL
                val updatedUser = user.copy(profilePictureUrl = profilePicUrl)
                // Update user info with profile picture URL in Firebase
                updateUserInfo(uid, updatedUser)
            }
        }.addOnFailureListener {
            // Handle failure
            Toast.makeText(context, "Failed to upload profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserInfo(uid: String, user: User) {
        databaseReference.child(uid).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        cameraActivityResultLauncher.launch(null)
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

}

//storageReference = FirebaseStorage.getInstance().getReference("Users/${auth.currentUser?.uid}")
//storageReference.putFile(imageUri).addOnSuccessListener {
//    Toast.makeText(context, "Profile successfully updated", Toast.LENGTH_SHORT).show()
//}.addOnFailureListener {
//    Toast.makeText(context, "Failed to upload the image", Toast.LENGTH_SHORT).show()
//}
//}