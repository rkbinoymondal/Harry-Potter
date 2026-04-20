package com.example.harrypotter.Adapters

import android.content.Context
import android.graphics.Color
import android.media.Image
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
import com.example.harrypotter.Database.FavoriteData
import com.example.harrypotter.Database.OverallDatabase
import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterAdapterBig(
    val context: Context,
    val list: List<CharacterData>,
    val parentView: View
) : RecyclerView.Adapter<CharacterAdapterBig.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.character_card_full, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.name.text = list[position].name ?: "No Name"

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

        holder.backCard.strokeColor = color

        val logo = when (houseText.lowercase()) {
            "gryffindor" -> R.drawable.gryffindor
            "slytherin" -> R.drawable.slytherin
            "ravenclaw" -> R.drawable.ravenclaw
            "hufflepuff" -> R.drawable.hufflepuff
            else -> 0
        }

        if (logo != 0) {
            holder.fullBack.setImageResource(logo)
        } else {
            holder.fullBack.setImageResource(0)
        }

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

        list[position].wizard ?: false
        if (list[position].wizard == true) {
            holder.wizard.text = "Wizard"
        } else {
            holder.wizard.text = "Not a Wizard"
        }
        list[position].hogwartsStudent ?: false
        list[position].hogwartsStaff ?: false
        if (list[position].hogwartsStudent == true) {
            holder.house.text = "Student at ${houseText}"
        } else if (list[position].hogwartsStaff == true) {
            holder.house.text = "Staff at ${houseText}"
        } else {
            holder.house.text = "Neither Student nor Staff at ${houseText}"
        }

        var ancestryText = list[position].ancestry ?: "No Ancestry"

        if (ancestryText.isNullOrEmpty()) {
            ancestryText = "N/A"
        }
        holder.ancestry.text = "Ancestry: " + ancestryText

        var patronusText = list[position].patronus ?: "No Patronus"

        if (patronusText.isNullOrEmpty()) {
            patronusText = "N/A"
        }
        holder.patronus.text = "Patronus: " + patronusText

        val store = list[position]

        val database = OverallDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val dataList = database.favoriteDao().getFavorite(store.id!!)

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
                val dataList = database.favoriteDao().getFavorite(store.id!!)

                if (dataList != null) {

                    CoroutineScope(Dispatchers.IO).launch {
                        database.favoriteDao().deleteFavorite(
                            FavoriteData(
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
                            holder.favBtn.setIconResource(R.drawable.outline_favorite_24)
                            Toast.makeText(
                                context,
                                "Character removed from favorites successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        database.favoriteDao().insertFavorite(
                            FavoriteData(
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
                                "Character added to favorites successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.favBtn.setIconResource(R.drawable.baseline_favorite_24)
                        }
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            val backCardBig = parentView.findViewById<MaterialCardView>(R.id.dataCardBig)

            var houseTextBig = list[position].house ?: "No House"

            if (houseTextBig.isNullOrEmpty()) {
                houseTextBig = "Any House"
            }

            val colorBig = when (houseTextBig.lowercase()) {
                "gryffindor" -> ContextCompat.getColor(context, R.color.gryffindor_red)
                "slytherin" -> ContextCompat.getColor(context, R.color.slytherin_green)
                "ravenclaw" -> ContextCompat.getColor(context, R.color.ravenclaw_blue)
                "hufflepuff" -> ContextCompat.getColor(context, R.color.hufflepuff_yellow)
                else -> ContextCompat.getColor(context, R.color.white)
            }

            backCardBig.strokeColor = colorBig

            val logoBig = when (houseTextBig.lowercase()) {
                "gryffindor" -> R.drawable.gryffindor
                "slytherin" -> R.drawable.slytherin
                "ravenclaw" -> R.drawable.ravenclaw
                "hufflepuff" -> R.drawable.hufflepuff
                else -> 0
            }

            val fullBackBig = parentView.findViewById<ImageView>(R.id.fullBackgroundBig)

            if (logoBig != 0) {
                fullBackBig.setImageResource(logoBig)
            } else {
                fullBackBig.setImageResource(0)
            }

            val placeholderBig = when (houseTextBig.lowercase()) {
                "gryffindor" -> R.drawable.gryffindor
                "slytherin" -> R.drawable.slytherin
                "ravenclaw" -> R.drawable.ravenclaw
                "hufflepuff" -> R.drawable.hufflepuff
                else -> R.drawable.hogwart_castle_new
            }

            val photoNewBig = parentView.findViewById<ImageView>(R.id.photoNewBig)

            if (list[position].image.isNullOrEmpty()) {

                if (placeholderBig == R.drawable.hogwart_castle_new) {
                    photoNewBig.setImageResource(placeholderBig)
                    photoNewBig.scaleType = ImageView.ScaleType.CENTER_CROP
                } else {
                    photoNewBig.setImageResource(placeholderBig)
                }
            } else {
                Glide.with(context).load(list[position].image).into(photoNewBig)
            }

            val wizardBig = parentView.findViewById<TextView>(R.id.wizardBig)

            list[position].wizard ?: false
            if (list[position].wizard == true) {
                wizardBig.text = "Wizard"
            } else {
                wizardBig.text = "Not a Wizard"
            }
            list[position].hogwartsStudent ?: false
            list[position].hogwartsStaff ?: false

            val houseBig = parentView.findViewById<TextView>(R.id.houseBig)

            if (list[position].hogwartsStudent == true) {
                houseBig.text = "Student at ${houseTextBig}"
            } else if (list[position].hogwartsStaff == true) {
                houseBig.text = "Staff at ${houseTextBig}"
            } else {
                houseBig.text = "Neither Student nor Staff at ${houseTextBig}"
            }

            var ancestryTextBig = list[position].ancestry ?: "No Ancestry"

            if (ancestryTextBig.isNullOrEmpty()) {
                ancestryTextBig = "N/A"
            }

            val ancestryBig = parentView.findViewById<TextView>(R.id.ancestryBig)
            ancestryBig.text = "Ancestry: " + ancestryTextBig

            var patronusTextBig = list[position].patronus ?: "No Patronus"

            if (patronusTextBig.isNullOrEmpty()) {
                patronusTextBig = "N/A"
            }

            val patronusBig = parentView.findViewById<TextView>(R.id.patronusBig)

            patronusBig.text = "Patronus: " + patronusTextBig

            var dateOfBirthBig = list[position].dateOfBirth ?: "No DOB"

            if (dateOfBirthBig.isNullOrEmpty()) {
                dateOfBirthBig = "N/A"
            }

            val dateOfBirthTextBig = parentView.findViewById<TextView>(R.id.dateOfBirthBig)

            dateOfBirthTextBig.text = "Date Of Birth: " + dateOfBirthBig

            val alternateNamesListBig = list[position].alternate_names

            var alternateTextBig = parentView.findViewById<TextView>(R.id.alternateNamesBig)

            alternateTextBig.text = "Alternate Names: "

            if (alternateNamesListBig.size == 0) {
                alternateTextBig.text = "Alternate Names: N/A"
            } else {
                for (nameAlterBig in alternateNamesListBig) {
                    alternateTextBig.text = alternateTextBig.text.toString() + nameAlterBig + ", "
                }
            }

            var wandTextBig = list[position].wand?.core ?: "No Wand"

            if (wandTextBig.isNullOrEmpty()) {
                wandTextBig = "N/A"
            }

            val wandBig = parentView.findViewById<TextView>(R.id.wandExtraBig)

            wandBig.text = "Wand: " + wandTextBig

            var actorTextBig = list[position].actor ?: "No Patronus"

            if (actorTextBig.isNullOrEmpty()) {
                actorTextBig = "N/A"
            }

            val actorBig = parentView.findViewById<TextView>(R.id.actorBig)

            actorBig.text = "Actor: " + actorTextBig

            parentView.findViewById<TextView>(R.id.nameNewBig).text =
                list[position].name ?: "No Name"

            val overlay = parentView.findViewById<FrameLayout>(R.id.overlayBig)
            overlay.visibility = View.VISIBLE

            backCardBig.setOnClickListener {

            }
            overlay.setOnClickListener {
                overlay.visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var photo = itemView.findViewById<ImageView>(R.id.photo)
        var name = itemView.findViewById<TextView>(R.id.name)
        var house = itemView.findViewById<TextView>(R.id.house)
        var ancestry = itemView.findViewById<TextView>(R.id.ancestry)
        var patronus = itemView.findViewById<TextView>(R.id.patronus)
        var wizard = itemView.findViewById<TextView>(R.id.wizard)
        var favBtn = itemView.findViewById<MaterialButton>(R.id.favoriteBtnLarge)
        var backCard = itemView.findViewById<MaterialCardView>(R.id.mainCard)
        var fullBack = itemView.findViewById<ImageView>(R.id.fullBackground)
    }
}