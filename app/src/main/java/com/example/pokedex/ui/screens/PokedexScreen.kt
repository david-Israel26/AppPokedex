package com.example.pokedex.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNodeLifecycleCallback
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
import com.example.pokedex.common.entities.Types
import com.example.pokedex.common.utils.Retrofit.service
import com.example.pokedex.common.utils.capitalizeFirstLetter
import com.example.pokedex.common.utils.formatId
import com.example.pokedex.common.utils.getImageById
import com.example.pokedex.ui.theme.PokedexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Funcion pokedex screen que nos pinta la pantalla
fun PokedexScreen(onNavigation: (Int) -> Unit) {
    // Con ? permitimos valores nulos - Esta linea permite la recomposicion
    // (cambiar el estado de la UI)
    // Lista de pokemones - Lista que va a cambiar su contenido
    val pokemonListState = remember { mutableStateOf<List<PokemonEntity>?>(null) }

    // Lista de pokemones
    val tempList = mutableListOf<PokemonEntity>()

    // Inicio de set
    val offset = remember {
        mutableStateOf(0)
    }

    // Limite o fin de set
    val limit = remember {
        mutableStateOf(20)
    }

    // Realiza la solicitud de los N pokemon/pokemones
    for (index in offset.value .. limit.value) {
        // Cuando el index cambia cambia el estado del programa
        // Se hace uso de Courtine o Threads o Hilos
        LaunchedEffect(index)
        {
            try {
                // Dentro de respuesta se hace uso del servicio de obtener la informacion del pokemon
                // de acuerdo a su index o ID
                val response = service.getPokemonInfo(index.toString())

                tempList.add(response)
                if (tempList.size == 20) {
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
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.DarkGray
                ),
                title = { Text(stringResource(id = R.string.app_name)) },
                )
        },
        content = {
                paddingValues ->
            // Creando mi propio composable - Composable siempre inicia con mayuscula
            // Aplicando modificadores
            Box(
                // Box toma completamente el ancho y alto de la pantalla
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // A la funcion de contenido principal se le pasa los valores de la lista pokemon
                MainContent(
                    listPokemon = pokemonListState.value,
                    callback =  {
                        // Incrementando pokemons
                        offset.value += 10
                        // Incrementando limite
                        limit.value += 10
                        // Para mostrar el circulo de carga
                        pokemonListState.value = null
                    },
                    onNavigation = {
                        onNavigation(it)
                    })
            }
        }
    )
}

// Mi composable
@Composable
fun MainContent(listPokemon: List<PokemonEntity>? ,
                callback: () -> Unit, onNavigation: (Int) -> Unit)
{
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
        // Lazy Column permite hacer scroll de la informacion
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                //Para el otro diseño, comenta esta linea
                //.verticalScroll(rememberScrollState())
        ) {

            // Aqui va el lazy vertical grid
            LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                columns = GridCells.Fixed(3),
                content = {
                    items(listPokemon.size)
                    {
                        CardPokemonItemGrid(pokemonEntity = listPokemon[it])
                    }
                }
            )

            /*
            listPokemon.forEach {
                CardPokemonItem(it) {
                    id ->
                    onNavigation(id)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            */

            /*
            Lazy Column sirve para solo mostrar listas u objetos, pero si se quiere añadir
            algo debajo no se va a poder por que lo va a cubrir
            LazyColumn(
                //Establece el contenido a toda la pantalla
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(25.dp),
                content = {
                    // Asegurando que la lista tenga un valor con !!
                    // Pasando los elementos
                    items(listPokemon.size)
                    {
                        // A la funcion CardPokemonItem pasale la lista de pokemones
                        CardPokemonItem(listPokemon[it])
                    }
                })
            */
            Button(onClick = { callback() }) {
                Text(text = "Siguiente")
            }

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Atras")
            }

        }
    }
}

// Mi funcion composable - 2
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPokemonItem(pokemonEntity: PokemonEntity, callback: (Int) -> Unit) {
    // Mostrando los elementos en fila
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { callback(pokemonEntity.id) },
    ) {
        // Column ocupa 3 espacios - Informacion de pokemon
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            // Se divide la columna en dos filas

            // Fila 1 - Parte superior
            Row(
                // Ocupa todo el espacio de la fila
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                // Separa lo que es el ID y el Nombre del pokemon
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Mostrando en pantalla el ID del pokemon
                Text(text = formatId(pokemonEntity.id))
                // Mostrando en pantalla el nombre del pokemon - haciendo uso de la funcion
                // capitalizeFirstLetter de Utils.kt
                Text(text = pokemonEntity.name.capitalizeFirstLetter())
            }

            // Fila 2 - Parte inferior
            Row(
                // Ocupa completamente el espacio de la fila
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                )
                {
                    // Obtiene el tamaño de los tipos pokemon - 1 item
                    items(pokemonEntity.types.size)
                    {
                        // Dentro ed la lista de type se obtiene de acuerdo a el item el nombre
                        // o nombre de tipo pokemon
                        val type = pokemonEntity.types[it].type.name.capitalizeFirstLetter()
                        // AssistChip es un cuadrito que aparecera en la interfaz
                        AssistChip(
                            // Al hacer clic en ella no hara nada
                            onClick = { },
                            // El texto que contendra sera
                            label = {
                                // Dentro del contenido si el elemento es diferente de nullo
                                // Escribe el nombre o tipo de pokemon
                                // De lo contrario escribe "Vacio"
                                // Operador ternario
                                Text(text = if (type != null) type else "Vacio -")
                            })
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
            // Funcion publica que obtiene la imagen
            AsyncImage(
                // Ocupa completamente el espacio disponible
                modifier = Modifier.fillMaxSize(),
                // modelo hara uso de la funcion de obtener la imagen por ID
                // haciendo uso del parametro de ID
                model = getImageById(pokemonEntity.id),
                // No tendra descripcion
                contentDescription = null,
                // La imagen se ve un poco mas grande
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Mi funcion composable - 3 Otro diseño
@Composable
fun CardPokemonItemGrid(pokemonEntity: PokemonEntity)
{
    // Caja de fondo
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .size(350.dp)
    )
    {
        // Columna
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            // Mostrando el ID del pokemon
            Text(text = formatId(pokemonEntity.id))

            //Mostrando la imagen del pokemon
            AsyncImage(
                // Ocupa completamente el espacio disponible
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                // modelo hara uso de la funcion de obtener la imagen por ID
                // haciendo uso del parametro de ID
                model = getImageById(pokemonEntity.id),
                // No tendra descripcion
                contentDescription = null,
                // La imagen se ve un poco mas grande
                contentScale = ContentScale.Fit
            )

            // Mostrando el Nombre del pokemon
            Text(text = pokemonEntity.name.capitalizeFirstLetter())
        }
    }
}

// Funcion de prueba para visualizar el armado del pokemon
/*
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
                Types(
                    type = Type(
                        name = "grass"
                    )
                )
            ),
            sprites = Sprites(
                image = ""
            )
        )
        CardPokemonItem(pokemonEntity = pokemonEntity)
    }
}
*/