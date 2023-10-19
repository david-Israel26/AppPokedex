package com.example.pokedex.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.ui.screens.DetailScreen
import com.example.pokedex.ui.screens.PokedexScreen

@Composable
fun Navigation() {
    // Val - Var para las funciones
    // Val solo lectura no permite ser modificada
    // Var si permite la modificacion
    val navController = rememberNavController()
    // Nav Host le damos el control de navegacion y la ruta principal
    NavHost(
        navController = navController,
        startDestination = Routes.PokedexScreen.route
    )
    {
        // composable define partes de una pantalla
        composable(Routes.PokedexScreen.route) { PokedexScreen()
            {
                id ->
                navController.navigate(Routes.DetailScreen.createRoute(id))
            }
        }

        composable(Routes.DetailScreen.route, arguments = listOf(
            navArgument("id")
            {
                type = NavType.IntType
            }
        )) { DetailScreen( it.arguments?.getInt("id")?:0) }
    }
}