package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {

    @Query("SELECT * FROM scripts ORDER BY updatedAt DESC")
    fun getAllScripts(): Flow<List<ScriptEntity>>

    @Query("SELECT * FROM scripts WHERE id = :id LIMIT 1")
    suspend fun getScriptById(id: Long): ScriptEntity?

    @Query("""
        SELECT * FROM scripts 
        WHERE (title LIKE '%' || :query || '%' 
           OR hook LIKE '%' || :query || '%' 
           OR bodyContent LIKE '%' || :query || '%' 
           OR tags LIKE '%' || :query || '%')
        ORDER BY updatedAt DESC
    """)
    fun searchScripts(query: String): Flow<List<ScriptEntity>>

    @Query("SELECT * FROM scripts WHERE category = :category ORDER BY updatedAt DESC")
    fun getScriptsByCategory(category: String): Flow<List<ScriptEntity>>

    @Query("""
        SELECT * FROM scripts 
        WHERE category = :category 
          AND (title LIKE '%' || :query || '%' 
           OR hook LIKE '%' || :query || '%' 
           OR bodyContent LIKE '%' || :query || '%' 
           OR tags LIKE '%' || :query || '%')
        ORDER BY updatedAt DESC
    """)
    fun searchScriptsWithCategory(query: String, category: String): Flow<List<ScriptEntity>>

    @Query("SELECT DISTINCT category FROM scripts WHERE category != '' ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScript(script: ScriptEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scripts: List<ScriptEntity>)

    @Update
    suspend fun updateScript(script: ScriptEntity)

    @Delete
    suspend fun deleteScript(script: ScriptEntity)

    @Query("DELETE FROM scripts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM scripts")
    fun getScriptCount(): Flow<Int>
}
