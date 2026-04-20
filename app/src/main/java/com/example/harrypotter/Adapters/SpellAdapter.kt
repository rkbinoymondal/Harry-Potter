package com.example.harrypotter.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.harrypotter.Database.FavoriteDAO
import com.example.harrypotter.Database.FavoriteData
import com.example.harrypotter.Database.FavoriteDataSpell
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.Model.SpellData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class SpellAdapter(val context: Context, val list: List<SpellData>, val parentView: View) :
    RecyclerView.Adapter<SpellAdapter.SpellViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.spell_card_layout, parent, false)
        return SpellViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {

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

        holder.itemView.setOnClickListener {
            val card = parentView.findViewById<MaterialCardView>(R.id.dataCard2)

            parentView.findViewById<TextView>(R.id.nameSpell).text =
                list[position].name ?: "No Name"
            parentView.findViewById<TextView>(R.id.descriptionSpell).text =
                list[position].description ?: "No Description"

            val overlay = parentView.findViewById<FrameLayout>(R.id.overlay2)
            overlay.visibility = View.VISIBLE

            card.setOnClickListener {

            }
            overlay.setOnClickListener {
                overlay.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class SpellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.name)
        var favBtn = itemView.findViewById<MaterialButton>(R.id.favoriteBtnSmall)
    }
}