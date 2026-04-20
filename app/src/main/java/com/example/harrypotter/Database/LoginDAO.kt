package com.example.harrypotter.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LoginDAO {

    @Insert
    suspend fun insertLogin(login: LoginData)

    @Update
    suspend fun updateLogin(login: LoginData)

    @Delete
    suspend fun deleteLogin(login: LoginData)

    @Query("SELECT * FROM loginTable WHERE id = :id")
    suspend fun getLogin(id: String): LoginData

    @Query("SELECT * FROM loginTable")
    fun getLoginAll(): LiveData<List<LoginData>>

    @Query("DELETE FROM loginTable")
    suspend fun deleteLoginAll()
}