package com.example.harrypotter.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDAO {

    @Insert
    suspend fun insertFavorite(favorite: FavoriteData)

    @Update
    suspend fun updateFavorite(favorite: FavoriteData)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteData)

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getFavorite(id: String): FavoriteData

    @Query("SELECT * FROM favorite")
    fun getFavoriteAll(): LiveData<List<FavoriteData>>

    @Query("DELETE FROM favorite")
    suspend fun deleteFavoriteAll()
}