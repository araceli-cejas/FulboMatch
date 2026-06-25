package com.matchball.fulbomatch.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Declaramos la base de datos, sus tablas y la versión
@Database(entities = [PartidoEntity::class], version = 3, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun partidoDao(): PartidoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Patrón Singleton seguro para obtener la base de datos
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fulbomatch_database"
                )
                .fallbackToDestructiveMigration() // Borra y recrea si cambia el esquema
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}