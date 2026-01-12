package com.universidade.project_form

// AUTHOR : Adilson Hernany

import TelaInfoSistema

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.universidade.project_form.modelos.Despesa
import com.universidade.project_form.ui.telas.TelaCadastrarDespesa
import com.universidade.project_form.ui.telas.TelaListarDespesa
import com.universidade.project_form.ui.telas.TelaLogin
import com.universidade.project_form.ui.BottomBar
import com.universidade.project_form.ui.telas.DialogEditarProduto
import com.universidade.project_form.ui.telas.TelaDashboard
import com.universidade.project_form.ui.telas.TelaSplash
import kotlinx.coroutines.launch

sealed class Telas(
    val rota: String,
    val titulo: String,
    val icone: ImageVector) {

    object  Splash: Telas (
        rota = "splash",
        titulo = "",
        icone = Icons.Default.Refresh
    )
    object Login : Telas (
        rota = "login",
        titulo = "Login",
        icone = Icons.Default.Lock
    )

    object Despesa : Telas(
        rota = "listar",
        titulo = "Despesas",
        icone = Icons.Filled.Category
    )
    object Cadastrar : Telas(

        rota = "add_produto",
        titulo = "Cadastrar",
        icone = Icons.Default.AddCircle
    )

    object Dashboard : Telas (
        rota = "dashboard",
        titulo = "Dashboard",
        icone = Icons.Filled.Dashboard
    )

    object Info : Telas(
        rota = "info",
        titulo = "Info",
        icone = Icons.Default.Info
    )

}

@Composable
fun AppNavegacaoTelas(controladorNav: NavHostController) {

    val rotaActual =
        controladorNav.currentBackStackEntryAsState().value?.destination?.route


    // lista produtos
    var listaDespesas by remember { mutableStateOf<List<Despesa>>(emptyList()) }

    var despesaParaEditar by remember { mutableStateOf<Despesa?>(null) }

    val emailAdmin = "admin@gmail.com"
    val palPasseAdmin = "admin123"

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (rotaActual != Telas.Login.rota && rotaActual != Telas.Splash.rota)
                BottomBar(controladorNav)
        }
    ) { paddingValues ->

        NavHost(
            navController = controladorNav,
            startDestination = Telas.Splash.rota, //
            modifier = Modifier.padding(paddingValues)
        ) {

            // TELA DE COBERTURA - SPLASH
            composable(Telas.Splash.rota) {
                TelaSplash {
                    controladorNav.navigate(Telas.Login.rota) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }

            // TELA DE LOGIN
            composable(Telas.Login.rota) {
                TelaLogin(
                    aoFazerLogin = { email, palPasse ->
                        // login com sucesso
                        if (email == emailAdmin && palPasse == palPasseAdmin) {
                            controladorNav.navigate(Telas.Despesa.rota) {
                                popUpTo(Telas.Login.rota) {
                                    inclusive = true
                                }
                            }
                        } else {
                            // MOSTRA SNACKBAR
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Credenciais inválidas!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    aoNavegarParaRegistro = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "BY ADILSON HERNANY - COMPUTAÇÃO",

                            )
                        }
                    }
                )
            }

            // TELA DE DASHBOARD
            composable(Telas.Dashboard.rota) {
                TelaDashboard(despesas = listaDespesas) // listaDespesas é a sua lista de produtos/ despesas
            }


            // TELA DE LISTAGEM DE PRODUTOS
            composable (Telas.Despesa.rota) {

                TelaListarDespesa (
                    despesas = listaDespesas,
                    aoClicarAdicionarDespesa = {
                        controladorNav.navigate(Telas.Cadastrar.rota)
                    },
                    aoDeletarDespesa = { produto ->
                        listaDespesas = listaDespesas.filter { it.id != produto.id }
                    },
                    aoEditarDespesa = { produto ->
                        despesaParaEditar = produto
                    }
                )

            }

            // TELA DE CADASTRAR PRODUTO
            composable(Telas.Cadastrar.rota) {
                TelaCadastrarDespesa(
                    aoRegistrarDespesa = { produto ->
                        listaDespesas = listaDespesas + produto
                        controladorNav.popBackStack()
                    },
                    aoVoltar = { controladorNav.popBackStack() }
                )
            }

            // TELA DE INFO
            composable(Telas.Info.rota) { TelaInfoSistema() }


        }

        despesaParaEditar?.let { produto ->
            DialogEditarProduto(
                despesa = produto,
                aoConfirmar = { produtoAtualizado ->
                    listaDespesas = listaDespesas.map {
                        if (it.id == produtoAtualizado.id) produtoAtualizado else it
                    }
                    despesaParaEditar = null
                },
                aoCancelar = {
                    despesaParaEditar = null
                }
            )
        }

    }
}
