package com.laurentdarl.confidentialnotesapplication.data.repository

import androidx.lifecycle.LiveData
import com.laurentdarl.confidentialnotesapplication.data.db.NoteDAO
import com.laurentdarl.confidentialnotesapplication.data.models.Note

class NoteRepository(private val noteDAO: NoteDAO) {
    suspend fun addNote(note: Note): Long = noteDAO.addNewNote(note)

    suspend fun updateNote(note: Note) = noteDAO.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDAO.deleteNote(note)

    suspend fun deleteAllNotes() = noteDAO.deleteAllNotes()

    fun getAllNotes(): LiveData<List<Note>> = noteDAO.getAllNotes()

    fun getSelectNote(noteSearch: String): LiveData<List<Note>> = noteDAO.getSelectNote(noteSearch)
}