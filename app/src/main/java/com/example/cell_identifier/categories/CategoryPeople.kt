package com.example.cell_identifier.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cell_identifier.Globals
import com.example.cell_identifier.SlideInfo
import com.example.cell_identifier.SlidesCardAdapter
import com.example.cell_identifier.SlidesGridAdapter
import com.example.cell_identifier.databinding.CategoryPeopleBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryPeople:AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: CategoryPeopleBinding
    private lateinit var slides: ArrayList<SlideInfo>

    private var switchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CategoryPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        slides = ArrayList()

        switchOn = binding.toggleView.isChecked

        searchSlides()
        viewUpdate()

        binding.backButton.setOnClickListener{
            finish()
        }

        binding.toggleView.setOnClickListener{
            switchOn = binding.toggleView.isChecked
            searchSlides()
            viewUpdate()
        }

    }

    private fun searchSlides() {
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                slides.clear()
                if(snapshots.exists()){
                    for (snapshot in snapshots.children){
                        val slide = snapshot.getValue(SlideInfo::class.java)
                        if(slide?.category == "People"){
                            slides.add(slide)
                        }
                    }
                }
                println("Mandu Debugging: slidesize=${slides.size}")
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun viewUpdate(){
        if(switchOn){
            binding.peopleRv.layoutManager = GridLayoutManager(this,3)
            val slideAdapter = SlidesGridAdapter(slides)
            binding.peopleRv.adapter = slideAdapter
        }else{
            binding.peopleRv.layoutManager = LinearLayoutManager(this)
            val slideAdapter = SlidesCardAdapter(slides)
            binding.peopleRv.adapter = slideAdapter
        }
    }
}