
package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Produto::class, Endereco::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun produtoDao(): ProdutoDAO

    abstract fun enderecoDao(): EnderecoDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance

            } else {
                synchronized(this) {

                    val instanciaAtual = INSTANCE

                    if (instanciaAtual != null) {
                        return instanciaAtual

                    } else {
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "mercado_preso_database"
                        ).build()

                        INSTANCE = instance

                        return instance
                    }
                }
            }
        }
    }
}
