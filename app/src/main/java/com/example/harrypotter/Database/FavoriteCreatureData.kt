package com.example.harrypotter.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteCreature")
data class FavoriteCreatureData(

    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String? = "No Name",
    val house: String? = "No House",
    val ancestry: String? = "No Ancestry",
    val patronus: String? = "No Patronus",
    val image: String? = "No Image",
    val wizard: Boolean? = false,
    val hogwartsStudent: Boolean? = false,
    val hogwartsStaff: Boolean? = false,
    val species: String? = "Not Known"
)
