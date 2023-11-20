package com.example.cell_identifier

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UploadFragment:Fragment() {
    private lateinit var btnUpload: FloatingActionButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upload, container, false)
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