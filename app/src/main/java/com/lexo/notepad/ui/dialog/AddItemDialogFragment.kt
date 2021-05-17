package com.lexo.notepad.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lexo.notepad.R
import com.lexo.notepad.databinding.AddItemDialogBinding
import com.lexo.notepad.util.exhaustive
import com.lexo.notepad.ui.viewmodel.AddItemDialogViewModel
import kotlinx.coroutines.flow.collect

class AddItemDialogFragment: DialogFragment(R.layout.add_item_dialog) {
    private var _binding: AddItemDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddItemDialogViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = AddItemDialogBinding.bind(view)
        binding.apply {
            itemAddChecklist.setOnClickListener {
                viewModel.onClickAddChecklist()
            }
            itemAddNote.setOnClickListener {
                viewModel.onClickAddNote()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.moveEvent.collect { event ->
                when(event) {
                    AddItemDialogViewModel.MoveEvent.NavigateToCheckList -> {
                        val action = AddItemDialogFragmentDirections.actionAddItemDialogFragmentToEditChecklistFragment(null, false)
                        findNavController().navigate(action)
                        dismiss()
                    }
                    AddItemDialogViewModel.MoveEvent.NavigateToNoteItem -> {
                        val action = AddItemDialogFragmentDirections.actionAddItemDialogFragmentToEditNoteFragment(null, false)
                        findNavController().navigate(action)
                        dismiss()
                    }
                }.exhaustive
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}