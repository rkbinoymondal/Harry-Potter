package com.example.harrypotter.Adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.harrypotter.Fragments.AnalyticsFragment
import com.example.harrypotter.Fragments.CharacterFragment
import com.example.harrypotter.Fragments.FavoriteFragment
import com.example.harrypotter.Fragments.FavoriteFragmentCreature
import com.example.harrypotter.Fragments.FavoriteFragmentSpell

class FavoritePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteFragment()
            1 -> FavoriteFragmentSpell()
            else -> FavoriteFragmentCreature()
        }
    }
}