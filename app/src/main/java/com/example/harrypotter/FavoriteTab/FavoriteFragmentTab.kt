package com.example.harrypotter.FavoriteTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.harrypotter.Adapters.FavoriteAdapter
import com.example.harrypotter.Adapters.FavoritePagerAdapter
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteFragmentTab : Fragment(R.layout.fragment_favorite_tab) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val adapt = FavoritePagerAdapter(this)

        viewPager.adapter = adapt

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Characters"
                1 -> "Spells"
                else -> "Creatures"
            }
        }.attach()
    }
}