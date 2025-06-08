package com.example.e_waste.data.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    // Gunakan SharedPreferences untuk menyimpan data secara persisten
    private var prefs: SharedPreferences = context.getSharedPreferences("app_session", Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_EMAIL = "user_email"
    }

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun saveEmail(email: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun getEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun clearToken() {
        val editor = prefs.edit()
        editor.remove(AUTH_TOKEN)
        editor.remove(USER_EMAIL)
        editor.apply()
    }
}