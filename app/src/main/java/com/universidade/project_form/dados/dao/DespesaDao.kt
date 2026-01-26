package com.universidade.project_form.dados.dao

import androidx.room.*
import com.universidade.project_form.modelos.DespesaEntidade
import kotlinx.coroutines.flow.Flow

@Dao
interface DespesaDao {

    @Query("""
        SELECT * FROM despesas 
        WHERE usuarioId = :usuarioId
        ORDER BY dataregisto DESC
    """)
    fun getDespesasDoUsuario(usuarioId: Long): Flow<List<DespesaEntidade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(despesa: DespesaEntidade)

    @Update
    suspend fun atualizar(despesa: DespesaEntidade)

    @Delete
    suspend fun deletar(despesa: DespesaEntidade)
}
