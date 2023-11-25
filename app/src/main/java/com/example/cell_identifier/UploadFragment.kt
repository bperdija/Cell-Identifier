package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UploadFragment:Fragment() {

    // Test List until Database is implemented
    private val slides = listOf(
        SlideInfo(R.drawable.tree, "Tree", "Plant"),
        SlideInfo(R.drawable.tree, "Tree", "Plant"),
        SlideInfo(R.drawable.tree, "Tree", "Plant"),
        SlideInfo(R.drawable.tree, "Tree", "Plant"),
        SlideInfo(R.drawable.tree, "Tree", "Plant"),
        SlideInfo(R.drawable.tree, "Tree", "Plant"),
    )

    private lateinit var slidesRecyclerView: RecyclerView
    private lateinit var slidesCardAdapter: SlidesCardAdapter

    private lateinit var btnUpload: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        slidesRecyclerView = view.findViewById(R.id.slides_recycler_view)
        slidesCardAdapter = SlidesCardAdapter(slides)
        slidesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        slidesRecyclerView.adapter = slidesCardAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnUpload = view.findViewById(R.id.btn_upload)
        btnUpload.setOnClickListener() {
            val intent = Intent(context, UploadActivity::class.java)
            startActivity(intent)
        }
    }
}