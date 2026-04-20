package com.example.harrypotter.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.harrypotter.Database.FavoriteData
import com.example.harrypotter.Database.FavoriteDataSpell
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.Model.SpellData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpellAdapterBig(val context: Context, val list: List<SpellData>) :
    RecyclerView.Adapter<SpellAdapterBig.SpellViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.spell_card_full, parent, false)
        return SpellViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.name.text = list[position].name ?: "No Name"
        holder.description.text = list[position].description ?: "No Description"

        val store = list[position]
        holder.name.text = list[position].name ?: "No Name"

        val database = OverallDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val dataList = database.favoriteDaoSpell().getFavoriteSpell(store.id!!)

            if (dataList != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    holder.favBtn.setIconResource(R.drawable.baseline_favorite_24)
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    holder.favBtn.setIconResource(R.drawable.outline_favorite_24)
                }
            }
        }

        holder.favBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val dataList = database.favoriteDaoSpell().getFavoriteSpell(store.id!!)

                if (dataList != null) {

                    CoroutineScope(Dispatchers.IO).launch {
                        database.favoriteDaoSpell().deleteFavoriteSpell(
                            FavoriteDataSpell(store.id, store.name, store.description)
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(
                                context,
                                "Spell removed from favorites successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.favBtn.setIconResource(R.drawable.outline_favorite_24)
                        }
                    }

                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        database.favoriteDaoSpell().insertFavoriteSpell(
                            FavoriteDataSpell(
                                store.id,
                                store.name,
                                store.description
                            )
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(
                                context,
                                "Spell added to favorites successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.favBtn.setIconResource(R.drawable.baseline_favorite_24)
                        }
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SpellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.name)
        var description = itemView.findViewById<TextView>(R.id.description)
        var favBtn = itemView.findViewById<MaterialButton>(R.id.favoriteBtnLarge)
    }
}