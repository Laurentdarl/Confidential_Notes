package com.laurentdarl.confidentialnotesapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.laurentdarl.confidentialnotesapplication.data.models.Note

@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE :noteSearch OR content LIKE :noteSearch")
    fun getSelectNote(noteSearch: String): LiveData<List<Note>>
}