package com.laurentdarl.confidentialnotesapplication.presentation.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.data.adapters.NotesAdapter
import com.laurentdarl.confidentialnotesapplication.data.viewmodels.NoteViewModel
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentNotesBinding


class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(layoutInflater)
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        notesAdapter = NotesAdapter() {  }
        binding.rvNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            hasFixedSize()
        }

        binding.floatingActionButton.setOnClickListener {
            val actions = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment()
            findNavController().navigate(actions)
        }

        noteViewModel.getAllNotes.observe(viewLifecycleOwner, {note ->
            notesAdapter.setData(note)
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}