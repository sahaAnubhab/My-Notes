package com.example.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {
    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDescription = "Description"
    val dbVersion =1
//    CREATE TABLE IF NOT EXIST MyNotes (ID INTEGER PRIMARY KEY, Title TEXT, Description TEXT);
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+ dbTable +" ("+ colID +" INTEGER PRIMARY KEY,"+
            colTitle + " TEXT, "+ colDescription +" TEXT);"
    var sqlDB : SQLiteDatabase? = null


    constructor(context: Context){
        var db= DatabaseHelperNotes(context)
        sqlDB= db.writableDatabase
    }

    inner class DatabaseHelperNotes(private val context: Context): SQLiteOpenHelper(context,dbName,null, dbVersion) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(context, "Database has been created", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $dbTable")
        }
    }

    fun insert(value: ContentValues): Long {
        return sqlDB!!.insert(dbTable, "", value)
    }

    fun query(projection: Array<String>?, selection:String, selectionArgs: Array<String>, sortOrder: String): Cursor{
        val qb= SQLiteQueryBuilder()
        qb.tables= dbTable
        val cursor = qb.query(sqlDB, null, null, null, null, null, sortOrder)
        return cursor
    }

    fun delete(selection:String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.delete(dbTable,selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

}