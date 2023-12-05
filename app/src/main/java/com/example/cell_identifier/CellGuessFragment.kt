package com.example.cell_identifier

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import androidx.fragment.app.Fragment
import com.example.cell_identifier.databinding.FragmentCellGuesserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import java.lang.Integer.max

class CellGuessFragment:Fragment(){

    private lateinit var slides: ArrayList<SlideInfo>
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: FragmentCellGuesserBinding
    private lateinit var storage: StorageReference
    private var currentSlideIndex = -1

    private var currentScore = 0
    private var currentStreak = 0
    private var highestStreak = 0
    private var highestScore = 0

    private lateinit var currentScoreTextView: TextView
    private lateinit var currentStreakTextView: TextView
    private lateinit var highestStreakTextView: TextView
    private lateinit var highestScoreTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        dbRef = FirebaseDatabase.getInstance().getReference(Globals.RT_SLIDES_DB)
        storage = FirebaseStorage.getInstance().getReference(Globals.SLIDES_STORAGE)
        slides = ArrayList()
        getSlides()
        binding = FragmentCellGuesserBinding.inflate(inflater, container, false)

        binding.generateButton.setOnClickListener {
            if (slides.isNotEmpty()) {
                val randomSlide = slides.random() // Select a random slide
                showGuessDialog(randomSlide)
            } else {
                Toast.makeText(context, "No slides available.", Toast.LENGTH_SHORT).show()
            }
        }

        updateStatsDisplay()


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

    private fun showNextSlide() {
        if (slides.isNotEmpty()) {
            currentSlideIndex = (currentSlideIndex + 1) % slides.size
            showGuessDialog(slides[currentSlideIndex])
        } else {
            Toast.makeText(context, "No more slides available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateStats(correctGuess: Boolean) {
        if (correctGuess) {
            currentScore++
            currentStreak++
            highestScore = max(highestScore, currentScore)
            highestStreak = max(highestStreak, currentStreak)
        } else {
            currentStreak = 0
        }

        updateStatsDisplay()
    }

    private fun updateStatsDisplay() {
        binding.highestScoreTextView.text = "Highest Score: $highestScore"
        binding.highestStreakTextView.text = "Highest Streak: $highestStreak"
        binding.currentStreakTextView.text = "Current Streak: $currentStreak"
        binding.currentScoreTextView.text = "Current Score: $currentScore"
    }

    private fun showGuessDialog(slide: SlideInfo) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_slide_guesser, null)
        val slideImageView = dialogView.findViewById<ImageView>(R.id.slideImageView)
        val categoryButtons = listOf(
            dialogView.findViewById<Button>(R.id.categoryButton1),
            dialogView.findViewById<Button>(R.id.categoryButton2),
            dialogView.findViewById<Button>(R.id.categoryButton3),
            dialogView.findViewById<Button>(R.id.categoryButton4)
        )

        // Load the image into slideImageView
        Picasso.get().load(slide.imageUri).into(slideImageView)

        // List of categories
        val categories = listOf("People", "Animals", "Plants", "Bacteria")

        // Set up category buttons
        categoryButtons.forEachIndexed { index, button ->
            button.text = categories[index]
            button.setOnClickListener {
                // Handle category click
                val isCorrect = slide.category.equals(categories[index], ignoreCase = true)
                updateStats(isCorrect)
                button.setBackgroundColor(if (isCorrect) Color.GREEN else Color.RED)
                // Update score logic here
            }
        }

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.nextButton).setOnClickListener {
            // Load next slide and update dialog contents
            dialog.dismiss()
            showNextSlide()
        }

        dialog.show()
    }


}