package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesapp.database.DatabaseHelper
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.addBtn.setOnClickListener { addNote() }

    }

    fun addNote() {
        val userNote = binding.userNoteET.text.toString()
        databaseHelper.saveNotes(userNote)
        binding.userNoteET.text.clear()
        Toast.makeText(this, "Note added successfully!", Toast.LENGTH_LONG).show()
    }
}