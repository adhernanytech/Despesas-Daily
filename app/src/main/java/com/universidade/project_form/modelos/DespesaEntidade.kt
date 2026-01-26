package com.universidade.project_form.modelos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "despesas",
    foreignKeys = [ForeignKey(
        entity = UsuarioEntidade::class,
        parentColumns = ["id"],
        childColumns = ["usuarioId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DespesaEntidade(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val categoria: String,
    val descricao: String,
    val valor: Double,
    val dataregisto: Long,
    val usuarioId: Long,  // não pode ser null
    val imagem: String? = null
)
