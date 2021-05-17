package com.lexo.notepad.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lexo.notepad.databinding.NoteItemsBinding
import com.lexo.notepad.db.Task
import com.lexo.notepad.util.toBulletList

class ItemAdapter(private val listener: OnItemClickListener): ListAdapter<Task, ItemAdapter.ItemViewHolder>(
    DiffCallBack()
) {

    var tracker: SelectionTracker<Task>? = null

    inner class ItemViewHolder(val binding: NoteItemsBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val task = getItem(position)
                    listener.getItem(task)
                }
            }
            binding.root.setOnLongClickListener {
                listener.onLongPress()
                true
            }
        }

        fun loadItems(task: Task) {
            binding.apply {
                itemHeadingTextView.text = task.title

                if (!task.isListItem) {
                    itemTextView.text = task.content
                } else {
                    val taskList = task.content.split(", ")
                    itemTextView.text = taskList.toBulletList()
                }

            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Task> =
            object : ItemDetailsLookup.ItemDetails<Task>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): Task? = getItem(position)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = NoteItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.loadItems(getItem(position))
        val selected: Boolean = tracker?.isSelected(getItem(position)) ?: false
         if (selected) {
             holder.binding.root.setBackgroundColor(Color.parseColor("#FF03DAC5"))
             selected != selected
         }
        if (!selected) {
            holder.binding.root.setBackgroundColor(Color.parseColor("#262626"))
            selected != selected
        }
    }

    class DiffCallBack: DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }

    fun getCurrentItem(position: Int): Task? = getItem(position)
    fun getCurrentPosition(id: Int) = currentList.indexOfFirst { it.id == id}

    interface OnItemClickListener {
        fun getItem(task: Task)
        fun onLongPress()
    }
}