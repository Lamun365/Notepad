package com.lexo.notepad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lexo.notepad.databinding.ChecklistItemBinding
import com.lexo.notepad.db.ChecklistItem

class ChecklistAdapter(private val listener: OnCheckItemTextClick): ListAdapter<ChecklistItem, ChecklistAdapter.ChecklistViewHolder>(
    DiffChecklistCallBack()
) {

    inner class ChecklistViewHolder(private val binding: ChecklistItemBinding): RecyclerView.ViewHolder(binding.root) {


        init {

            binding.apply {
                checkItemText.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val itemText = getItem(position).item
                        listener.getCheckItemText(position, itemText)
                    }
                }

                checkboxCheckItem.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val itemChecked = checkboxCheckItem.isChecked
                        listener.getItemCheck(position, itemChecked)
                    }
                }
            }
        }

        fun bind(item: ChecklistItem) {
            binding.apply {
                checkItemText.text = item.item
                checkboxCheckItem.isChecked = item.isChecked
                checkItemText.paint.isStrikeThruText = item.isChecked
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val binding = ChecklistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChecklistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class DiffChecklistCallBack: DiffUtil.ItemCallback<ChecklistItem>() {
        override fun areItemsTheSame(oldItem: ChecklistItem, newItem: ChecklistItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChecklistItem, newItem: ChecklistItem) =
            oldItem == newItem
    }

    interface OnCheckItemTextClick {
        fun getCheckItemText(position: Int, itemText: String)
        fun getItemCheck(position: Int, isChecked: Boolean)
    }

}