package com.universidade.project_form

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.universidade.project_form.dados.database.DespesaDatabase
import com.universidade.project_form.dados.repository.DespesaRepository
import com.universidade.project_form.dados.repository.UsuarioRepository

import com.universidade.project_form.navigation.AppNavegacaoTelas

class MainActivity : ComponentActivity() {

    lateinit var despesaRepository: DespesaRepository
    lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instancia o banco de dados usando singleton
        val db = DespesaDatabase.getDatabase(this)

        // Instancia os repositorios usando o DAO do banco
        despesaRepository = DespesaRepository(db.despesaDao())
        usuarioRepository = UsuarioRepository(db.usuarioDao())

        setContent {
            val navController = rememberNavController()
            AppNavegacaoTelas(
                navController = navController,
                despesaRepository,
                usuarioRepository
            )
        }
    }
}
