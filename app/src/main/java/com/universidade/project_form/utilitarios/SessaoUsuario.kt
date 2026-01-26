package com.universidade.project_form.utilitarios

object SessaoUsuario {
    var usuarioId: Long? = null
    var nome: String? = null
    var email: String? = null

    fun logar(id: Long, nome: String, email: String) {
        usuarioId = id
        this.nome = nome
        this.email = email
    }

    fun logout() {
        usuarioId = null
        nome = null
        email = null
    }



    fun estaLogado(): Boolean = usuarioId != null
}