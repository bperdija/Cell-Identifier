package com.example.cell_identifier

import android.os.Bundle
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

        tabCS = TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
            tab.text = tabNames[position]
        }
        tabLM = TabLayoutMediator(tabLayout, viewPager2, tabCS)
        tabLM.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLM.detach()
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Cell_IdentifierTheme {
            Greeting("Android")
        }
    }
}