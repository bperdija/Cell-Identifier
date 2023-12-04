package com.example.cell_identifier

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cell_identifier.databinding.SlidesGridItemBinding
import com.squareup.picasso.Picasso

class SlidesGridAdapter(val slides: ArrayList<SlideInfo>):RecyclerView.Adapter<SlidesGridAdapter.SlidesGVHolder>() {
    class SlidesGVHolder(val binding: SlidesGridItemBinding):RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlidesGVHolder {
        return SlidesGVHolder(SlidesGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return slides.size
    }

    override fun onBindViewHolder(holder: SlidesGVHolder, position: Int) {
        val currentItem = slides[position]
        holder.apply {
            binding.apply {
                Picasso.get().load(currentItem.imageUri).into(slideGridImg)
            }
        }
    }

}