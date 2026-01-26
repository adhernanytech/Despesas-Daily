package com.universidade.project_form.ui.componentes

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
import com.universidade.project_form.modelos.DespesaEntidade

@Composable
fun DialogEditarDespesa(
    despesa: DespesaEntidade,
    aoConfirmar: (DespesaEntidade) -> Unit,
    aoCancelar: () -> Unit
) {
    var nome by remember { mutableStateOf(despesa.nome) }
    var descricao by remember { mutableStateOf(despesa.descricao)}
    var valor by remember { mutableStateOf(despesa.valor.toString()) }

    AlertDialog(
        onDismissRequest = aoCancelar,
        title = { Text("Editar despesa") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = nome,
                    onValueChange = {nome = it},
                    label = { Text("Despesa") }
                )

                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição / razão") }
                )

                TextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor") }
                )
            }
        },

        confirmButton = {
            TextButton(onClick = {
                aoConfirmar(
                    despesa.copy(
                        nome = nome,
                        descricao = descricao,
                        valor = valor.toDoubleOrNull() ?: despesa.valor
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