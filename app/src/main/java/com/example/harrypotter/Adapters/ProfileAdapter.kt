package com.example.harrypotter.Adapters

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.harrypotter.Database.FavoriteDAO
import com.example.harrypotter.Database.FavoriteData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class ProfileAdapter(val context: Context, val list: List<CharacterData>, val parentView: View) :
    RecyclerView.Adapter<ProfileAdapter.CharacterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.character_card_layout_profile, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {

        val store = list[position]
        holder.name.text = list[position].name ?: "No Name"

        var houseText = list[position].house ?: "No House"

        val placeholder = when (houseText.lowercase()) {
            "gryffindor" -> R.drawable.gryffindor
            "slytherin" -> R.drawable.slytherin
            "ravenclaw" -> R.drawable.ravenclaw
            "hufflepuff" -> R.drawable.hufflepuff
            else -> R.drawable.hogwart_castle_new
        }

        if (list[position].image.isNullOrEmpty()) {
            if (placeholder == R.drawable.hogwart_castle_new) {
                holder.photo.setImageResource(placeholder)
                holder.photo.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                holder.photo.setImageResource(placeholder)
            }
        } else {
            Glide.with(context).load(list[position].image).into(holder.photo)
        }

        val sharedPref = context.getSharedPreferences("ImageHolder", MODE_PRIVATE)
        val editor = sharedPref.edit()

        holder.itemView.setOnClickListener {
            val photoProfile = parentView.findViewById<ImageView>(R.id.photoProfile)
            if (list[position].image.isNullOrEmpty()) {
                if (placeholder == R.drawable.hogwart_castle_new) {
                    photoProfile.setImageResource(placeholder)
                    photoProfile.scaleType = ImageView.ScaleType.CENTER_CROP
                    editor.putInt("defaultImage", placeholder)
                    editor.apply()
                } else {
                    photoProfile.setImageResource(placeholder)
                    editor.putInt("defaultImage", placeholder)
                    editor.apply()
                }
            } else {
                Glide.with(context).load(list[position].image).into(photoProfile)
                editor.putString("defaultImage1", list[position].image)
                editor.apply()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo = itemView.findViewById<ImageView>(R.id.photo)
        var name = itemView.findViewById<TextView>(R.id.name)
    }
}