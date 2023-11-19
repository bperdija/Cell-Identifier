package com.example.cell_identifier

import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.cell_identifier.ui.theme.Cell_IdentifierTheme
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var uploadFragment: UploadFragment
    private lateinit var cellGuessFragment: CellGuessFragment
    private lateinit var profileFragment: ProfileFragment

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentAdapter: FragmentAdapter
    private lateinit var fragments: ArrayList<Fragment>

    private val tabNames = arrayOf("Home", "Search", "Upload", "Guesser", "Profile")

    private lateinit var tabCS: TabLayoutMediator.TabConfigurationStrategy
    private lateinit var tabLM: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab)

        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        uploadFragment = UploadFragment()
        cellGuessFragment = CellGuessFragment()
        profileFragment = ProfileFragment()

        fragments = ArrayList()
        fragments.add(homeFragment)
        fragments.add(searchFragment)
        fragments.add(uploadFragment)
        fragments.add(cellGuessFragment)
        fragments.add(profileFragment)

        fragmentAdapter = FragmentAdapter(this, fragments)
        viewPager2.adapter = fragmentAdapter

        // Prevent swiping between fragments
        viewPager2.isUserInputEnabled = false

        // Adjust tabs on the bottom
        tabCS = TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
            val customTab = layoutInflater.inflate(R.layout.layout_tabs, null)
            val tabIcon = customTab.findViewById<ImageView>(R.id.tabIcon)
            val tabText = customTab.findViewById<TextView>(R.id.tabText)

            // Set icon and text based on position
            tabIcon.setImageResource(getTabIcon(position))
            tabText.text = tabNames[position]

            // Calculate text size based on screen width
            val screenWidth = resources.displayMetrics.widthPixels
            val textSize = screenWidth / 100f
            tabText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

            // Set custom view for the tab
            tab.customView = customTab
        }

        tabLM = TabLayoutMediator(tabLayout, viewPager2, tabCS)
        tabLM.attach()

    }

    // Set icons on the bottom of the screen
    fun getTabIcon(position: Int): Int {
        when (position) {
            0 -> return R.drawable.ic_home
            1 -> return R.drawable.ic_search
            2 -> return R.drawable.ic_add
            3 -> return R.drawable.ic_book
            4 -> return R.drawable.ic_person
            else -> return R.drawable.ic_search
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLM.detach()
    }
}