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
import androidx.navigation.NavController
import com.universidade.project_form.navigation.Telas
import com.universidade.project_form.modelos.DespesaEntidade
import com.universidade.project_form.ui.componentes.CardMetrica
import com.universidade.project_form.ui.componentes.TelaBase
import com.universidade.project_form.utilitarios.SessaoUsuario

@Composable
fun TelaDashboard(
    despesas: List<DespesaEntidade>,
    navController: NavController
) {

    val totalGeral = despesas.sumOf { it.valor }

    val gastoPorCategoria = despesas.groupBy { it.categoria }
        .mapValues { it.value.sumOf { d -> d.valor } }

    val topCategorias = gastoPorCategoria
        .toList()
        .sortedByDescending { it.second }
        .take(3)


    val categoriaMaisConsumida = despesas.groupBy { it.categoria }
        .maxByOrNull { it.value.size }?.key ?: "N/A"

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- //

    TelaBase (
        aoLogout = {
            SessaoUsuario.logout()
            navController.navigate(Telas.Login.rota) {
                popUpTo(0)
            }
        }
    ) {
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

            item {
                Card () {
                    Column(
                        Modifier.padding(16.dp),

                        ) {
                        Text("Top categorias")
                        Spacer(Modifier.height(10.dp))
                        topCategorias.forEachIndexed { index, item ->
                            Text("${index + 1}. ${item.first} – AO %.2f".format(item.second))
                            Spacer(Modifier.height(3.dp))

                        }
                    }
                }
            }


            item {
                Button(
                    onClick = {
                        navController.navigate(Telas.Historico.rota)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver histórico detalhado")
                }
            }
        }
    }
}



