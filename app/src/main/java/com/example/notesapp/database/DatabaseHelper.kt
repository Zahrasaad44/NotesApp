package com.example.notesapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
        sqLiteDatabase.insert("notes", null, contentValues)
        // "nullColumnHack" is used when I need to enter a null value of a specific column
    }

    fun readData(): ArrayList<Note> {
        val notes = arrayListOf<Note>()
        // Cursor used to read (retrieve) data from SQLite
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes", null)
        // "selectionArgs" is to filter the data we get

        if(cursor.count < 1) { //To handle empty table
            println("No Data Found")
        } else {
            while (cursor.moveToNext()) { //Iterate through the table and populate notes ArrayList
                val pk = cursor.getInt(0)// The integer value refers to column index (it's same order of its creation, line 14)
                val noteText = cursor.getString(1)
                notes.add(Note(pk, noteText))

            }
        }
        return notes
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
 */