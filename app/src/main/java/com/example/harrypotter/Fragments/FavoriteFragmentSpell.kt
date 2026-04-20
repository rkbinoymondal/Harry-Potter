package com.example.harrypotter.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harrypotter.Adapters.CreatureAdapter
import com.example.harrypotter.Adapters.FavoriteAdapter
import com.example.harrypotter.Adapters.FavoriteAdapterSpell
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton

class FavoriteFragmentSpell : Fragment(R.layout.fragment_favorite_spell) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val charList = view.findViewById<RecyclerView>(R.id.favoriteSpellList)

        val database = OverallDatabase.getDatabase(requireContext())

        val emptyData = view.findViewById<LinearLayout>(R.id.emptyData)

        database.favoriteDaoSpell().getFavoriteAllSpell().observe(viewLifecycleOwner, {

            if (it.size == 0) {
                emptyData.visibility = View.VISIBLE
            } else {
                emptyData.visibility = View.GONE
            }
            val adapt = FavoriteAdapterSpell(requireContext(), it)

            charList.adapter = adapt
            charList.layoutManager = LinearLayoutManager(requireContext())

        })
    }
}