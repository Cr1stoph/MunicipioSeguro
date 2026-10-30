package com.duoc.municipioseguro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UsuarioEntity::class, CasoEntity::class], version = 2, exportSchema = false)
abstract class MunicipioDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun casoDao(): CasoDao

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
                .fallbackToDestructiveMigration()
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
                        prepoblarDatabase(database.usuarioDao(), database.casoDao())
                    }
                }
            }

            suspend fun prepoblarDatabase(usuarioDao: UsuarioDao, casoDao: CasoDao) {
                usuarioDao.insertarUsuario(UsuarioEntity(correo = "admin@sanjoaquin.cl", contrasena = "admin123", rol = "Administrador"))
                usuarioDao.insertarUsuario(UsuarioEntity(correo = "operador@sanjoaquin.cl", contrasena = "operador123", rol = "Operador"))
                usuarioDao.insertarUsuario(UsuarioEntity(correo = "vecino@sanjoaquin.cl", contrasena = "vecino123", rol = "Residente"))

                casoDao.insertarCaso(
                    CasoEntity(
                        id = "SJ-2026-0148",
                        categoria = "AlumbradoPublico",
                        fechaHora = 1792022400000L - (1L * 86400000L),
                        ubicacion = "Av. Departamental con Las Industrias",
                        descripcion = "Reporte ingresado para revisión.",
                        prioridad = "Media",
                        estado = "Registrado",
                        rolAsignado = null,
                        urlEvidencia = null,
                        latitud = -33.4883,
                        longitud = -70.6233,
                        autorEmail = "vecino@sanjoaquin.cl",
                        observacionesJson = "[]"
                    )
                )
                casoDao.insertarCaso(
                    CasoEntity(
                        id = "SJ-2026-0142",
                        categoria = "EspacioPublico",
                        fechaHora = 1792022400000L - (3L * 86400000L),
                        ubicacion = "Las Industrias con Pintor Cicarelli",
                        descripcion = "Derivado al área de espacio público.",
                        prioridad = "Alta",
                        estado = "EnRevision",
                        rolAsignado = "Operador",
                        urlEvidencia = null,
                        latitud = -33.4812,
                        longitud = -70.6255,
                        autorEmail = "vecino@sanjoaquin.cl",
                        observacionesJson = "[]"
                    )
                )
                casoDao.insertarCaso(
                    CasoEntity(
                        id = "SJ-2026-0126",
                        categoria = "AlumbradoPublico",
                        fechaHora = 1792022400000L - (8L * 86400000L),
                        ubicacion = "Av. Departamental con Santa Rosa",
                        descripcion = "Reparación informada por el área a cargo.",
                        prioridad = "Baja",
                        estado = "Derivado",
                        rolAsignado = "Supervisor",
                        urlEvidencia = null,
                        latitud = -33.4891,
                        longitud = -70.6380,
                        autorEmail = "comunidad@sanjoaquin.cl",
                        observacionesJson = "[]"
                    )
                )
            }
        }
    }
}
