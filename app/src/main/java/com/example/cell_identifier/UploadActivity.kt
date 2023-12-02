package com.example.cell_identifier

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cell_identifier.databinding.ActivityUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


@Suppress("DEPRECATION")
class UploadActivity : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var addImageButton: Button
    private lateinit var categorySpinner: Spinner
    private lateinit var subCategorySpinner: Spinner
    private lateinit var categories: Array<String>

    //    Image uploading
    private lateinit var binding: ActivityUploadBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize variables:
        categorySpinner = findViewById(R.id.category_spinner)
        subCategorySpinner = findViewById(R.id.subcategory_spinner)
        categories = resources.getStringArray(R.array.categories)

        // subcategories. If Animal, show relevant sub. If plant, show others. Etc.

        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)
        addImageButton = findViewById(R.id.add_image_button)

        // Check Gallery Permission
        checkCameraPermission()

        // database:
        firebaseAuth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().getReference(Globals.SLIDES_STORAGE)
        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)

        val userEmail = firebaseAuth.currentUser?.email

        // Change the spinner entries:
        // https://stackoverflow.com/questions/41809942/how-to-programatically-set-entries-of-spinner-in-android
        binding.categorySpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val category = categorySpinner.selectedItem
                if(category == "People"){

                }else if(category == "Animals"){

                }else if(category == "Plants"){
                    val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                        binding.root.context, android.R.layout.simple_spinner_item, Globals.SUBCAT_PLANTS
                    )
                    subCategorySpinner.adapter = arrayAdapter

                }else if(category == "Bacteria"){
                    val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                        binding.root.context, android.R.layout.simple_spinner_item, Globals.SUBCAT_BACT
                    )
                    binding.subcategorySpinner.adapter = arrayAdapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.saveButton.setOnClickListener {

            if (binding.slideName.text.isNullOrBlank() || imageUri == null || binding.categorySpinner.selectedItemPosition == 0) {
                // Show an error popup
                showToast("Error! Name, Category, and Image are REQUIRED to submit a slide.")
            } else {
                // Upload slide information
                val slideName = binding.slideName.text.toString()
                val category = binding.categorySpinner.selectedItem.toString()
                val subCategory = binding.subcategorySpinner.selectedItem.toString()
                val comment = binding.slideDescription.text.toString()
                var slide: SlideInfo

                imageUri?.let {
                    val imageId = dbRef.push().key!!
                    storageRef.child(imageId).putFile(it)
                        .addOnSuccessListener { task ->
                            task.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener {
                                    val uri = it.toString()
                                    slide = SlideInfo(
                                        slideName,
                                        category,
                                        subCategory,
                                        comment,
                                        userEmail,
                                        uri
                                    )

                                    //  Add slide information to realtime database
                                    dbRef.child(imageId).setValue(slide)
                                        .addOnCompleteListener {
                                            Toast.makeText(
                                                this,
                                                "Uploaded successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                }
                        }
                }
                finish()
            }
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


    fun showToast(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.toast_upload_slide, findViewById(R.id.toast_layout))

        val toastText: TextView = layout.findViewById(R.id.toast_text)
        toastText.text = message

        with(Toast(applicationContext)) {
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
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
