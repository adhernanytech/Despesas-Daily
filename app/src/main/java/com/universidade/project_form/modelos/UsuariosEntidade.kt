package com.universidade.project_form.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntidade(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val nome: String,
    val email: String,
    val senhaHash: String
)
