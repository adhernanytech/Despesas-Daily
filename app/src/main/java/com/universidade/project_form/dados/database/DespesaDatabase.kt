package com.universidade.project_form.dados.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.universidade.project_form.dados.Converters
import com.universidade.project_form.dados.dao.DespesaDao
import com.universidade.project_form.dados.dao.UsuarioDao
import com.universidade.project_form.modelos.DespesaEntidade
import com.universidade.project_form.modelos.UsuarioEntidade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [DespesaEntidade::class, UsuarioEntidade::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class DespesaDatabase : RoomDatabase() {
    abstract fun despesaDao(): DespesaDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: DespesaDatabase? = null

        fun getDatabase(context: Context): DespesaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DespesaDatabase::class.java,
                    "despesas_db"
                )
                    .build()

                INSTANCE = instance



                /*
                // Inserir usuário padrão na primeira execução
                CoroutineScope(Dispatchers.IO).launch {
                    val usuarioDao = instance.usuarioDao()
                    if (usuarioDao.countUsuarios() == 0) {
                        usuarioDao.inserir(
                            UsuarioEntidade(
                                nome = "Admin",
                                email = "admin@gmail.com",
                                senhaHash = "admin123"
                            )
                        )

                    }
                }*/

                instance
            }
        }
    }
}
