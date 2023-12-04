package com.example.cell_identifier

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide


class AnnotationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation)

        // Retrieve data from intent extras
        val slideName = intent.getStringExtra("slideName")
        val category = intent.getStringExtra("category")
        val subCategory = intent.getStringExtra("subCategory")
        val comment = intent.getStringExtra("comment")
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        // Load the image into the ImageView using Glide
        val annotatedImageView: ImageView = findViewById(R.id.annotatedImageView)
        Glide.with(this)
            .load(imageUri)
            .into(annotatedImageView)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Create an intent to navigate back to UploadActivity
            val intent = Intent(this, UploadActivity::class.java)

            // Start the UploadActivity
            startActivity(intent)

            // Finish the current activity (AnnotationActivity)
            finish()
        }
    }

        // Now with the imageUri, we can use it in our AnnotationActivity

}