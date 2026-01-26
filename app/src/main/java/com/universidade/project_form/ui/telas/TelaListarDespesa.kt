package com.universidade.project_form.ui.telas

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.universidade.project_form.modelos.DespesaEntidade
import com.universidade.project_form.navigation.Telas
import com.universidade.project_form.ui.componentes.TelaBase
import com.universidade.project_form.utilitarios.SessaoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListarDespesa(
    navController: NavHostController,
    despesas: List<DespesaEntidade>,
    aoClicarAdicionarDespesa: () -> Unit,
    aoDeletarDespesa: (DespesaEntidade) -> Unit,
    aoEditarDespesa: (DespesaEntidade) -> Unit,
) {

    // Bloqueia o back button físico
    BackHandler(enabled = true) { }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Despesas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = aoClicarAdicionarDespesa) {
                Text("+")
            }
        }
    ) { padding ->

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
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(despesas) { despesa ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {


                            Row(modifier = Modifier.weight(1f)) {

                                // IMAGEM (opcional)
                                if (despesa.imagem != null) {
                                    AsyncImage(
                                        model = despesa.imagem,
                                        contentDescription = "Imagem da despesa",
                                        modifier = Modifier
                                            .size(90.dp)
                                            .padding(end = 12.dp)

                                    )
                                }


                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = despesa.nome,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = despesa.categoria,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = despesa.descricao,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Valor: AO %.2f".format(despesa.valor),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }


                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(onClick = { aoEditarDespesa(despesa) }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(onClick = { aoDeletarDespesa(despesa) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Apagar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

