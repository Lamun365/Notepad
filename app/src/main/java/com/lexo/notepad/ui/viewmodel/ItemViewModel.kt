package com.lexo.notepad.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lexo.notepad.db.PreferenceManager
import com.lexo.notepad.db.Task
import com.lexo.notepad.db.TaskDao
import com.lexo.notepad.util.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
        private val taskDao: TaskDao,
        private val preferenceManager: PreferenceManager
): ViewModel() {

    private val taskEventChannel = Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()
    val searchQuery = MutableStateFlow("")
    private val preferenceFlow = preferenceManager.preferenceFlow

    private val taskFlow = combine(
        searchQuery,
        preferenceFlow
    ) {query, pref ->
        Pair(query, pref)
    }
        .flatMapLatest { (query, pref) ->
            taskDao.getTask(query, pref.sortOrder)
        }

    val task = taskFlow.asLiveData()

    fun onAddItemClick() = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddItemDialog)
    }

    fun toNoteView(task: Task) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToNoteView(task))
    }

    fun toChecklistView(task: Task) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToChecklistView(task))
    }

    fun onSelectedSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            preferenceManager.updateSortOrder(sortOrder)
        }
    }

    fun deleteSelectedItems(list: MutableList<Task>) = viewModelScope.launch {
        list.forEach { task ->
            taskDao.delete(task)
        }
    }

    sealed class TaskEvent {
        object NavigateToAddItemDialog: TaskEvent()
        data class NavigateToNoteView(val task: Task): TaskEvent()
        data class NavigateToChecklistView(val task: Task): TaskEvent()
    }

}