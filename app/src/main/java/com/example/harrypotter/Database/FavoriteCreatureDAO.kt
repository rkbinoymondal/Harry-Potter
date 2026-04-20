package com.example.harrypotter.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteCreatureDAO {

    @Insert
    suspend fun insertFavoriteCreature(favorite: FavoriteCreatureData)

    @Update
    suspend fun updateFavoriteCreature(favorite: FavoriteCreatureData)

    @Delete
    suspend fun deleteFavoriteCreature(favorite: FavoriteCreatureData)

    @Query("SELECT * FROM favoriteCreature WHERE id = :id")
    suspend fun getFavoriteCreature(id: String): FavoriteCreatureData

    @Query("SELECT * FROM favoriteCreature")
    fun getFavoriteAllCreature(): LiveData<List<FavoriteCreatureData>>

    @Query("DELETE FROM favoriteCreature")
    suspend fun deleteFavoriteCreatureAll()
}