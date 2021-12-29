package com.example.notesapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, "details.db", null, 2) {
    // Changing the version No. because of adding a new column (the pk)
    private val sqLiteDatabase: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table notes (pk INTEGER PRIMARY KEY AUTOINCREMENT,NoteText text)")
        // "db?" same as "if(db.isNotEmpty){}"
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //To remove the table if a new version is detected
        db!!.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)  //To go to onCreate() and create the table (new version)
    }


    fun saveNotes(text: String) {
        val contentValues = ContentValues()
        contentValues.put("NoteText", text)
        // ContentValues "put"s a Key_Value pair. The "Key" must match the attributes of the DB (line 13)
        // if there are other attributes (columns) in the DB, I need to "put" their values in contentValues too.
        // There is no need to pass the pk because it's automatically generated
        sqLiteDatabase.insert("notes", null, contentValues)
        // "nullColumnHack" is used when I need to enter a null value of a specific column
    }

    fun readData(): ArrayList<Note> {
        val notes = arrayListOf<Note>()
        var cursor: Cursor? = null
        try {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes", null)
            // "selectionArgs" is to filter the data we get
        } catch (e: SQLException) {
            sqLiteDatabase.execSQL("SELECT * FROM notes")
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val pk =
                    cursor.getInt(0)// The integer value refers to column index (it's same order of its creation, line 14)
                val noteText = cursor.getString(1)
                notes.add(Note(pk, noteText))
            } while (cursor.moveToNext())  //Iterate through the table and populate notes ArrayList
        }
        return notes
    }


    fun updateDate(note: Note): Int {
        val contentValues = ContentValues()
        contentValues.put("NoteText", note.noteText)
        //sqLiteDatabase.close()
        return sqLiteDatabase.update("notes", contentValues, "pk = ${note.pk}", null)
        // if the return > 0 means it worked
        // It returns number of rows
    }

    fun deleteData(note: Note): Int {
        val contentValues = ContentValues()
        contentValues.put("pk", note.pk)
        //sqLiteDatabase.close()
        return sqLiteDatabase.delete("notes", "pk = ${note.pk}", null)

    }
}



/*
The SQLiteOpenHelper is built into Android and allows us to manage our database

It requires:
1/ constructor: SQLiteOpenHelper(context, DB name to be created, factory, DB version number (it's changing when the DB is upgraded))
2/ onCreate function: query for creating DB table
3/ onUpgrade function: when the version No. is increased, it will be executed (Alter commands)

ContentValues is to group data and pass it into the DB instead of passing evey single data individually . To populate table's
columns with data.

 Cursor used to read (retrieve) data from SQLite
 */