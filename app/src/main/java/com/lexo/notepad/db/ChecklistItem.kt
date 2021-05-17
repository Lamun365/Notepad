package com.lexo.notepad.db

data class ChecklistItem(
        var id: Int,
        var item: String,
        var isChecked: Boolean
)
