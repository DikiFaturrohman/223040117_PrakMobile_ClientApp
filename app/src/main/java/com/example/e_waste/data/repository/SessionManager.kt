package com.example.e_waste.data.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private var authToken: String? = null

    fun saveToken(token: String) {
        authToken = token
    }

    fun getToken(): String? {
        return authToken
    }

    fun clearToken() {
        authToken = null
    }
}