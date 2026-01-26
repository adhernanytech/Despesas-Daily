package com.universidade.project_form.dados.repository

import com.universidade.project_form.dados.dao.UsuarioDao
import com.universidade.project_form.modelos.UsuarioEntidade
import com.universidade.project_form.utilitarios.gerarHashSenha

class UsuarioRepository(
    private val dao: UsuarioDao
) {

    suspend fun registrar(nome: String, email: String, senha: String) {
        dao.inserir(
            UsuarioEntidade(
                nome = nome,
                email = email,
                senhaHash = gerarHashSenha(senha)
            )
        )
    }

    suspend fun login(email: String, senha: String): UsuarioEntidade? {
        return dao.login(email, gerarHashSenha(senha))
    }

}
