package com.lexo.notepad.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lexo.notepad.R
import com.lexo.notepad.ui.adapter.ChecklistAdapter
import com.lexo.notepad.databinding.EditItemChecklistBinding
import com.lexo.notepad.util.exhaustive
import com.lexo.notepad.ui.viewmodel.EditChecklistViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditChecklistFragment: Fragment(R.layout.edit_item_checklist), ChecklistAdapter.OnCheckItemTextClick {

    private var _binding: EditItemChecklistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditChecklistViewModel by activityViewModels()
    private val args: EditChecklistFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = EditItemChecklistBinding.bind(view)
        val checklistAdapter = ChecklistAdapter(this)


        binding.apply {

            dateCreatedText.apply {
                text = args.checklistTask?.createDateFormat ?: ""
                isVisible = !args.checklistTask?.createDateFormat.isNullOrEmpty()
            }

            fabAddItem.setOnClickListener {
                viewModel.onClickAddItem(binding.itemTitleChecklist.text.toString())
            }

            checklistRecycle.apply {
                adapter = checklistAdapter
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
            }

            fabSaveChecklist.setOnClickListener {
                saveChecklist()
            }
        }

        viewModel.apply {

            getPreviousTask(args.checklistTask)

            setChecklistArray(args.checklistTask)

            checkLists.observe(viewLifecycleOwner) {items ->
                checklistAdapter.submitList(items)
            }

            title.observe(viewLifecycleOwner) {
                binding.itemTitleChecklist.setText(it.orEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editChecklistEvent.collect { event ->
                when (event) {
                    EditChecklistViewModel.EditChecklistEvent.NavigateToAddItemDialog -> {
                        val action = EditChecklistFragmentDirections.actionEditChecklistFragmentToAddChecklistDialogFragment()
                        findNavController().navigate(action)
                    }
                    EditChecklistViewModel.EditChecklistEvent.DeleteNoteSelected -> {
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = checklistAdapter.currentList[viewHolder.bindingAdapterPosition]
                viewModel.onItemSwipe(item)
                checklistAdapter.notifyDataSetChanged()
            }
        }).attachToRecyclerView(binding.checklistRecycle)

        val callback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                saveChecklist()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setHasOptionsMenu(true)
    }

    private fun saveChecklist() {
        if (!viewModel.checkLists.value.isNullOrEmpty()) {
            if (viewModel.checkLists.value!![0].item.isNotEmpty() || viewModel.checkLists.value!!.size != 1) {
                viewModel.onSaveClick(binding.itemTitleChecklist.text.toString())
                findNavController().popBackStack()
                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Add item", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Add item", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_item_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.delete_full_note -> {
                viewModel.onOptionDeleteSelected(args.checklistTask)
                Toast.makeText(requireContext(), "Checklist deleted", Toast.LENGTH_SHORT).show()
                binding.itemTitleChecklist.setText("")
                true
            }

            R.id.edit_note_option -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getCheckItemText(position: Int, itemText: String) {
        viewModel.apply {
            sendEditItemPosition(position, itemText)
            onClickAddItem(binding.itemTitleChecklist.text.toString())
        }
    }

    override fun getItemCheck(position: Int, isChecked: Boolean) {
        viewModel.editChecklistCheckbox(isChecked, position)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null


    }
}