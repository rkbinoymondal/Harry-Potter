package com.example.harrypotter.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnalyticsDAO {

    @Insert
    suspend fun insertAnalytics(analytic: AnalyticsData)

    @Update
    suspend fun updateAnalytics(analytic: AnalyticsData)

    @Delete
    suspend fun deleteAnalytics(analytic: AnalyticsData)

    @Query("SELECT * FROM analyticsTable WHERE id = :id")
    suspend fun getAnalytics(id: String): AnalyticsData

    @Query("SELECT * FROM analyticsTable")
    fun getAnalyticsAll(): LiveData<List<AnalyticsData>>

    @Query("SELECT * FROM analyticsTable WHERE isSuccess IS true")
    fun getAnalyticsAllisSuccess(): LiveData<List<AnalyticsData>>

    @Query("SELECT * FROM analyticsTable WHERE isSuccess IS false")
    fun getAnalyticsAllisFailure(): LiveData<List<AnalyticsData>>
}