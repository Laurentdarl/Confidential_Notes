package com.laurentdarl.confidentialnotesapplication.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.laurentdarl.confidentialnotesapplication.data.models.Note
import com.laurentdarl.confidentialnotesapplication.databinding.NoteItemBinding
import java.util.*

class NotesAdapter(var clicker: (Note) -> Unit): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    var noteList = emptyList<Note>()

    inner class NoteViewHolder(private val binding: NoteItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private val currentTime: Date = Calendar.getInstance().time
        private val formattedDate: String = java.text.DateFormat.getDateInstance().format(currentTime)
        fun bind(note: Note) {
            binding.apply {
                tvTitle.text = binding.tvTitle.toString()
                tvContent.text = binding.tvContent.toString()
                tvTime.text = formattedDate

                root.setOnClickListener {
                    note.let {

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewBinding = NoteItemBinding.inflate(LayoutInflater.from(parent.context))
        return NoteViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun setData(note: List<Note>) {
        this.noteList = note
        notifyDataSetChanged()
    }
}