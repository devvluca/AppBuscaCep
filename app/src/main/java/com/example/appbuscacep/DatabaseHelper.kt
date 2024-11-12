package com.example.appbuscacep

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CepDatabase.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "Endereco"
        const val COLUMN_ID = "id"
        const val COLUMN_CEP = "cep"
        const val COLUMN_CIDADE = "cidade"
        const val COLUMN_BAIRRO = "bairro"
        const val COLUMN_RUA = "rua"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_CEP TEXT,
            $COLUMN_CIDADE TEXT,
            $COLUMN_BAIRRO TEXT,
            $COLUMN_RUA TEXT
        )"""
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun inserirEndereco(cep: String, cidade: String, bairro: String, rua: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CEP, cep)
            put(COLUMN_CIDADE, cidade)
            put(COLUMN_BAIRRO, bairro)
            put(COLUMN_RUA, rua)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun obterEnderecos(): List<Endereco> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val enderecos = mutableListOf<Endereco>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val cep = getString(getColumnIndexOrThrow(COLUMN_CEP))
                val cidade = getString(getColumnIndexOrThrow(COLUMN_CIDADE))
                val bairro = getString(getColumnIndexOrThrow(COLUMN_BAIRRO))
                val rua = getString(getColumnIndexOrThrow(COLUMN_RUA))
                enderecos.add(Endereco(id, cep, cidade, bairro, rua))
            }
        }
        cursor.close()
        return enderecos
    }

    fun limparHistorico() {
        val db = writableDatabase
        db.execSQL("DELETE FROM Endereco")
        db.close()
    }

    fun resetDatabase() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS Endereco") // Exclui a tabela
        onCreate(db) // Recria a tabela
        db.close()
    }


}

data class Endereco(val id: Long, val cep: String, val cidade: String, val bairro: String, val rua: String)
