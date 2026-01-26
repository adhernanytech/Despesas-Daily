package com.universidade.project_form.ui.telas

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.universidade.project_form.modelos.DespesaEntidade
import com.universidade.project_form.navigation.Telas
import com.universidade.project_form.ui.componentes.GraficoCategorias
import com.universidade.project_form.ui.componentes.TelaBase
import com.universidade.project_form.ui.componentes.TopAppLogout
import com.universidade.project_form.utilitarios.SessaoUsuario
import java.text.SimpleDateFormat
import java.util.*

enum class PeriodoFiltro {
    DIA, SEMANA, MES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHistoricoDespesas(
    despesas: List<DespesaEntidade>,
    navController: NavController
) {

    var filtroSelecionado by remember { mutableStateOf(PeriodoFiltro.DIA) }
    var categoriaSelecionada by remember { mutableStateOf<String?>(null) }

    val hoje = Calendar.getInstance()

    val gastoPorCategoria = despesas.groupBy { it.categoria }
        .mapValues { it.value.sumOf { d -> d.valor } }

    // Filtra por período
    val despesasFiltradasPeriodo = despesas.filter { despesa ->
        val dataDespesa = Calendar.getInstance().apply { timeInMillis = despesa.dataregisto }
        when (filtroSelecionado) {
            PeriodoFiltro.DIA ->
                dataDespesa.get(Calendar.YEAR) == hoje.get(Calendar.YEAR) &&
                        dataDespesa.get(Calendar.DAY_OF_YEAR) == hoje.get(Calendar.DAY_OF_YEAR)
            PeriodoFiltro.SEMANA ->
                dataDespesa.get(Calendar.YEAR) == hoje.get(Calendar.YEAR) &&
                        dataDespesa.get(Calendar.WEEK_OF_YEAR) == hoje.get(Calendar.WEEK_OF_YEAR)
            PeriodoFiltro.MES ->
                dataDespesa.get(Calendar.YEAR) == hoje.get(Calendar.YEAR) &&
                        dataDespesa.get(Calendar.MONTH) == hoje.get(Calendar.MONTH)
        }
    }

    // Lista de categorias disponíveis no período
    val categorias = despesasFiltradasPeriodo.map { it.categoria }.distinct()

    // Filtra por categoria selecionada
    val despesasFiltradas = if (categoriaSelecionada != null) {
        despesasFiltradasPeriodo.filter { it.categoria == categoriaSelecionada }
    } else despesasFiltradasPeriodo

    val totalPeriodo = despesasFiltradas.sumOf { it.valor }

    val despesasAgrupadasPorData = despesasFiltradas.groupBy {
        SimpleDateFormat("dd/MM/yyyy").format(Date(it.dataregisto))
    }.toSortedMap(reverseOrder()) //

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Gastos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        TelaBase (
            aoLogout = {
                SessaoUsuario.logout()
                navController.navigate(Telas.Login.rota) { popUpTo(0) }
            }
        ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    // 🔹 FILTRO POR PERÍODO
                    TabRow(selectedTabIndex = filtroSelecionado.ordinal) {
                        PeriodoFiltro.values().forEach { periodo ->
                            Tab(
                                selected = filtroSelecionado == periodo,
                                onClick = { filtroSelecionado = periodo },
                                text = {
                                    Text(
                                        when (periodo) {
                                            PeriodoFiltro.DIA -> "Dia"
                                            PeriodoFiltro.SEMANA -> "Semana"
                                            PeriodoFiltro.MES -> "Mês"
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

                //  FILTRO POR PERÍODO
                item {
                    TabRow(selectedTabIndex = filtroSelecionado.ordinal) {
                        PeriodoFiltro.values().forEach { periodo ->
                            Tab(
                                selected = filtroSelecionado == periodo,
                                onClick = { filtroSelecionado = periodo },
                                text = {
                                    Text(
                                        when (periodo) {
                                            PeriodoFiltro.DIA -> "Dia"
                                            PeriodoFiltro.SEMANA -> "Semana"
                                            PeriodoFiltro.MES -> "Mês"
                                        }
                                    )
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // FILTRO POR CATEGORIA
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categorias.forEach { cat ->
                            FilterChip(
                                selected = categoriaSelecionada == cat,
                                onClick = {
                                    categoriaSelecionada = if (categoriaSelecionada == cat) null else cat
                                },
                                label = { Text(cat) }
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }

                //  GRÁFICO DE BARRAS
                item {
                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Text("Gráfico de gastos por categoria")
                            Spacer(Modifier.height(20.dp))
                            GraficoCategorias(gastoPorCategoria)
                        }
                    }
                }


                // 🔹 TOTAL DO PERÍODO
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Total do período: AO %.2f".format(totalPeriodo),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                //  LISTA AGRUPADA POR DATA
                despesasAgrupadasPorData.forEach { (data, lista) ->
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Text(
                                    text = data,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(Modifier.height(8.dp))

                                lista.forEach { despesa ->
                                    Text(
                                        "• ${despesa.nome} - AO %.2f".format(despesa.valor),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}
