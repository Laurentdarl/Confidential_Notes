package com.laurentdarl.confidentialnotesapplication.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.laurentdarl.confidentialnotesapplication.R
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
        validateInput(title, content, )
    }

    private fun validateInput(title: String, Content: String) {

    }
}