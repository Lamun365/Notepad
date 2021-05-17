package com.lexo.notepad.ui.viewmodel


import androidx.lifecycle.*
import com.lexo.notepad.db.Task
import com.lexo.notepad.db.TaskDao
import com.lexo.notepad.db.ChecklistItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditChecklistViewModel @Inject constructor(
    private val taskDao: TaskDao,
): ViewModel() {

    private val editChecklistChannel = Channel<EditChecklistEvent>()
    val editChecklistEvent = editChecklistChannel.receiveAsFlow()

    private var _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    fun onClickAddItem(title: String) = viewModelScope.launch {
        editChecklistChannel.send(EditChecklistEvent.NavigateToAddItemDialog)
        _title.value = title
    }
    private var isEditMode = false
    private var getTask: Task? = null

    fun getPreviousTask(task: Task?) {
        if (task != null) {
            isEditMode = true
            getTask = task
            _title.value = task.title
        }
    }

    fun onSaveClick(title: String) = viewModelScope.launch {
        val itemList = _checkList.value?.toList()!!
        val contents = arrayListOf<String>()
        val booleans = arrayListOf<Boolean>()
        for (item in itemList) {
            contents.add(item.item)
            booleans.add(item.isChecked)
        }
        if (!isEditMode) {
            val task = Task(title, contents.joinToString(), booleans.toBooleanArray().joinToString(", "), true)
            taskDao.insert(task)
            _checkList.value = arrayListOf(ChecklistItem(0, "", false))
            _title.value = ""
        }

        if (isEditMode) {
            val editTask = getTask?.copy(
                title = title,
                content = contents.joinToString(),
                booleanList = booleans.joinToString(),
                isListItem = true
            )!!
            taskDao.update(editTask)
            isEditMode = false
            _checkList.value = arrayListOf(ChecklistItem(0, "", false))
            _title.value = ""
        }

    }

    fun sendEditItemPosition(position: Int, itemText: String) = viewModelScope.launch {
        positionEdit = position
        itemTextEdit = itemText
        dialogChannel.send(ChecklistDialogEvent.OnEditChecklistClick)
    }

    fun editChecklistCheckbox(isChecked: Boolean, position: Int) {
        val checkListItem = _checkList.value?.get(position)
        _checkList.value?.removeAt(position)
        checkListItem?.let {
            _checkList.value?.add(position, it.copy(isChecked = isChecked))
        }
    }

    fun onOptionDeleteSelected(task: Task?) = viewModelScope.launch {
        if (task != null) {
            taskDao.delete(task)
        }
        _checkList.value = arrayListOf(ChecklistItem(0, "", false))
        editChecklistChannel.send(EditChecklistEvent.DeleteNoteSelected)
    }

    fun onItemSwipe(item: ChecklistItem) {
        _checkList.value?.remove(item)
    }

    sealed class EditChecklistEvent {
        object NavigateToAddItemDialog: EditChecklistEvent()
        object DeleteNoteSelected: EditChecklistEvent()
    }

    /* dialog checklist view model */

    private var _checkList = MutableLiveData(arrayListOf(ChecklistItem(0, "", false)))
    val checkLists: LiveData<ArrayList<ChecklistItem>> = _checkList
    private val dialogChannel = Channel<ChecklistDialogEvent>()
    val checklistDialogEvent = dialogChannel.receiveAsFlow()

    var positionEdit: Int = 0
    var itemTextEdit: String = ""

    fun setChecklistArray (task: Task?) {
        if (task != null) {
            val itemsArray = arrayListOf<ChecklistItem>()
            var id = 0
            val contents = task.content.split(", ")
            val booleanList = task.booleanList.split(", ")
            val items = contents zip booleanList

            items.forEach { item ->
                id++
                itemsArray.add(
                    ChecklistItem(
                    id,
                    item.first,
                    item.second.toBoolean()
                )
                )
            }
            if (!_checkList.value.isNullOrEmpty()) {
                if (_checkList.value!![0].item.isEmpty() && _checkList.value!!.size == 1) {
                    _checkList.value?.removeAt(0)
                }
            }
            itemsArray.forEach { item ->
                _checkList.value?.add(item)
            }
        }
    }

    fun onAddChecklistClick() = viewModelScope.launch {
        dialogChannel.send(ChecklistDialogEvent.NavigateToEditChecklistFragment)
    }

    fun addChecklistItem(item: ChecklistItem) {
        _checkList.value?.add(item)
    }

    fun editChecklistItem(itemText: String, position: Int) {
        val checkListItem = _checkList.value?.get(position)
        _checkList.value?.removeAt(position)
        checkListItem?.let { _checkList.value?.add(position, it.copy(item = itemText)) }
    }

    sealed class ChecklistDialogEvent {
        object NavigateToEditChecklistFragment: ChecklistDialogEvent()
        object OnEditChecklistClick: ChecklistDialogEvent()
    }

}