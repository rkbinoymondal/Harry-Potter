package com.example.harrypotter.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteSpell")
data class FavoriteDataSpell(

    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String? = "No Name",
    val description: String? = "No Description"
)
