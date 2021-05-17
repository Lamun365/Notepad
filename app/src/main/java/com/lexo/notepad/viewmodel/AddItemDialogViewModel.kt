package com.lexo.notepad.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddItemDialogViewModel: ViewModel() {

    private val moveEventChannel = Channel<MoveEvent>()
    val moveEvent = moveEventChannel.receiveAsFlow()

    fun onClickAddNote() = viewModelScope.launch {
        moveEventChannel.send(MoveEvent.NavigateToNoteItem)
    }

    fun onClickAddChecklist() = viewModelScope.launch {
        moveEventChannel.send(MoveEvent.NavigateToCheckList)
    }

    sealed class MoveEvent {
        object NavigateToCheckList: MoveEvent()
        object NavigateToNoteItem: MoveEvent()
    }
}