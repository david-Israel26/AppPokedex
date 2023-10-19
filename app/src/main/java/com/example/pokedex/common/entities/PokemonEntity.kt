package com.example.pokedex.common.entities

import com.google.gson.annotations.SerializedName
// Con SerializedName se puede cambiar el nombre a el elemento JSON y
// debajo se coloca el nombre personal

data class PokemonEntity(
    var id: Int,
    var name: String,
    var height: Int,
    @SerializedName("weight")
    var peso: Double,
    var types: List<Type>,
    var sprites: Sprites
)
