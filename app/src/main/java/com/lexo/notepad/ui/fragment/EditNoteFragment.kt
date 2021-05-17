package com.lexo.notepad.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lexo.notepad.R
import com.lexo.notepad.databinding.EditItemNoteBinding
import com.lexo.notepad.util.exhaustive
import com.lexo.notepad.viewmodel.EditNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditNoteFragment: Fragment(R.layout.edit_item_note) {
    private var _binding: EditItemNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = EditItemNoteBinding.bind(view)

        binding.apply {

            fabSaveNote.setOnClickListener {
                saveNote()
            }

            dateCreatedTextNote.apply {
                isVisible = viewModel.taskDateCrated.isNotEmpty()
                text = viewModel.taskDateCrated
            }
        }

        viewModel.apply {
            title.observe(viewLifecycleOwner) {
                binding.itemTitleNote.setText(it)
            }

            content.observe(viewLifecycleOwner) {content ->
                binding.itemContentNote.setText(content)
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                saveNote()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editNoteEvent.collect { event ->
                when (event) {
                    EditNoteViewModel.EditNoteEvent.NavigateToBackStack -> {
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    private fun saveNote() {
        val title = binding.itemTitleNote.text.toString()
        val content = binding.itemContentNote.text.toString()

        if (content.isNotEmpty()) {
            viewModel.onSaveClick(
                title = title,
                content = content
            )
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Empty note", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_item_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_full_note -> {

                viewModel.onDeleteNoteClick()

                binding.apply {
                    itemTitleNote.setText("")
                    itemContentNote.setText("")
                }
                Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                true
                //customize it later and add it to lifecycle scope
            }
            R.id.edit_note_option -> {
                //customize it later
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}