package com.universidade.project_form.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.universidade.project_form.Telas

@Composable
fun BottomBar(controladorNav: NavHostController) {

    val telas = listOf(
        Telas.Despesa,
        Telas.Cadastrar,
        Telas.Dashboard,
        Telas.Info
    )

    // Recupera a rota atual
    val rotaAtual = controladorNav.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {

        telas.forEach { tela ->

            // Define se o botão está selecionado
            val selecionado = rotaAtual == tela.rota

            NavigationBarItem(
                selected = selecionado,
                onClick = {
                    controladorNav.navigate(tela.rota) {
                        // Evita criar múltiplas instâncias da mesma tela
                        launchSingleTop = true
                        // Mantém o estado da tela
                        restoreState = true
                        // PopUp até a tela inicial da BottomBar, salvando estado
                        popUpTo(Telas.Despesa.rota) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = tela.icone,
                        contentDescription = tela.titulo
                    )
                },
                label = {
                    Text(
                        tela.titulo
                    )
                }
            )
        }
    }
}
