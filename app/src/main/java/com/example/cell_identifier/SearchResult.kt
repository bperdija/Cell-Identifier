package com.example.cell_identifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cell_identifier.databinding.ListOfSlidesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SearchResult : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var binding: ListOfSlidesBinding
    private lateinit var exitButton: Button
    private lateinit var resultTitle: TextView
    private lateinit var slides: ArrayList<SlideInfo>
    private lateinit var keyword:String
    private lateinit var noResultText: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListOfSlidesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.slideDisplay.layoutManager = LinearLayoutManager(this)

        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        storageRef = FirebaseStorage.getInstance().getReference(Globals.SLIDES_STORAGE)
        exitButton = findViewById(R.id.exit_button)
        resultTitle = findViewById(R.id.slide_display_title)

//        Set "No Result" TextView
        noResultText = TextView(this)
        noResultText.isVisible = false
        noResultText.text = "No Result"
        noResultText.textSize = 18f
        noResultText.gravity = Gravity.START
        binding.root.addView(noResultText)

        slides = ArrayList()
        keyword = intent.getStringExtra(Globals.INTENT_SEARCH_KEY).toString()
        this.resultTitle.text = "Search: $keyword"
        searchSlides()

        exitButton.setOnClickListener {
            finish()
        }

    }

    //    Use the keyword to search through RT database and find SlidesInfo that contain the keyword
    private fun searchSlides() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                slides.clear()
                if (snapshots.exists()) {
                    for (snapshot in snapshots.children) {
                        val slide = snapshot.getValue(SlideInfo::class.java)

                        if (slide?.slideComment!!.lowercase().contains(keyword.lowercase())
                            || slide.slideName!!.lowercase().contains(keyword.lowercase())
                            || slide.category!!.lowercase().contains(keyword.lowercase())
                            || slide.subCategory!!.lowercase().contains(keyword.lowercase())
                        ) {
                            slides.add(slide)
                        }
                    }
                }
                if(slides.isEmpty()){
//                    "No result" text if no matches
                    binding.slideDisplay.isVisible = false
                    noResultText.isVisible = true
                }else {
                    val slidesCardAdapter = SlidesCardAdapter(slides)
                    binding.slideDisplay.adapter = slidesCardAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}