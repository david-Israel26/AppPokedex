package com.example.pokedex.common.utils

import com.example.pokedex.common.dataAccess.PokemonService
import com.example.pokedex.common.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Con la clase/objeto kotlin el objeto puede ser leida sin necesidad de un objeto de clase
object Retrofit {
    private val retrotif = Retrofit.Builder()
        //Obtiene la URL
        .baseUrl(BASE_URL)
        //Se accede a el objeto del pokemon
        .addConverterFactory(GsonConverterFactory.create())
        //Retorna el valor
        .build()
    val service: PokemonService = retrotif.create(PokemonService::class.java)
}