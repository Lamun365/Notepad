package com.lexo.notepad.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.lexo.notepad.R
import com.lexo.notepad.adapter.ItemAdapter
import com.lexo.notepad.databinding.NoteViewFragmentBinding
import com.lexo.notepad.db.Task
import com.lexo.notepad.tracker.ItemsDetailsLookup
import com.lexo.notepad.tracker.ItemsKeyProvider
import com.lexo.notepad.util.SortOrder
import com.lexo.notepad.util.exhaustive
import com.lexo.notepad.util.onQueryTextChange
import com.lexo.notepad.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ItemFragment: Fragment(R.layout.note_view_fragment), ItemAdapter.OnItemClickListener {
    private val viewModel: ItemViewModel by viewModels()
    private var _binding: NoteViewFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var itemAdapter: ItemAdapter
    private var mActionMode: ActionMode? = null
    private var tracker: SelectionTracker<Task>? = null
    private var selectedItems: MutableList<Task>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = NoteViewFragmentBinding.bind(view)
        //add it to the adapter

        itemAdapter = ItemAdapter(this)
        binding.apply {

            recycleView.apply {
                adapter = itemAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
                setHasFixedSize(true)
            }

            fabAddTask.setOnClickListener {
                viewModel.onAddItemClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect { event ->
                when(event) {
                    ItemViewModel.TaskEvent.NavigateToAddItemDialog -> {
                        val action = ItemFragmentDirections.actionItemFragmentToAddItemDialogFragment()
                        findNavController().navigate(action)
                    }
                    is ItemViewModel.TaskEvent.NavigateToChecklistView -> {
                        val task = event.task
                        val action = ItemFragmentDirections.actionItemFragmentToEditChecklistFragment(task, true)
                        findNavController().navigate(action)
                    }
                    is ItemViewModel.TaskEvent.NavigateToNoteView -> {
                        val task = event.task
                        val action = ItemFragmentDirections.actionItemFragmentToEditNoteFragment(task, true)
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        viewModel.task.observe(viewLifecycleOwner) {
            itemAdapter.submitList(it)
        }

        setHasOptionsMenu(true)

        tracker = SelectionTracker.Builder(
            "itemSelection",
            binding.recycleView,
            ItemsKeyProvider(itemAdapter),
            ItemsDetailsLookup(binding.recycleView),
            StorageStrategy.createParcelableStorage(Task::class.java)
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        itemAdapter.tracker = tracker

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_task, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChange {
            viewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSelectedSortOrder(SortOrder.ORDER_BY_NAME)
                true
            }
            R.id.action_sort_by_date -> {
                viewModel.onSelectedSortOrder(SortOrder.ORDER_BY_DATE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getItem(task: Task) {
        if (!task.isListItem) {
            //navigate to noteView
            viewModel.toNoteView(task)
        }
        if (task.isListItem) {
            //navigate to checklist
            viewModel.toChecklistView(task)
        }
    }

    override fun onLongPress() {
        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Task>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    tracker?.let {
                        selectedItems = it.selection.toMutableList()
                        if (it.selection.isEmpty) {
                            mActionMode?.finish()
                        } else {

                            if (mActionMode == null) {
                                mActionMode = requireActivity().startActionMode(actionModeCallback())
                            }

                            mActionMode?.title =
                                "${it.selection.size()}"
                        }
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun actionModeCallback() =
        object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.delete_selection, menu)
                mode?.title = "Delete item"
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete_selected_option -> {
                        if (!selectedItems.isNullOrEmpty()) {
                            viewModel.deleteSelectedItems(selectedItems!!)
                            Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                itemAdapter.tracker?.clearSelection()
                mActionMode = null
            }
        }

}