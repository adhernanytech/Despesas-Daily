package com.universidade.project_form.ui.telas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.universidade.project_form.modelos.Despesa

@Composable
fun DialogEditarProduto(
    despesa: Despesa,
    aoConfirmar: (Despesa) -> Unit,
    aoCancelar: () -> Unit
) {
    var nome by remember { mutableStateOf(despesa.nome) }
    var descricao by remember { mutableStateOf(despesa.descricao)}
    var preco by remember { mutableStateOf(despesa.valor.toString())}

    AlertDialog(
        onDismissRequest = aoCancelar,
        title = { Text("Editar Produto") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = nome,
                    onValueChange = {nome = it},
                    label = { Text("Nome") }
                )

                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") }
                )

                TextField(
                    value = preco,
                    onValueChange = { preco = it },
                    label = { Text("Preço") }
                )
            }
        },

        confirmButton = {
            TextButton(onClick = {
                aoConfirmar(
                    despesa.copy(
                        nome = nome,
                        descricao = descricao,
                        valor = preco.toDoubleOrNull() ?: despesa.valor
                    )
                )
            }) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = aoCancelar) {
                Text("Cancelar")
            }
        }
    )
}