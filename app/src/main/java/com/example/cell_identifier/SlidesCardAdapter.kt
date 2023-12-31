package com.example.cell_identifier

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cell_identifier.databinding.SlidesCardBinding
import com.squareup.picasso.Picasso

class SlidesCardAdapter(private val slides: java.util.ArrayList<SlideInfo>): RecyclerView.Adapter<SlidesCardAdapter.SlidesViewHolder>(){
    class SlidesViewHolder(val binding: SlidesCardBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlidesViewHolder {
        return SlidesViewHolder(SlidesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SlidesViewHolder, position: Int) {
        val currentItem = slides[position]
        holder.apply {
            binding.apply {
                slideCardName.text = "Name: ${currentItem.slideName}"
                slideCardCategory.text = "Category: ${currentItem.category}"
                slideCardDescription.text = currentItem.slideComment

                Picasso.get().load(currentItem.imageUri).into(slideCardImage)

                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context, SlideViewActivity::class.java)
                    intent.putExtra("imageUri", slides[adapterPosition].imageUri)
                    intent.putExtra("slideName", slides[adapterPosition].slideName)
                    intent.putExtra("category", slides[adapterPosition].category)
                    intent.putExtra("slideComment", slides[adapterPosition].slideComment)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return slides.size
    }
}