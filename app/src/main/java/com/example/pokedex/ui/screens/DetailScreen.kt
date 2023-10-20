package com.example.pokedex.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.example.pokedex.common.entities.PokemonEntity
import com.example.pokedex.common.utils.formatId
import com.example.pokedex.common.utils.getImageById
import androidx.compose.runtime.LaunchedEffect
import com.example.pokedex.common.utils.Retrofit
import com.example.pokedex.common.utils.capitalizeFirstLetter

// Composable "Decoracion" - Permite a una funcion trabajar UI
// sin eso la funcion solo trabaja logica
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen( id: Int, onNavigation:() -> Unit) {

    var pokemonById by remember { mutableStateOf<PokemonEntity?>(null) }
    LaunchedEffect(id) {
        try {
            val response = Retrofit.service.getPokemonInfo(id.toString())
            pokemonById = response
        } catch (e: Exception) {
            Log.e("PokedexScreen", "Error: $e")
        }
    }

    Scaffold (
        //Barra superior
        topBar = {
            // La barra no se colocara en el centro, estara un espacio abajo del lado izquierdo
                 MediumTopAppBar(title = { Text(text = "🗒️Desplegando Información🗒️")},
                     navigationIcon = {
                         IconButton(onClick = { onNavigation() }) {
                             Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                         }
                     },
                     // Acciones para hacer, por ejemplo un menu desplegable
                     actions = {
                         IconButton(onClick = { }) {
                             Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                         }
                     })
        },
        content = {
            paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            {
                Content(pokemonEntity = pokemonById)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(pokemonEntity: PokemonEntity?)
{

    if(pokemonEntity == null)
    {
        Box(modifier = Modifier.fillMaxSize() )
        {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    else {
        Column {
            Box()
            {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    // Obtener ID
                    Text(text = formatId(pokemonEntity.id), style = MaterialTheme.typography.titleMedium)
                    // Obtener Nombre
                    Text(text = pokemonEntity.name.capitalizeFirstLetter(), style = MaterialTheme.typography.headlineMedium)
                    // Obtener Imagen
                    AsyncImage(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        model = com.example.pokedex.common.utils.getImageById(pokemonEntity.id),
                        contentDescription = null,
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                }
            }

            Column()
            {
                Row(
                    verticalAlignment = Alignment.Bottom
                )
                {
                    Text(text = "Información del Pokemon", style = MaterialTheme.typography.headlineSmall)
                }

                Row (
                    verticalAlignment = Alignment.Bottom
                ){
                    Text(text = "Este Pokemon mide: "+pokemonEntity.height.toString()+" metros", style = MaterialTheme.typography.bodyLarge)
                }

                Row (
                    verticalAlignment = Alignment.Bottom
                ){
                    Text(text = "El peso de "+pokemonEntity.name.capitalizeFirstLetter()+" es de:"+pokemonEntity.peso+" kilos!", style = MaterialTheme.typography.bodyLarge)
                }

                Row(
                    verticalAlignment = Alignment.Bottom
                )
                {
                    Text(text = "Este Pokemon es de tipo:", style = MaterialTheme.typography.titleMedium)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    )
                    {
                        items(pokemonEntity.types.size)
                        {
                            val type = pokemonEntity.types[it].type.name.capitalizeFirstLetter()
                            AssistChip(
                                onClick = { },
                                label = {
                                    Text(text = if (type != null) type else "Vacio")
                                })
                        }
                    }
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                )
                {
                    Text(text = "La experiencia base del pokemon es de:"+pokemonEntity.base_experience.toString()+" xp")
                }
            }


        }
    }
    
}