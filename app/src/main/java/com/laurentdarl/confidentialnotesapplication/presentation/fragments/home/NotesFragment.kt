package com.laurentdarl.confidentialnotesapplication.presentation.fragments.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
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
    private val auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser

    private val authUI = AuthUI.getInstance()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.profileFragment ->
            item.onNavDestinationSelected(
                findNavController()
            ) || super.onOptionsItemSelected(item)
            R.id.logout -> {
                signOut()
                user?.reload()
                user = FirebaseAuth.getInstance().currentUser
            }
            R.id.profile_delete -> deleteProfile()
        }
        return true
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

    private fun signOut() {
        authUI.signOut(requireContext())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginAction()
                    Toast.makeText(requireContext(), "logged out successfully",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), task.exception?.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteProfile() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete account?")
            .setMessage("This action is permanent, are you sure?")
            .setPositiveButton("Yes") {_,_ ->
                authUI.delete(requireContext())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                            loginAction()
                        } else {
                            Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun loginAction() {
        val actions = NotesFragmentDirections.actionNotesFragmentToSignInFragment()
        findNavController().navigate(actions)
    }

}