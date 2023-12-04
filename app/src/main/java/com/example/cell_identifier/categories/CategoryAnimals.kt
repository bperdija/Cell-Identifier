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
import com.example.cell_identifier.databinding.CategoryAnimalsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryAnimals:AppCompatActivity() {
//    database
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: CategoryAnimalsBinding
    private lateinit var slides: ArrayList<SlideInfo>

    private var switchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Prepare the database:
        binding = CategoryAnimalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        slides = ArrayList()

        switchOn = binding.toggleView.isChecked

        binding.toggleView.setOnClickListener{
            switchOn = binding.toggleView.isChecked
            viewUpdate()
        }

//        switchOn == true: Grid View
        binding.mammals.setOnClickListener{
            searchSlides(Globals.SUBCAT_ANIMALS[0])
            viewUpdate()
        }

        binding.birds.setOnClickListener{
            searchSlides(Globals.SUBCAT_ANIMALS[1])
            viewUpdate()
        }

        binding.reptiles.setOnClickListener {
            searchSlides(Globals.SUBCAT_ANIMALS[2])
            viewUpdate()
        }

        binding.fish.setOnClickListener {
            searchSlides(Globals.SUBCAT_ANIMALS[3])
            viewUpdate()
        }

        binding.amphibians.setOnClickListener {
            searchSlides(Globals.SUBCAT_ANIMALS[4])
            viewUpdate()
        }

        binding.invertebrate.setOnClickListener {
            searchSlides(Globals.SUBCAT_ANIMALS[5])
            viewUpdate()
        }

        binding.others.setOnClickListener {
            searchSlides(Globals.SUBCAT_ANIMALS[6])
            viewUpdate()
        }

        binding.backButton.setOnClickListener{
            finish()
        }
    }

    private fun searchSlides(subCategory:String) {
        dbRef.addValueEventListener(object: ValueEventListener{
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
            binding.animalsRv.layoutManager = GridLayoutManager(this,3)
            binding.animalsRv.clipChildren = false
            binding.animalsRv.clipToPadding = false
            binding.animalsRv.addItemDecoration(object: RecyclerView.ItemDecoration(){
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
            binding.animalsRv.adapter = slideAdapter

        }else{
            binding.animalsRv.layoutManager = LinearLayoutManager(this)
            val slideAdapter = SlidesCardAdapter(slides)
            binding.animalsRv.adapter = slideAdapter
        }
    }
}