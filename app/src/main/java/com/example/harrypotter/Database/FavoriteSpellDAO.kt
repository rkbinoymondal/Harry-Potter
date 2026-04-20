package com.example.harrypotter.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteSpellDAO {

    @Insert
    suspend fun insertFavoriteSpell(favorite: FavoriteDataSpell)

    @Update
    suspend fun updateFavoriteSpell(favorite: FavoriteDataSpell)

    @Delete
    suspend fun deleteFavoriteSpell(favorite: FavoriteDataSpell)

    @Query("SELECT * FROM favoriteSpell WHERE id = :id")
    suspend fun getFavoriteSpell(id: String): FavoriteDataSpell

    @Query("SELECT * FROM favoriteSpell")
    fun getFavoriteAllSpell(): LiveData<List<FavoriteDataSpell>>

    @Query("DELETE FROM favoriteSpell")
    suspend fun deleteFavoriteSpellAll()
}