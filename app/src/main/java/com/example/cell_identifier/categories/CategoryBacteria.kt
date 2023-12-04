package com.example.cell_identifier.categories

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cell_identifier.Globals
import com.example.cell_identifier.SlideInfo
import com.example.cell_identifier.SlidesCardAdapter
import com.example.cell_identifier.SlidesGridAdapter
import com.example.cell_identifier.databinding.CategoryBacteriaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryBacteria:AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: CategoryBacteriaBinding
    private lateinit var slides: ArrayList<SlideInfo>

    private var switchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CategoryBacteriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        slides = ArrayList()

        switchOn = binding.toggleView.isChecked

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.toggleView.setOnClickListener{
            switchOn = binding.toggleView.isChecked

        }

        binding.cocci.setOnClickListener {
            searchSlides(Globals.SUBCAT_BACTERIA[0])
            viewUpdate()
        }

        binding.bacilli.setOnClickListener {
            searchSlides(Globals.SUBCAT_BACTERIA[1])
            viewUpdate()
        }

        binding.spirals.setOnClickListener {
            searchSlides(Globals.SUBCAT_BACTERIA[2])
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
            binding.bacteriaRv.layoutManager = GridLayoutManager(this,3)
            binding.bacteriaRv.clipChildren = false
            binding.bacteriaRv.clipToPadding = false
            binding.bacteriaRv.addItemDecoration(object: RecyclerView.ItemDecoration(){
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.set(10,20,10,20)
                }
            })
            val slideAdapter = SlidesGridAdapter(slides)
            binding.bacteriaRv.adapter = slideAdapter
        }else{
            binding.bacteriaRv.layoutManager = LinearLayoutManager(this)
            val slideAdapter = SlidesCardAdapter(slides)
            binding.bacteriaRv.adapter = slideAdapter
        }
    }
}