package com.example.pokedex.common.dataAccess

import com.example.pokedex.common.entities.PokemonEntity
import retrofit2.http.GET
import retrofit2.http.Path

// Define la funcion y lo que se tenga que regresar o no
interface PokemonService {
    // Peticion GET
    @GET("pokemon/{id}")
    suspend fun getPokemonInfo(@Path("id") id: String): PokemonEntity
}