package com.laurentdarl.confidentialnotesapplication.presentation.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.data.models.Note
import com.laurentdarl.confidentialnotesapplication.data.viewmodels.NoteViewModel
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentAddNoteBinding
import java.util.*


class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        binding.btnAddNote.setOnClickListener {
            addNote()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun addNote() {
        val currentTime: Date = Calendar.getInstance().time
        val title = binding.tifNoteTitle.text.toString()
        val content = binding.tifNoteContent.text.toString()
        validateInput(title, content, currentTime)
    }

    private fun validateInput(title: String, content: String, currentTime: Date?) {
        val note = Note(title, content, currentTime)
        when {
            TextUtils.isEmpty(title) -> Toast.makeText(requireContext(), "Please include a title", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(content) -> Toast.makeText(requireContext(), "Please include a content", Toast.LENGTH_SHORT).show()
            else -> {
                noteViewModel.addNote(note)
                Toast.makeText(requireContext(), "Note added successfully!", Toast.LENGTH_SHORT).show()
                val actions = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
                findNavController().navigate(actions)
            }
        }
    }
}