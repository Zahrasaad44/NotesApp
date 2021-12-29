package com.example.notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.database.Note
import com.example.notesapp.databinding.NoteRowBinding

class NotesAdapter(private var notes: ArrayList<Note>, private val activity: MainActivity): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>(){
    class NotesViewHolder(val binding: NoteRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(NoteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]

        holder.binding.apply {
            noteTV.text = note.noteText
            noteCV.setOnClickListener { activity.showEditDeleteDialog(note.pk,
                noteTV.text as String
            ) }
        }
    }

    override fun getItemCount() = notes.size

}