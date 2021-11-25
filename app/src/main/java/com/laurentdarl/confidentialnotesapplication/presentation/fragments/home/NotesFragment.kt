package com.laurentdarl.confidentialnotesapplication.presentation.fragments.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laurentdarl.confidentialnotesapplication.R
import com.laurentdarl.confidentialnotesapplication.data.adapters.NotesAdapter
import com.laurentdarl.confidentialnotesapplication.data.viewmodels.NoteViewModel
import com.laurentdarl.confidentialnotesapplication.databinding.ActivityMainBinding.inflate
import com.laurentdarl.confidentialnotesapplication.databinding.FragmentNotesBinding
import java.util.zip.Inflater


class NotesFragment : Fragment(), SearchView.OnQueryTextListener {

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
        setHasOptionsMenu(true)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)

        val search = menu?.findItem(R.id.search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(searchQuery: String) {
        val userSearch = "%$searchQuery%"  //The custom SQL query will require this specific format
        noteViewModel.getSelectNote(userSearch).observe(viewLifecycleOwner, {note ->
            notesAdapter.setData(note)
        })
    }


}