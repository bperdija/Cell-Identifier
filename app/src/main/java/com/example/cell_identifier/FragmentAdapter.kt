package com.example.cell_identifier

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// This adapter is from the lecture demo: Fragment and ViewPager
class FragmentAdapter(activity: FragmentActivity, var list: ArrayList<Fragment>):FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

}