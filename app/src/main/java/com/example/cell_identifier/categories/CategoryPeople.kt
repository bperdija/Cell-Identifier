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
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun viewUpdate(){
        if(switchOn){
            binding.peopleRv.layoutManager = GridLayoutManager(this,3)
            binding.peopleRv.clipChildren = false
            binding.peopleRv.clipToPadding = false
            binding.peopleRv.addItemDecoration(object: RecyclerView.ItemDecoration(){
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
            binding.peopleRv.adapter = slideAdapter
        }else{
            binding.peopleRv.layoutManager = LinearLayoutManager(this)
            val slideAdapter = SlidesCardAdapter(slides)
            binding.peopleRv.adapter = slideAdapter
        }
    }
}