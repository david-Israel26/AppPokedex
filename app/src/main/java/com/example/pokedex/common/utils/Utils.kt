package com.example.pokedex.common.utils

// Retorna la URL a la cual le pasamos un ID
fun getImageById(id: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
}

// Pasamos ID y lo retorna en un signo de gato y tres decimales
fun formatId(number: Int): String {
    // Usar la función "String.format" para formatear el número con ceros a la izquierda
    return String.format("#%03d", number)
}

// Funcion de extension, la primera letra la pone en mayuscula y la segunda en minuscula
fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) {
        return this
    }
    return substring(0, 1).toUpperCase() + substring(1)
}