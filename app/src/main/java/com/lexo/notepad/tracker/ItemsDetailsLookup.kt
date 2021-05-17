package com.lexo.notepad.tracker

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.lexo.notepad.adapter.ItemAdapter
import com.lexo.notepad.db.Task

class ItemsDetailsLookup(
    private val rv: RecyclerView
): ItemDetailsLookup<Task>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Task>? {
        val view = rv.findChildViewUnder(e.x, e.y)

        if (view != null) {
            return (rv.getChildViewHolder(view) as ItemAdapter.ItemViewHolder)
                .getItemDetails()
        }
        return null
    }
}