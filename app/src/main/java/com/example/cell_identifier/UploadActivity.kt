package com.example.cell_identifier

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cell_identifier.databinding.ActivityUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadActivity : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var addImageButton: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var categories: Array<String>
    private lateinit var categoryAdapter: ArrayAdapter<String>

    //    Image uploading
    private lateinit var binding: ActivityUploadBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    companion object{
        val SLIDES_STORAGE = "Slides"
        val RT_SLIDES_DB = "SlidesInfo"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Initialize variables:
        categorySpinner = findViewById(R.id.category_spinner)
        categories = resources.getStringArray(R.array.categories)
        categoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = categoryAdapter

        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)
        addImageButton = findViewById(R.id.add_image_button)

//        Check Gallery Permission
        checkCameraPermission()

        // database:
        firebaseAuth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().getReference(SLIDES_STORAGE)
        dbRef = FirebaseDatabase.getInstance().getReference(RT_SLIDES_DB)
        binding.saveButton.setOnClickListener {

//            Upload slide information
            val slideName = binding.slideName.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()
            val comment = binding.slideDescription.text.toString()
            var slide: SlideInfo

            imageUri?.let{
                val imageId = dbRef.push().key!!
                storageRef.child(imageId).putFile(it)
                    .addOnSuccessListener{task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener{
                                val uri = it.toString()
                                slide = SlideInfo(slideName, category, comment, uri)

//                                Add slide information to realtime database
                                dbRef.child(imageId).setValue(slide)
                                    .addOnCompleteListener {
                                        Toast.makeText(this, "Uploaded successfully", Toast.LENGTH_LONG).show()
                                    }

                            }
                    }
            }
            finish()
        }
        cancelButton.setOnClickListener() {
            finish()
        }

        val slideImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.slideImage.setImageURI(it)
            if(it != null){
                imageUri = it
            }
        }

        binding.addImageButton.setOnClickListener() {
            slideImage.launch("image/*")
        }

    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 0
            )
        }
    }
}
