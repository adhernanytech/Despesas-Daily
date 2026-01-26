package com.universidade.project_form.navigation

// AUTHOR : Adilson Hernany

import TelaInfoSistema
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.universidade.project_form.dados.repository.DespesaRepository
import com.universidade.project_form.dados.repository.UsuarioRepository
import com.universidade.project_form.modelos.DespesaEntidade
import com.universidade.project_form.ui.BottomBar
import com.universidade.project_form.ui.componentes.DialogEditarDespesa
import com.universidade.project_form.ui.componentes.TelaBase
import com.universidade.project_form.ui.telas.*
import com.universidade.project_form.utilitarios.SessaoUsuario
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

sealed class Telas(
    val rota: String,
    val titulo: String,
    val icone: ImageVector
) {

    object Splash : Telas("splash", "", Icons.Default.Refresh)
    object Login : Telas("login", "Login", Icons.Default.Lock)
    object Usuarios : Telas("usuarios", "Usuários", Icons.Default.FmdBad)
    object Despesa : Telas("listar", "Despesas", Icons.Filled.Category)
    object Cadastrar : Telas("add_produto", "Cadastrar", Icons.Default.AddCircle)
    object Dashboard : Telas("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object Info : Telas("info", "Info", Icons.Default.Info)
    object Historico : Telas("hist", "Historico", Icons.Default.Abc)
}

@Composable
fun AppNavegacaoTelas(
    navController: NavHostController,
    despesaRepository: DespesaRepository,
    usuarioRepository: UsuarioRepository
) {
    val rotaActual = navController.currentBackStackEntryAsState().value?.destination?.route
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var despesaParaEditar by remember { mutableStateOf<DespesaEntidade?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (rotaActual != Telas.Login.rota &&
                rotaActual != Telas.Splash.rota &&
                rotaActual != Telas.Usuarios.rota
            ) BottomBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Telas.Splash.rota,
            modifier = Modifier.padding(paddingValues)
        ) {

            // -------------------- SPLASH --------------------
            composable(Telas.Splash.rota) {
                TelaSplash(navController = navController)
            }

            // -------------------- LOGIN --------------------
            composable(Telas.Login.rota) {
                TelaLogin(
                    aoFazerLogin = { email, senha ->
                        scope.launch {
                            val usuario = usuarioRepository.login(email, senha)
                            if (usuario != null) {
                                SessaoUsuario.logar(
                                    id = usuario.id,
                                    nome = usuario.nome,
                                    email = usuario.email
                                )
                                navController.navigate(Telas.Dashboard.rota) {
                                    popUpTo(Telas.Login.rota) { inclusive = true }
                                }
                            } else {
                                snackbarHostState.showSnackbar("Email ou senha inválidos")
                            }
                        }
                    },
                    aoNavegarParaRegistro = {
                        navController.navigate(Telas.Usuarios.rota)
                    }
                )
            }

            // -------------------- REGISTRO --------------------
            composable(Telas.Usuarios.rota) {
                TelaRegistroUsuario(
                    aoRegistrar = { nome, email, senha ->
                        scope.launch {
                            usuarioRepository.registrar(nome, email, senha)
                            snackbarHostState.showSnackbar("Conta criada com sucesso! Faça login.")
                            navController.navigate(Telas.Login.rota) {
                                popUpTo(Telas.Usuarios.rota) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // -------------------- DASHBOARD --------------------
            composable(Telas.Dashboard.rota) {
                val usuarioId = SessaoUsuario.usuarioId
                val despesas by if (usuarioId != null) {
                    despesaRepository.despesasDoUsuario(usuarioId)
                        .collectAsState(initial = emptyList())
                } else remember { mutableStateOf(emptyList<DespesaEntidade>()) }

                TelaBase(
                    aoLogout = {
                        SessaoUsuario.logout()
                        navController.navigate(Telas.Login.rota) { popUpTo(0) }
                    }
                ) {
                    TelaDashboard(
                        despesas = despesas,
                        navController = navController
                    )
                }
            }

            // -------------------- LISTAR DESPESAS --------------------
            composable(Telas.Despesa.rota) {
                val usuarioId = SessaoUsuario.usuarioId
                val despesas by if (usuarioId != null) {
                    despesaRepository.despesasDoUsuario(usuarioId)
                        .collectAsState(initial = emptyList())
                } else remember { mutableStateOf(emptyList<DespesaEntidade>()) }

                TelaBase(
                    aoLogout = {
                        SessaoUsuario.logout()
                        navController.navigate(Telas.Login.rota) { popUpTo(0) }
                    }
                ) {
                    TelaListarDespesa(
                        despesas = despesas,
                        aoClicarAdicionarDespesa = { navController.navigate(Telas.Cadastrar.rota) },
                        aoDeletarDespesa = { despesa ->
                            scope.launch {
                                despesaRepository.remover(
                                    despesa
                                )
                            }
                        },
                        aoEditarDespesa = { despesaParaEditar = it },
                        navController = navController
                    )
                }
            }

            // -------------------- CADASTRAR DESPESA --------------------
            composable(Telas.Cadastrar.rota) {
                TelaBase(
                    aoLogout = {
                        SessaoUsuario.logout()
                        navController.navigate(Telas.Login.rota) { popUpTo(0) }
                    }
                ) {
                    TelaCadastrarDespesa(
                        aoRegistrarDespesa = { despesa ->
                            scope.launch { despesaRepository.adicionar(despesa) }
                            navController.popBackStack()
                        },
                        aoVoltar = { navController.popBackStack() },
                        navController = navController
                    )
                }
            }

            // -------------------- INFO --------------------
            composable(Telas.Info.rota) {
                TelaBase(
                    aoLogout = {
                        SessaoUsuario.logout()
                        navController.navigate(Telas.Login.rota) { popUpTo(0) }
                    }
                ) {
                    TelaInfoSistema(navController = navController)
                }
            }

            // -------------------- HISTÓRICO --------------------
            composable(Telas.Historico.rota) {
                val usuarioId = SessaoUsuario.usuarioId
                val despesas by if (usuarioId != null) {
                    despesaRepository.despesasDoUsuario(usuarioId)
                        .collectAsState(initial = emptyList())
                } else remember { mutableStateOf(emptyList<DespesaEntidade>()) }

                TelaBase(
                    aoLogout = {
                        SessaoUsuario.logout()
                        navController.navigate(Telas.Login.rota) { popUpTo(0) }
                    }
                ) {
                    TelaHistoricoDespesas(
                        despesas = despesas,
                        navController = navController
                    )
                }
            }

        }

        // -------------------- DIALOG EDITAR --------------------
        despesaParaEditar?.let { despesa ->
            DialogEditarDespesa(
                despesa = despesa,
                aoConfirmar = { despesaAtualizada ->
                    scope.launch { despesaRepository.actualizar(despesaAtualizada) }
                    despesaParaEditar = null
                },
                aoCancelar = { despesaParaEditar = null }
            )
        }
    }
}
