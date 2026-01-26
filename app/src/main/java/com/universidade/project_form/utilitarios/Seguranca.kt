package com.universidade.project_form.utilitarios

import java.security.MessageDigest

fun gerarHashSenha(senha: String): String {
    val bytes = MessageDigest
        .getInstance("SHA-256")
        .digest(senha.toByteArray())

    return bytes.joinToString("") { "%02x".format(it) }
}
