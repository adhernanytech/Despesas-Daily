package com.universidade.project_form.dados.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.universidade.project_form.modelos.UsuarioEntidade

@Dao
interface UsuarioDao {

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun countUsuarios(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(usuario: UsuarioEntidade)

    @Query("""
        SELECT * FROM usuarios 
        WHERE email = :email AND senhaHash = :senhaHash
        LIMIT 1
    """)
    suspend fun login(email: String, senhaHash: String): UsuarioEntidade?

}
