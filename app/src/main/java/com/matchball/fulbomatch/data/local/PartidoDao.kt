package com.matchball.fulbomatch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlin.jvm.JvmSuppressWildcards
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidoDao {

    @Query("SELECT * FROM partidos_table ORDER BY fecha ASC, hora ASC")
    fun getAllPartidos(): Flow<List<PartidoEntity>>

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(partidos: List<PartidoEntity>): List<Long>

    @JvmSuppressWildcards
    @Query("DELETE FROM partidos_table")
    suspend fun clearAll(): Int
}