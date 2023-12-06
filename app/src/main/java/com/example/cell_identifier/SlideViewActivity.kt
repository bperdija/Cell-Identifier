package com.example.cell_identifier

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class SlideViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_view)

        val imageView: ImageView = findViewById(R.id.imageView)
        val backButton: Button = findViewById(R.id.backButton)

        val imageUri = intent.getStringExtra("imageUri")

        Picasso.get().load(imageUri).into(imageView)

        backButton.setOnClickListener {
            finish()
        }
    }
}
