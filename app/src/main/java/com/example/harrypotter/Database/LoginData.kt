package com.example.harrypotter.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loginTable")
data class LoginData(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String? = "N/A",
    val house: String? = "N/A",
    val wand: String? = "N/A",
    val patronus: String? = "N/A",
    val role: String? = "N/A"
)
