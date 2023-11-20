package com.example.cell_identifier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class UploadActivity : AppCompatActivity() {
    private lateinit var cellImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
    }
}