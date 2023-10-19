package com.example.pokedex.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

// Composable "Decoracion" - Permite a una funcion trabajar UI
// sin eso la funcion solo trabaja logica
@Composable
fun DetailScreen( id: Int) {
    Text(text = id.toString())
}