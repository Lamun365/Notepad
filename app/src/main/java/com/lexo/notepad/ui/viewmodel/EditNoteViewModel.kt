package com.lexo.notepad.ui.viewmodel

import androidx.lifecycle.*
import com.lexo.notepad.db.Task
import com.lexo.notepad.db.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val taskDao: TaskDao,
    state: SavedStateHandle
): ViewModel() {

    private val channel = Channel<EditNoteEvent>()
    val editNoteEvent = channel.receiveAsFlow()

    private val isEditMode = state.get<Boolean>("isEditMode") ?: false
    private val stateTask = state.get<Task>("noteTask")

    private val taskTitle = stateTask?.title ?: ""
    private val taskContent = stateTask?.content ?: ""
    val taskDateCrated = stateTask?.createDateFormat ?: ""

    private var _title = MutableLiveData(taskTitle)
    val title: LiveData<String> = _title

    private var _content = MutableLiveData(taskContent)
    val content: LiveData<String> = _content

    fun onSaveClick(title: String?, content: String?) = viewModelScope.launch {

        if (!isEditMode) {
            val task = Task(title.orEmpty(), content.orEmpty(), "", false)
            taskDao.insert(task)
        }

        if (isEditMode) {
            val editedTask = stateTask?.copy(
                title = title.orEmpty(),
                content = content.orEmpty()
            )!!
            taskDao.update(editedTask)
        }

        channel.send(EditNoteEvent.NavigateToBackStack)
    }

    fun onDeleteNoteClick() {
        viewModelScope.launch {
            if (stateTask != null) {
                taskDao.delete(stateTask)
            }
        }
    }

    sealed class EditNoteEvent {
        object NavigateToBackStack: EditNoteEvent()
    }
}