package com.example.myapplication.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        copyDatabase(context)  // Chama o método para copiar o banco de dados
    }

    override fun onCreate(db: SQLiteDatabase) {
          }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
     }

    @Throws(IOException::class)
    private fun copyDatabase(context: Context) {
        val databasePath = context.getDatabasePath(DATABASE_NAME).absolutePath
        val dbFile = File(databasePath)

        if (!dbFile.exists()) {
            context.assets.open(DATABASE_NAME).use { inputStream ->
                FileOutputStream(dbFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                }
            }
            Log.d("MyDatabaseHelper", "Database copied to $databasePath")
        } else {
            Log.d("MyDatabaseHelper", "Database already exists at $databasePath")
        }
    }

    // Método para inserir um produto
    fun insertProduct(nome: String, descricao: String, preco: Double) {
        val db = this.writableDatabase
        val newId = getNextId(db)

        val values = ContentValues().apply {
            put("id", newId)
            put("nome", nome)
            put("descricao", descricao)
            put("preco", preco)
        }
        val result = db.insert("produtos", null, values)
        if (result == -1L) {
            Log.e("MyDatabaseHelper", "Failed to insert row into produtos")
        } else {
            Log.d("MyDatabaseHelper", "Row inserted into produtos with id: $result")
        }
        db.close()
    }

    // Método para obter o próximo ID
    private fun getNextId(db: SQLiteDatabase): Int {
        var maxId = 0
        val cursor = db.rawQuery("SELECT MAX(id) FROM produtos", null)
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0)
        }
        cursor.close()
        return maxId + 1
    }
    fun deleteProduct(productId: Int) {
        val db = this.writableDatabase
        db.delete("produtos", "id = ?", arrayOf(productId.toString()))
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "Produtos.db"
        private const val DATABASE_VERSION = 1
    }
}
