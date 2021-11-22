package com.laurentdarl.confidentialnotesapplication.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.laurentdarl.confidentialnotesapplication.data.db.NoteDatabase
import com.laurentdarl.confidentialnotesapplication.data.models.Note
import com.laurentdarl.confidentialnotesapplication.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {
    val getAllNotes: LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val dao = NoteDatabase.getNoteDatabase(application).noteDAO()
        repository = NoteRepository(dao)
        getAllNotes = repository.getAllNotes()
    }

    fun getSelectNote(noteSearch: String): Job = viewModelScope.launch(Dispatchers.IO) {
        repository.getSelectNote(noteSearch)
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO)
    {
        repository.addNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO)
    {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO)
    {
        repository.deleteNote(note)
    }

    fun deleteAllNotes() = viewModelScope.launch(Dispatchers.IO)
    {
        repository.deleteAllNotes()
    }

}