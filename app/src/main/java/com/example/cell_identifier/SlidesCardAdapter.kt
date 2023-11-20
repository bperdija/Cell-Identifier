package com.example.cell_identifier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SlidesCardAdapter(private val slides: List<SlideInfo>): RecyclerView.Adapter<SlidesCardAdapter.SlidesViewHolder>(){
    inner class SlidesViewHolder(view: View):RecyclerView.ViewHolder(view){
        var slideCardImageView: ImageView = view.findViewById(R.id.slide_card_image)
        val slideCardNameView: TextView = view.findViewById(R.id.slide_card_name)
        val slideCategoryView: TextView = view.findViewById(R.id.slide_category)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlidesCardAdapter.SlidesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.slides_card, parent, false)
        return SlidesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SlidesCardAdapter.SlidesViewHolder, position: Int) {
       val currentItem = slides[position]
        holder.slideCardImageView.setImageResource(currentItem.image)
        holder.slideCardNameView.text = currentItem.name
        holder.slideCategoryView.text = currentItem.category
    }

    override fun getItemCount(): Int {
        return slides.size
    }
}