package com.example.notesapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, "details.db", null, 1) {
    private val sqLiteDatabase: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("create table notes (NoteText text)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}


    fun saveNotes(text: String) {
        val contentValues = ContentValues()
        contentValues.put("NoteText", text)
        // ContentValues "put"s a Key_Value pair. The "Key" must match the attributes of the DB (line 13)
        // if there are other attributes (columns) in the DB, I need to "put" their values in contentValues too.
        sqLiteDatabase.insert("notes", null, contentValues)
        // "nullColumnHack" is used when I need to enter a null value of a specific column
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