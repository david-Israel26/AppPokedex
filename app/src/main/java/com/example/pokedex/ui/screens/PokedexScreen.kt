package com.example.pokedex.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokedex.Greeting
import com.example.pokedex.R
import com.example.pokedex.common.entities.PokemonEntity
import com.example.pokedex.common.entities.Sprites
import com.example.pokedex.common.entities.Type
import com.example.pokedex.common.utils.Retrofit.service
import com.example.pokedex.common.utils.capitalizeFirstLetter
import com.example.pokedex.common.utils.formatId
import com.example.pokedex.common.utils.getImageById
import com.example.pokedex.ui.theme.PokedexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Funcion pokedex screen
fun PokedexScreen() {
    // Con ? permitimos valores nulos - Esta linea permite la recomposicion
    // (cambiar el estado de la UI)
    // Lista de pokemones - Lista que va a cambiar su contenido
    val pokemonListState = remember { mutableStateOf<List<PokemonEntity>?>(null) }

    // Lista de pokemones
    val tempList = mutableListOf<PokemonEntity>()

    // Realiza la solicitud de los N pokemon/pokemones
    for (index in 1..50) {
        // Cuando el index cambia cambia el estado del programa
        // Se hace uso de Courtine o Threads o Hilos
        LaunchedEffect(index)
        {
            try {
                val response = service.getPokemonInfo(index.toString())
                tempList.add(response)
                if (tempList.size == 50) {
                    // Actualizar el elemento de la variable
                    // Ordenando los IDs por orden
                    pokemonListState.value = tempList.toList().sortedBy { it.id }
                }
            } catch (e: Exception) {
                Log.e("PokedexScreen", "Error: $e")
            }
        }
    }

    // Diseño de la UI
    // Composable Scaffold - Propiedades de temas
    Scaffold(
        // Barra superior
        topBar = {
            //Centrar elementos de TopBar ( titulo - Text para pintar textos (pide un texto que es
            // el nombre de la app)
            CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
        content = { paddingValues ->
            // Creando mi propio composable - Composable siempre inicia con mayuscula
            // Aplicando modificadores
            Box(
                // Box toma completamente el ancho y alto de la pantalla
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MainContent(pokemonListState.value)
            }
        }
    )
}

// Mi composable
@Composable
fun MainContent(listPokemon: List<PokemonEntity>?) {
    if (listPokemon == null) {
        // Composable para indicar que esta cargando
        // Colocando el circulo de carga en medio de la pantalla
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Pintar los datos cargados
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                // Asegurando que la lista tenga un valor con !!
                // Pasando los elementos
                items(listPokemon.size) {
                    CardPokemonItem(listPokemon[it])
                }
            })
    }
}

// Mi funcion composable - 2
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPokemonItem(pokemonEntity: PokemonEntity) {
    // Mostrando los elementos
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        // Column ocupa 3 espacios - Informacion de pokemon
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
        ) {
            // Se divide la columna en dos filas

            // Fila 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Asignando el primer texto - ID con formato
                Text(text = formatId(pokemonEntity.id))
                Text(text = pokemonEntity.name.capitalizeFirstLetter())
            }

            // Fila 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                )
                {
                    items(pokemonEntity.types.size)
                    {
                        val type = pokemonEntity.types[it].name
                        AssistChip(onClick = { },
                            label = { Text(text = if (type != null) type else "") })
                    }
                }
            }
        }
        // Box ocupa un espacio - Imagen del pokemon
        Box(
            modifier = Modifier
                .weight(1f)
                // Restringiendo la altura
                .fillMaxHeight()
                // 0f - Transparencia(dos valores FF) - Color en exadecimal
                .background(
                    Color(0xFFf0f0f0), shape = RoundedCornerShape(
                        topStart = 64.dp,
                        bottomStart = 64.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        ) {
            AsyncImage(
                // Ocupa completamente el espacio disponible
                modifier = Modifier.fillMaxSize(),
                model = getImageById(pokemonEntity.id),
                contentDescription = null,
                // La imagen se ve un poco mas grande
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexTheme {
        val pokemonEntity = PokemonEntity(
            id = 1,
            name = "Bulbasaur",
            height = 7,
            peso = 84.0,
            types = listOf(
                Type(
                    name = "Grass",
                )
            ),
            sprites = Sprites(
                image = ""
            )
        )
        CardPokemonItem(pokemonEntity = pokemonEntity)
    }
}
