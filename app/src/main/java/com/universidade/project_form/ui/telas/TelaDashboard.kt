package com.universidade.project_form.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.universidade.project_form.modelos.Despesa

@Composable
fun TelaDashboard(despesas: List<Despesa>) {

    val totalGeral = despesas.sumOf { it.valor }

    val gastoPorCategoria = despesas.groupBy { it.categoria }
        .mapValues { it.value.sumOf { d -> d.valor } }

    val categoriaMaisConsumida = despesas.groupBy { it.categoria }
        .maxByOrNull { it.value.size }?.key ?: "N/A"

    val gastoPorDia = despesas.groupBy {
        java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(it.data))
    }.mapValues { it.value.sumOf { d -> d.valor } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium
            )
        }


        // TOTAL GERAL
        item {
            CardMetrica(
                titulo = "Total gasto",
                valor = "AO %.2f".format(totalGeral),
                icon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }

        // CATEGORIA MAIS CONSUMIDA
        item {
            CardMetrica(
                titulo = "Categoria mais consumida",
                valor = categoriaMaisConsumida,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }

        // GASTO POR CATEGORIA
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Gasto por categoria",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    gastoPorCategoria.forEach { (categoria, total) ->
                        Text(
                            "• $categoria: AO %.2f".format(total),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // GASTO POR DIA
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Gasto por dia",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    gastoPorDia.forEach { (dia, total) ->
                        Text(
                            "• $dia: AO %.2f".format(total),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


// ------------------------------------------------------------------ //

// para personalizar os cards
@Composable
fun CardMetrica(
    titulo: String,
    valor: String,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = valor,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            icon()
        }
    }
}
