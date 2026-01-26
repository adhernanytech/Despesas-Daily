package com.universidade.project_form.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universidade.project_form.dados.repository.UsuarioRepository
import com.universidade.project_form.modelos.UsuarioEntidade
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    var usuarioLogado by mutableStateOf<UsuarioEntidade?>(null)
        private set

    fun login(email: String, senha: String) {
        viewModelScope.launch {
            usuarioLogado = repository.login(email, senha)
        }
    }

    fun logout() {
        usuarioLogado = null
    }
}
