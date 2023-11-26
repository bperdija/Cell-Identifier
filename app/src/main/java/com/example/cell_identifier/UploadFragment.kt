package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cell_identifier.databinding.FragmentUploadBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UploadFragment:Fragment() {

    private lateinit var slides:ArrayList<SlideInfo>
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: FragmentUploadBinding

    private lateinit var btnUpload: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        dbRef = FirebaseDatabase.getInstance().getReference(UploadActivity.RT_SLIDES_DB)
        slides = arrayListOf()
        binding = FragmentUploadBinding.inflate(inflater, container, false)

        getFirebaseData()

        binding.slidesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }

        return binding.root
    }

    private fun getFirebaseData()
    {
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshots: DataSnapshot) {
                slides.clear()
                if(snapshots.exists()){
                    for (snapshot in snapshots.children){
                        val slide = snapshot.getValue(SlideInfo::class.java)
                        slides.add(slide!!)
                    }
                }
                val slidesCardAdapter = SlidesCardAdapter(slides)
                binding.slidesRecyclerView.adapter = slidesCardAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
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