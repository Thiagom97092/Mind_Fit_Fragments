package com.example.mindfit.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.mindfit.data.contract.UserContract
import com.example.mindfit.data.db.UserDbHelper

class UserRepository private constructor(private val context: Context) {
    private val dbHelper = UserDbHelper(context)

    companion object {
        @Volatile private var instance: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(context.applicationContext).also {
                    instance = it
                    Log.d("USER_REPO", "Nueva instancia de UserRepository creada")
                }
            }
        }
    }

    fun registerUser(email: String, password: String): Boolean {
        Log.d("USER_REGISTER", "Iniciando registro para: $email")

        if (userExists(email)) {
            Log.w("USER_REGISTER", "Registro fallido - Email ya existe: $email")
            return false
        }

        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        Log.d("USER_REGISTER", "Hash generado para $email: ${hashedPassword.take(10)}...")

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_EMAIL, email)
            put(UserContract.UserEntry.COLUMN_PASSWORD, hashedPassword)
        }

        return try {
            val result = db.insert(UserContract.UserEntry.TABLE_NAME, null, values)
            if (result != -1L) {
                Log.i("USER_REGISTER", "✅ Registro exitoso | ID: $result | Email: $email")
                true
            } else {
                Log.e("USER_REGISTER", "❌ Error en registro (insert retornó -1)")
                false
            }
        } catch (e: Exception) {
            Log.e("USER_REGISTER", "❌ Error en registro: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    fun loginUser(email: String, password: String): Boolean {
        Log.d("USER_LOGIN", "Intentando login para: $email")

        val db = dbHelper.readableDatabase
        val projection = arrayOf(UserContract.UserEntry.COLUMN_PASSWORD)
        val selection = "${UserContract.UserEntry.COLUMN_EMAIL} = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            val storedHash = cursor.getString(0)
            val verification = BCrypt.verifyer().verify(password.toCharArray(), storedHash)

            if (verification.verified) {
                Log.i("USER_LOGIN", "✅ Login exitoso para: $email")
            } else {
                Log.w("USER_LOGIN", "⚠️ Credenciales incorrectas para: $email")
            }

            verification.verified
        } else {
            Log.w("USER_LOGIN", "⚠️ Usuario no encontrado: $email")
            false
        }.also {
            cursor.close()
            db.close()
        }
    }

    fun userExists(email: String): Boolean {
        Log.v("USER_CHECK", "Verificando existencia de: $email")

        val db = dbHelper.readableDatabase
        val projection = arrayOf(UserContract.UserEntry.COLUMN_EMAIL)
        val selection = "${UserContract.UserEntry.COLUMN_EMAIL} = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            UserContract.UserEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null, null, null
        )

        return cursor.use {
            val exists = it.count > 0
            Log.v("USER_CHECK", "Usuario $email existe: $exists")
            exists
        }.also {
            db.close()
        }
    }

    /*// Método para debug (opcional)
    fun debugLogAllUsers() {
        if (!BuildConfig.DEBUG) return

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${UserContract.UserEntry.TABLE_NAME}", null)

        Log.d("USER_DEBUG", "━━━━ Usuarios Registrados ━━━━")
        while (cursor.moveToNext()) {
            Log.d("USER_DEBUG", "ID: ${cursor.getInt(0)} | Email: ${cursor.getString(1)}")
        }
        Log.d("USER_DEBUG", "━━━━ Total: ${cursor.count} ━━━━")

        cursor.close()
        db.close()
    }

     */
}