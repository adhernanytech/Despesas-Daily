@file:OptIn(ExperimentalMaterial3Api::class)

package com.universidade.project_form.ui.telas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.universidade.project_form.modelos.Categoria
import com.universidade.project_form.modelos.DespesaEntidade
import com.universidade.project_form.navigation.Telas
import com.universidade.project_form.ui.componentes.TelaBase
import com.universidade.project_form.utilitarios.SessaoUsuario
import kotlinx.coroutines.launch

@Composable
fun TelaCadastrarDespesa(
    aoRegistrarDespesa: (DespesaEntidade) -> Unit,
    aoVoltar: () -> Unit,
    navController: NavHostController
) {

    // ----- ESTADOS -----
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf<Categoria?>(null) }
    var descricao by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<String?>(null) }

    // Dropdown
    var categoriaExpanded by remember { mutableStateOf(false) }

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Launcher para selecionar imagem
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagemUri = uri?.toString()
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Despesa") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        TelaBase(
            aoLogout = {
                SessaoUsuario.logout()
                navController.navigate(Telas.Login.rota) {
                    popUpTo(0)
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),

                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                // ========== IMAGEM (opcional) ==========
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imagemUri == null) {
                        Text("Clique para adicionar imagem")
                    } else {
                        AsyncImage(
                            model = imagemUri,
                            contentDescription = "Imagem da despesa",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ========== NOME ==========
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da despesa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ========== CATEGORIA ==========
                ExposedDropdownMenuBox(
                    expanded = categoriaExpanded,
                    onExpandedChange = { categoriaExpanded = it }
                ) {

                    OutlinedTextField(
                        value = categoria?.mostrarNome ?: "Selecione a categoria",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoria") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoriaExpanded,
                        onDismissRequest = { categoriaExpanded = false }
                    ) {
                        Categoria.values().forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.mostrarNome) },
                                onClick = {
                                    categoria = item
                                    categoriaExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ========== DESCRIÇÃO ==========
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição / Razão do gasto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ========== VALOR ==========
                OutlinedTextField(
                    value = valor,
                    onValueChange = { valor = it },
                    label = { Text("Valor (AO)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))


                Button(
                    onClick = {
                        when {
                            nome.isBlank() -> {
                                scope.launch { snackbarHostState.showSnackbar("Nome não pode estar vazio") }
                            }
                            categoria == null -> {
                                scope.launch { snackbarHostState.showSnackbar("Selecione uma categoria") }
                            }
                            valor.toDoubleOrNull() == null -> {
                                scope.launch { snackbarHostState.showSnackbar("Valor inválido") }
                            }
                            else -> {
                                val usuarioId = SessaoUsuario.usuarioId

                                if (usuarioId == null) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Sessão expirada. Faça login novamente.")
                                    }
                                    aoVoltar()
                                    return@Button
                                }

                                aoRegistrarDespesa(
                                    DespesaEntidade(
                                        nome = nome,
                                        categoria = categoria!!.mostrarNome,
                                        descricao = descricao,
                                        valor = valor.toDouble(),
                                        dataregisto = System.currentTimeMillis(),
                                        imagem = imagemUri,
                                        usuarioId = usuarioId
                                    )
                                )


                                scope.launch { snackbarHostState.showSnackbar("Despesa cadastrada com sucesso!") }
                                // limpar campos
                                nome = ""
                                categoria = null
                                descricao = ""
                                valor = ""
                                imagemUri = null

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Cadastrado com sucesso!",
                                        duration = SnackbarDuration.Short
                                    )
                                }

                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cadastrar")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Botão para limpar os campos
                OutlinedButton(
                    onClick = {
                        // limpar campos
                        nome = ""
                        categoria = null
                        descricao = ""
                        valor = ""
                        imagemUri = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Limpar")
                }
            }
        }
    }
}
