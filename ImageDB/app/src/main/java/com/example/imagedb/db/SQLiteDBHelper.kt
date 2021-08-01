package com.example.imagedb.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDBHelper (context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS ${Constants.TABLE_NAME} (${Constants.COL_FLAG} VARCHAR PRIMARY KEY, ${Constants.COL_IMAGE} BLOB)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        val sql = "CREATE TABLE IF NOT EXISTS ${Constants.TABLE_NAME} (${Constants.COL_FLAG} VARCHAR PRIMARY KEY, ${Constants.COL_IMAGE} BLOB)"
        db?.execSQL(sql)
    }
}