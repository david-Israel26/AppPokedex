package com.example.pokedex.common.navigation

sealed class Routes(val route: String)
{
    // Objetos para acceder a las clases
    data object PokedexScreen : Routes( "pokedex")
    data object DetailScreen : Routes( "detail")
}
