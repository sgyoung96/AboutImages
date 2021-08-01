package com.example.imagedb.db

object Constants {
    val DB_NAME = "IMAGESAMPLE"
    val TABLE_NAME = "IMAGESAVE"
    val COL_FLAG = "IMAGEFLAG"
    val COL_IMAGE = "IMAGE"

    fun insertImageQuery(flag: String, image: String): String {
        return "INSERT INTO ${Constants.TABLE_NAME}" + " " +
               "(${Constants.COL_FLAG}, ${Constants.COL_IMAGE})" + " " +
               "VALUES ('$flag', $image)" + ";"
    }

    fun getImageQuery(flag: String): String {
        return "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.COL_FLAG} = '$flag';"
    }
}