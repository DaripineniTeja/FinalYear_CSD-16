package com.example.reportflow.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SessionManager(val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE)

    companion object {
        const val USER_ID = "id"
        const val USER_NAME = "name"
        const val USER_EMAIL = "email"
        const val USER_PASSWORD = "password"
        const val USER_MOBILE = "mobile"
        const val USER_TYPE = "type"
        const val USER_ROLE = "role"
        const val USER_LOCATION = "location"
    }

    // Save user login state
    fun saveLoginState(
        id: String,
        name: String,
        email: String,
        password: String,
        mobile: String,
        type: String,
        role: String,
        location: String
    ) {
        prefs.edit().apply {
            putString(USER_ID, id)
            putString(USER_NAME, name)
            putString(USER_EMAIL, email)
            putString(USER_PASSWORD, password)
            putString(USER_MOBILE, mobile)
            putString(USER_TYPE, type)
            putString(USER_ROLE, role)
            putString(USER_LOCATION, location)
            apply()
        }
    }

    // Check if the user is logged in
    fun isLoggedIn(): Boolean {
        val userId = prefs.getString(USER_ID, null)
        return userId != null
    }

    // Get user ID
    fun getUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    // Get user name
    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    // Get user email
    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    // Get user password
    fun getUserPassword(): String? {
        return prefs.getString(USER_PASSWORD, null)
    }

    // Get user mobile
    fun getUserMobile(): String? {
        return prefs.getString(USER_MOBILE, null)
    }
    // Get user mobile
    fun getUserLocation(): String? {
        return prefs.getString(USER_LOCATION, null)
    }

    // Get user type
    fun getUserType(): String? {
        return prefs.getString(USER_TYPE, null)
    }

    // Get user year
    fun getUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    // Clear login state (logout)
    fun clearLoginState() {
        prefs.edit().apply {
            clear()
            apply()
        }
    }
}