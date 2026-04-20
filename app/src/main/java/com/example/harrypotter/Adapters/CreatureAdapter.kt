package com.example.harrypotter.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.harrypotter.Database.FavoriteCreatureData
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

class CreatureAdapter(val context: Context, val list: List<CharacterData>, val parentView: View) :
    RecyclerView.Adapter<CreatureAdapter.CreatureViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.character_card_layout, parent, false)
        return CreatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreatureViewHolder, position: Int) {
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

        val store = list[position]

        val database = OverallDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val dataList = database.favoriteDaoCreature().getFavoriteCreature(store.id!!)

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
                val dataList = database.favoriteDaoCreature().getFavoriteCreature(store.id!!)

                if (dataList != null) {

                    CoroutineScope(Dispatchers.IO).launch {
                        database.favoriteDaoCreature().deleteFavoriteCreature(
                            FavoriteCreatureData(
                                store.id,
                                store.name,
                                store.house,
                                store.ancestry,
                                store.patronus,
                                store.image,
                                store.wizard,
                                store.hogwartsStudent,
                                store.hogwartsStaff,
                                store.species
                            )
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(
                                context,
                                "Creature removed from favorites successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.favBtn.setIconResource(R.drawable.outline_favorite_24)
                        }
                    }

                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        database.favoriteDaoCreature().insertFavoriteCreature(
                            FavoriteCreatureData(
                                store.id,
                                store.name,
                                store.house,
                                store.ancestry,
                                store.patronus,
                                store.image,
                                store.wizard,
                                store.hogwartsStudent,
                                store.hogwartsStaff,
                                store.species
                            )
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(
                                context,
                                "Creature added to favorites successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.favBtn.setIconResource(R.drawable.baseline_favorite_24)
                        }
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            val backCard = parentView.findViewById<MaterialCardView>(R.id.dataCard)

            var houseText = list[position].house ?: "No House"

            if (houseText.isNullOrEmpty()) {
                houseText = "Any House"
            }

            val color = when (houseText.lowercase()) {
                "gryffindor" -> ContextCompat.getColor(context, R.color.gryffindor_red)
                "slytherin" -> ContextCompat.getColor(context, R.color.slytherin_green)
                "ravenclaw" -> ContextCompat.getColor(context, R.color.ravenclaw_blue)
                "hufflepuff" -> ContextCompat.getColor(context, R.color.hufflepuff_yellow)
                else -> ContextCompat.getColor(context, R.color.white)
            }

//            val backCard = parentView.findViewById<>()
            backCard.strokeColor = color

            val logo = when (houseText.lowercase()) {
                "gryffindor" -> R.drawable.gryffindor
                "slytherin" -> R.drawable.slytherin
                "ravenclaw" -> R.drawable.ravenclaw
                "hufflepuff" -> R.drawable.hufflepuff
                else -> 0
            }

            val fullBack = parentView.findViewById<ImageView>(R.id.fullBackground)

            if (logo != 0) {
                fullBack.setImageResource(logo)
            } else {
                fullBack.setImageResource(0)
            }

            val placeholder = when (houseText.lowercase()) {
                "gryffindor" -> R.drawable.gryffindor
                "slytherin" -> R.drawable.slytherin
                "ravenclaw" -> R.drawable.ravenclaw
                "hufflepuff" -> R.drawable.hufflepuff
                else -> R.drawable.hogwart_castle_new
            }

            val photoNew = parentView.findViewById<ImageView>(R.id.photoNew)

            if (list[position].image.isNullOrEmpty()) {

                if (placeholder == R.drawable.hogwart_castle_new) {
                    photoNew.setImageResource(placeholder)
                    photoNew.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    photoNew.setImageResource(placeholder)
                }
            } else {
                Glide.with(context).load(list[position].image).into(photoNew)
            }

            val wizard = parentView.findViewById<TextView>(R.id.wizard)

            list[position].wizard ?: false
            if (list[position].wizard == true) {
                wizard.text = "Wizard"
            } else {
                wizard.text = "Not a Wizard"
            }
            list[position].hogwartsStudent ?: false
            list[position].hogwartsStaff ?: false

            val house = parentView.findViewById<TextView>(R.id.house)

            if (list[position].hogwartsStudent == true) {
                house.text = "Student at ${houseText}"
            } else if (list[position].hogwartsStaff == true) {
                house.text = "Staff at ${houseText}"
            } else {
                house.text = "Neither Student nor Staff at ${houseText}"
            }

            var ancestryText = list[position].ancestry ?: "No Ancestry"

            if (ancestryText.isNullOrEmpty()) {
                ancestryText = "N/A"
            }

            val ancestry = parentView.findViewById<TextView>(R.id.ancestry)
            ancestry.text = "Ancestry: " + ancestryText

            var dateOfBirth = list[position].dateOfBirth ?: "No DOB"

            if (dateOfBirth.isNullOrEmpty()) {
                dateOfBirth = "N/A"
            }

            val dateOfBirthText = parentView.findViewById<TextView>(R.id.dateOfBirth)

            dateOfBirthText.text = "Date Of Birth: " + dateOfBirth

            val alternateNamesList = list[position].alternate_names

            var alternateText = parentView.findViewById<TextView>(R.id.alternateNames)

            alternateText.text = "Alternate Names: "

            if (alternateNamesList.size == 0) {
                alternateText.text = "Alternate Names: N/A"
            } else {
                for (nameAlter in alternateNamesList) {
                    alternateText.text = alternateText.text.toString() + nameAlter + ", "
                }
            }

            var wandText = list[position].wand?.core ?: "No Wand"

            if (wandText.isNullOrEmpty()) {
                wandText = "N/A"
            }

            val wand = parentView.findViewById<TextView>(R.id.wandExtra)

            wand.text = "Wand: " + wandText

            var actorText = list[position].actor ?: "No Patronus"

            if (actorText.isNullOrEmpty()) {
                actorText = "N/A"
            }

            val actor = parentView.findViewById<TextView>(R.id.actor)

            actor.text = "Actor: " + actorText

            var patronusText = list[position].patronus ?: "No Patronus"

            if (patronusText.isNullOrEmpty()) {
                patronusText = "N/A"
            }

            val patronus = parentView.findViewById<TextView>(R.id.patronus)

            patronus.text = "Patronus: " + patronusText

            parentView.findViewById<TextView>(R.id.nameNew).text =
                list[position].name ?: "No Name"

            val overlay = parentView.findViewById<FrameLayout>(R.id.overlay)
            overlay.visibility = View.VISIBLE

            backCard.setOnClickListener {

            }
            overlay.setOnClickListener {
                overlay.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class CreatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo = itemView.findViewById<ImageView>(R.id.photo)
        var name = itemView.findViewById<TextView>(R.id.name)
        var favBtn = itemView.findViewById<MaterialButton>(R.id.favoriteBtnSmall)
    }
}