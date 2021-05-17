package com.lexo.notepad.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lexo.notepad.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao


    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            scope.launch {
                dao.insert(
                    Task(
                        title = "Example 1",
                        content = "Task 1, Task 2, Task 3, Task 4",
                        booleanList = "true, false, true, false",
                        isListItem = true
                    )
                )

                dao.insert(
                    Task(
                        title = "Example 2",
                        content = "This is a empty note",
                        booleanList = "",
                        isListItem = false
                    )
                )
            }
        }
    }

}