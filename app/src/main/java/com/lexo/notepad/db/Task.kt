package com.lexo.notepad.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import kotlinx.parcelize.Parcelize

@Entity(tableName = "task_table")
@Parcelize
data class Task(
        val title: String,
        val content: String,
        val booleanList: String,
        val isListItem: Boolean = false,
        val created: Long = System.currentTimeMillis(),
        @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable {
    val createDateFormat: String get() = DateFormat.getDateTimeInstance().format(created)
}
