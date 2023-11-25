package com.example.cell_identifier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class UploadActivity : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var addImageButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)
        addImageButton = findViewById(R.id.add_image_button)

        //Implement this once database is set up
        saveButton.setOnClickListener() {
            finish()
        }
        cancelButton.setOnClickListener() {
            finish()
        }
        addImageButton.setOnClickListener(){
            //Implement this once database is set up
        }

    }
}