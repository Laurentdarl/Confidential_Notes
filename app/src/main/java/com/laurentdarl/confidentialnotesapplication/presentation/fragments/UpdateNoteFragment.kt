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
import androidx.navigation.fragment.navArgs
import com.laurentdarl.confidentialnotesapplication.data.models.Note
import com.laurentdarl.confidentialnotesapplication.data.viewmodels.NoteViewModel
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentUpdateNoteBinding
import java.util.*

class UpdateNoteFragment : Fragment() {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteViewModel: NoteViewModel
    private val args by navArgs<UpdateNoteFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(layoutInflater)

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding.tifUpdateNoteTitle.setText(args.note.title)
        binding.tifUpdateNoteContent.setText(args.note.content)

        binding.btnUpdateNote.setOnClickListener {
            updateNote()
        }

        binding.btnClear.setOnClickListener {
            actions()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun actions() {
        val actions = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToNotesFragment()
        findNavController().navigate(actions)
    }

    private fun updateNote() {
        val currentTime: Date = Calendar.getInstance().time
        val title = binding.tifUpdateNoteTitle.text.toString()
        val content = binding.tifUpdateNoteContent.text.toString()
        val id = args.note.id
        validateInput(title, content, currentTime, id)
        actions()
    }

    private fun validateInput(title: String, content: String, currentTime: Date?, id: Int) {
        val note = Note(title, content, currentTime, id)
        when {
            TextUtils.isEmpty(title) -> Toast.makeText(requireContext(), "Please include a title", Toast.LENGTH_SHORT).show()
            TextUtils.isEmpty(content) -> Toast.makeText(requireContext(), "Please include a content", Toast.LENGTH_SHORT).show()
            else -> {
                noteViewModel.updateNote(note)
                Toast.makeText(requireContext(), "Note added successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}