package com.example.cell_identifier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cell_identifier.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CellGuessFragment:Fragment(){

    private lateinit var slides: ArrayList<SlideInfo>
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: FragmentSearchBinding
    private lateinit var storage: StorageReference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        storage = FirebaseStorage.getInstance().getReference(Globals.SLIDES_STORAGE)
        slides = ArrayList()
        getSlides()
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getSlides(){
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshots: DataSnapshot) {
                slides.clear()
                if (snapshots.exists()) {
                    for (snapshot in snapshots.children) {
                        val slide = snapshot.getValue(SlideInfo::class.java)
                        slides.add(slide!!)
                    }
                }
                if(slides.isEmpty()){
                    Toast.makeText(context, "Not enough slides available to generate a question.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}