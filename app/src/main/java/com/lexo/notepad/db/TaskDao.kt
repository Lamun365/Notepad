package com.lexo.notepad.db

import androidx.room.*
import com.lexo.notepad.util.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    fun getTask(searchQuery: String, sortOrder: SortOrder): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.ORDER_BY_NAME -> getTaskByName(searchQuery)
            SortOrder.ORDER_BY_DATE -> getTaskByDate(searchQuery)
        }

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY title")
    fun getTaskByName(searchQuery: String): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchQuery || '%' ORDER BY created")
    fun getTaskByDate(searchQuery: String): Flow<List<Task>>

}