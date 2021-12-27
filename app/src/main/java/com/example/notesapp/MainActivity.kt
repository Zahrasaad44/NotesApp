package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.database.DatabaseHelper
import com.example.notesapp.database.Note
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: NotesAdapter
    private lateinit var notes: ArrayList<Note>

    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        notes = arrayListOf()
        recyclerAdapter = NotesAdapter(notes)
        binding.notesRV.adapter = recyclerAdapter
        binding.notesRV.layoutManager = GridLayoutManager(this, 2)

        binding.addBtn.setOnClickListener { addNote(); displayNote() }

    }

    private fun addNote() {
        val userNote = binding.userNoteET.text.toString()
        databaseHelper.saveNotes(userNote)
        binding.userNoteET.text.clear()
        Toast.makeText(this, "Note added successfully!", Toast.LENGTH_LONG).show()
    }

    private fun displayNote() {
        notes = databaseHelper.readData() // readData() is returning an ArrayList of notes
        recyclerAdapter.updateNotes(notes)
    }
}