package com.example.cell_identifier.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cell_identifier.Globals
import com.example.cell_identifier.SlideInfo
import com.example.cell_identifier.SlidesCardAdapter
import com.example.cell_identifier.SlidesGridAdapter
import com.example.cell_identifier.databinding.CategoryPlantsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryPlants:AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: CategoryPlantsBinding
    private lateinit var slides: ArrayList<SlideInfo>

    private var switchOn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CategoryPlantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        slides = ArrayList()

        switchOn = binding.toggleView.isChecked

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.toggleView.setOnClickListener{
            switchOn = binding.toggleView.isChecked
            viewUpdate()
        }

        binding.mosses.setOnClickListener {
            searchSlides(Globals.SUBCAT_PLANTS[0])
            viewUpdate()
        }

        binding.ferns.setOnClickListener {
            searchSlides(Globals.SUBCAT_PLANTS[1])
            viewUpdate()
        }

        binding.gymnosperms.setOnClickListener {
            searchSlides(Globals.SUBCAT_PLANTS[2])
            viewUpdate()
        }

        binding.angiosperms.setOnClickListener {
            searchSlides(Globals.SUBCAT_PLANTS[3])
            viewUpdate()
        }
    }

    private fun searchSlides(subCategory:String) {
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                slides.clear()
                if(snapshots.exists()){
                    for (snapshot in snapshots.children){
                        val slide = snapshot.getValue(SlideInfo::class.java)
                        if(slide?.subCategory == subCategory){
                            slides.add(slide)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun viewUpdate(){
        if(switchOn){
            binding.plantsRv.layoutManager = GridLayoutManager(this,3)
            val slideAdapter = SlidesGridAdapter(slides)
            binding.plantsRv.adapter = slideAdapter
        }else{
            binding.plantsRv.layoutManager = LinearLayoutManager(this)
            val slideAdapter = SlidesCardAdapter(slides)
            binding.plantsRv.adapter = slideAdapter
        }
    }
}