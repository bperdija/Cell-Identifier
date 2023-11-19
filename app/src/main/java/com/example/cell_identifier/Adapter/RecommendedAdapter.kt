package com.example.cell_identifier.List_Slides

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cell_identifier.R

//class RecommendedAdapter(private val slidesList: List<RecommendedSlide>) :
//    RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {
//
//    inner class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//    {
//        var pictureImageView: ImageView = itemView.findViewById(R.id.picture)
//        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
//        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
//        val itemView =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.layout_recommended_slides, parent, false)
//        return RecommendedViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
//        val currentItem = slidesList[position]
//        holder.pictureImageView.setImageResource(currentItem.image)
//        holder.nameTextView.text = currentItem.name
//        holder.numberTextView.text = currentItem.number
//        // Set other data as needed
//    }
//
//
//    override fun getItemCount() = slidesList.size
//}

// Option 1:
//class RecommendedAdapter(
//    private val slidesList: List<RecommendedSlide>,
//    private val onItemClick: (Int) -> Unit
//) : RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {
//
//    inner class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var pictureImageView: ImageView = itemView.findViewById(R.id.picture)
//        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
//        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
//
//        init {
//            itemView.setOnClickListener {
//                onItemClick(adapterPosition)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
//        val itemView =
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.layout_recommended_slides, parent, false)
//        return RecommendedViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
//        val currentItem = slidesList[position]
//        holder.pictureImageView.setImageResource(currentItem.image)
//        holder.nameTextView.text = currentItem.name
//        holder.numberTextView.text = currentItem.number
//        // Set other data as needed
//    }
//
//    override fun getItemCount() = slidesList.size
//}

// Option 2 --------------------
class RecommendedAdapter(private val slidesList: List<RecommendedSlide>, private var onItemClick: (RecommendedSlide) -> Unit) :
    RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder>() {

    inner class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pictureImageView: ImageView = itemView.findViewById(R.id.picture)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(slidesList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_recommended_slides, parent, false)
        return RecommendedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val currentItem = slidesList[position]
        holder.pictureImageView.setImageResource(currentItem.image)
        holder.nameTextView.text = currentItem.name
        holder.numberTextView.text = currentItem.number
        // Set other data as needed
    }


    override fun getItemCount() = slidesList.size
}