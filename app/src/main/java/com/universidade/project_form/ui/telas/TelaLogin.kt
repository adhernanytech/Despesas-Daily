package com.universidade.project_form.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.universidade.project_form.ui.componentes.TelaBase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(
    aoFazerLogin: (email: String, palPasse: String) -> Unit,
    aoNavegarParaRegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var palPasse by remember { mutableStateOf("") }

    Scaffold(
        Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6650a4),
                        Color.White
                    )
                )
            ),
        topBar = {
            TopAppBar(
                title = { Text("Login") }
            )
        },

        ) { padding ->

        TelaBase {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF6650a4),
                                Color.White
                            )
                        )
                    )
                    .padding(35.dp),
                verticalArrangement = Arrangement.Center


            ) {

                Spacer(modifier = Modifier.height(60.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(25.dp))

                TextField(
                    value = palPasse,
                    onValueChange = { palPasse = it },
                    label = { Text("Palavra-passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { aoFazerLogin(email, palPasse) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Entrar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = aoNavegarParaRegistro) {
                    Text("Está sem conta? - Crie sua conta")
                }
            }
        }
    }
}
