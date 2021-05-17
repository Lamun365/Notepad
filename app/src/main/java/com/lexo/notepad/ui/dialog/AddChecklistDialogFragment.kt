package com.lexo.notepad.ui.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lexo.notepad.databinding.AddChecklistDialogBinding
import com.lexo.notepad.db.ChecklistItem
import com.lexo.notepad.ui.viewmodel.EditChecklistViewModel
import com.lexo.notepad.util.exhaustive
import kotlinx.coroutines.flow.collect


class AddChecklistDialogFragment: DialogFragment() {

    private var _binding: AddChecklistDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditChecklistViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = AddChecklistDialogBinding.bind(view)

        var id = 0
        binding.fabAddChecklist.isVisible = true
        binding.fabEditChecklist.isVisible = false
        binding.dialogAddItemEditText.setText(viewModel.itemTextEdit)
        binding.fabAddChecklist.setOnClickListener {
            id++
            val item = ChecklistItem(id, binding.dialogAddItemEditText.text.toString(), false)
            viewModel.addChecklistItem(item)
            viewModel.onAddChecklistClick()
        }
        binding.fabEditChecklist.setOnClickListener {
            val position = viewModel.positionEdit
            val itemText = binding.dialogAddItemEditText.text.toString()
            viewModel.editChecklistItem(itemText, position)
            viewModel.onAddChecklistClick()
        }



        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.checklistDialogEvent.collect { event ->
                when (event) {
                    EditChecklistViewModel.ChecklistDialogEvent.NavigateToEditChecklistFragment -> {
                        val action = AddChecklistDialogFragmentDirections.actionAddChecklistDialogFragmentToEditChecklistFragment(null)
                        findNavController().navigate(action)
                        dismiss()
//                        findNavController().popBackStack()
                    }
                    EditChecklistViewModel.ChecklistDialogEvent.OnEditChecklistClick -> {
                        binding.fabAddChecklist.isVisible = false
                        binding.fabEditChecklist.isVisible = true
                    }
                }.exhaustive
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return AddChecklistDialogBinding.inflate(inflater, container, false).root

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.itemTextEdit = ""
        _binding = null
    }

}