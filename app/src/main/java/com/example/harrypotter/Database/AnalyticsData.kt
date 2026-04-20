package com.example.harrypotter.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analyticsTable")
data class AnalyticsData(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val APIname: String? = "No Name",
    val isSuccess: Boolean? = false,
    val responseTime: Long? = 0,
    val timeStamp: Long? = 0
)
