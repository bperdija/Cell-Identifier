package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cell_identifier.List_Slides.RecommendedAdapter
import com.example.cell_identifier.List_Slides.RecommendedSlide
import com.example.cell_identifier.categories.CategoryAnimals
import com.example.cell_identifier.categories.CategoryBacteria
import com.example.cell_identifier.categories.CategoryPeople
import com.example.cell_identifier.categories.CategoryPlants

class HomeFragment : Fragment() {
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var recommendedAdapter: RecommendedAdapter

    // This list will need to pull from a database
    private val slidesList = listOf(
        RecommendedSlide(R.drawable.tree, "Sunflower Petal", "#123"),
        RecommendedSlide(R.drawable.testing, "Butterfly Wings", "#65"),
        RecommendedSlide(R.drawable.welcome, "Sunflower Flower", "#79"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView)

        /// Option 2 -------------
        recommendedAdapter = RecommendedAdapter(slidesList) { clickedSlide ->
            // Handle click on the item
            Log.d("HomeFragment", "Item Clicked: ${clickedSlide.name}")
        }
        recommendedRecyclerView.setHasFixedSize(true)
        recommendedRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recommendedRecyclerView.adapter = recommendedAdapter

        view.findViewById<RelativeLayout>(R.id.slideOfDayItem).setOnClickListener {
            Log.d("HomeFragment", "Slide of the Day Item Clicked")
        }

        view.findViewById<LinearLayout>(R.id.peopleCircle).setOnClickListener {
            Log.d("HomeFragment", "People Circle Clicked")
            val intent = Intent(this.context, CategoryPeople::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.animalCircle).setOnClickListener {
            Log.d("HomeFragment", "Animal Circle Clicked")
            val intent = Intent(this.context, CategoryAnimals::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.plantCircle).setOnClickListener {
            Log.d("HomeFragment", "Plant Circle Clicked")
            val intent = Intent(this.context, CategoryPlants::class.java)
            startActivity(intent)
        }


        view.findViewById<LinearLayout>(R.id.bacteriaCircle).setOnClickListener {
            Log.d("HomeFragment", "Bacteria Circle Clicked")
            val intent = Intent(this.context, CategoryBacteria::class.java)
            startActivity(intent)
        }

        return view
    }
}
