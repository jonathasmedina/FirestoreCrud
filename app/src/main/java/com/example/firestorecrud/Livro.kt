package com.example.firestorecrud

data class Livro(
    var id: String? = null, //pode ser nulo pois na criacao do objeto é nulo. firestore cria um id e atribui
    var titulo: String? = null,
    var autor: String? = null,
    var ano: Int? = null
)