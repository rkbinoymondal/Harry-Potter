package com.example.harrypotter.APIFetching

import com.example.harrypotter.Model.CharacterData
import com.example.harrypotter.Model.SpellData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://hp-api.onrender.com/"

interface APIFetching {

    @GET("api/characters")
    fun getAllCharacter(): Call<List<CharacterData>>

    @GET("api/spells")
    fun getAllSpell(): Call<List<SpellData>>
}

object RetrofitObject {
    val characterInstance: APIFetching

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        characterInstance = retrofit.create(APIFetching::class.java)
    }
}