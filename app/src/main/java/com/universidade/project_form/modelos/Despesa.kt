package com.universidade.project_form.modelos

data class Despesa (
    val id: Long = 0L,
    val nome: String,
    val categoria: String,
    val descricao: String, // a razao do gasto
    val valor: Double,
    val data: Long, //
    val imagem: String? = null
)

