package com.example.notesapp

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notesapp.database.DatabaseHelper
import com.example.notesapp.database.Note
import com.example.notesapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: NotesAdapter

    private val databaseHelper by lazy { DatabaseHelper(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.floatingActionButton.setOnClickListener { showAddNoteDialog() }

        updateRV() // To update the RV whenever the activity is created
    }


    private fun updateRV() {
        recyclerAdapter = NotesAdapter(getNotes(), this) // To display the notes in the RV on onCreate() without the need to add a note to then display all notes
        binding.notesRV.adapter = recyclerAdapter
        binding.notesRV.layoutManager = GridLayoutManager(this, 2)
    }

    private fun getNotes(): ArrayList<Note> {
        return databaseHelper.readData()  // readData() is returning an ArrayList of notes
    }


    private fun editNote(notePK: Int, text: String) {
        databaseHelper.updateDate(Note(notePK, text))
        Toast.makeText(this, "Note Updated", Toast.LENGTH_LONG).show()
        updateRV()

    }

    private fun deleteNote(notePK: Int) {
        databaseHelper.deleteData(Note(notePK, ""))
        Toast.makeText(applicationContext, "Note Deleted", Toast.LENGTH_LONG).show()
        updateRV()

    }


    fun showEditDeleteDialog(id: Int, noteText: String) {
        val dialog = Dialog(
            this,
            R.style.Theme_AppCompat_DayNight) //The second argument is to make the dialog in full screen

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_delete_dialog)

        val deleteBtn = dialog.findViewById<Button>(R.id.deleteBtn)
        val editBtn = dialog.findViewById<Button>(R.id.editBtn)
        val editDeleteET = dialog.findViewById<EditText>(R.id.editDeleteET)
        editDeleteET.setText(noteText)

        editBtn.setOnClickListener {
            editNote(id, editDeleteET.text.toString())
            dialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            displayDeleteConformationDialog(id)
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showAddNoteDialog() {
        val dialog = Dialog(this, R.style.Theme_AppCompat_DayNight)
        dialog.setContentView(R.layout.add_note_dialog)
        dialog.setCanceledOnTouchOutside(true)


        val addBtnD = dialog.findViewById<Button>(R.id.addBtnD)
        val addNoteET = dialog.findViewById<EditText>(R.id.addNoteET)

        addBtnD.setOnClickListener {
            if (addNoteET.text.isNotEmpty()) {
                databaseHelper.saveNotes(addNoteET.text.toString())
                addNoteET.text.clear()
                updateRV()
                dialog.dismiss()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Type something to add a note",
                    Toast.LENGTH_LONG
                ).show()
                // this Toast is not showing
            }
        }
        dialog.show()
    }

   private fun displayDeleteConformationDialog(id: Int) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                deleteNote(id)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Delete Confirmation")
        alert.show()
    }

}
