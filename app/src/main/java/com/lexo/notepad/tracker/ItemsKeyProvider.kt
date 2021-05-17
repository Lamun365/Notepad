package com.lexo.notepad.tracker

import androidx.recyclerview.selection.ItemKeyProvider
import com.lexo.notepad.adapter.ItemAdapter
import com.lexo.notepad.db.Task

class ItemsKeyProvider(
    private val adapter: ItemAdapter
): ItemKeyProvider<Task>(SCOPE_CACHED) {

    override fun getKey(position: Int): Task? {
        return adapter.getCurrentItem(position)
    }

    override fun getPosition(key: Task): Int {
        return adapter.getCurrentPosition(key.id)
    }
}