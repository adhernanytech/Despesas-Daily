package com.universidade.project_form.dados.repository


import android.util.Log
import com.universidade.project_form.dados.dao.DespesaDao
import com.universidade.project_form.modelos.DespesaEntidade
import kotlinx.coroutines.flow.Flow

class DespesaRepository(private val dao: DespesaDao) {

    fun despesasDoUsuario(usuarioId: Long): Flow<List<DespesaEntidade>> {
        return dao.getDespesasDoUsuario(usuarioId)
    }

    suspend fun adicionar(despesa: DespesaEntidade) {
        dao.inserir(despesa)
        Log.d("DB_TESTE", "Despesa inserida: $despesa")
    }

    suspend fun actualizar(despesa: DespesaEntidade) =
        dao.atualizar(despesa)

    suspend fun remover(despesa: DespesaEntidade) =
        dao.deletar(despesa)
}
