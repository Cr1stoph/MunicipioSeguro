package com.duoc.municipioseguro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UsuarioEntity::class], version = 1, exportSchema = false)
abstract class MunicipioDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: MunicipioDatabase? = null

        fun getDatabase(context: Context): MunicipioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MunicipioDatabase::class.java,
                    "municipio_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        prepoblarDatabase(database.usuarioDao())
                    }
                }
            }

            suspend fun prepoblarDatabase(usuarioDao: UsuarioDao) {
                usuarioDao.insertarUsuario(
                    UsuarioEntity(
                        correo = "admin@sanjoaquin.cl",
                        contrasena = "admin123",
                        rol = "Administrador"
                    )
                )
                usuarioDao.insertarUsuario(
                    UsuarioEntity(
                        correo = "operador@sanjoaquin.cl",
                        contrasena = "operador123",
                        rol = "Operador"
                    )
                )
                usuarioDao.insertarUsuario(
                    UsuarioEntity(
                        correo = "vecino@sanjoaquin.cl",
                        contrasena = "vecino123",
                        rol = "Residente"
                    )
                )
            }
        }
    }
}
