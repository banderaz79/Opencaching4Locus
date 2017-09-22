package com.arcao.opencaching4locus.data.account

import android.content.Context
import android.content.SharedPreferences
import com.arcao.opencaching4locus.model.response.User
import com.github.scribejava.core.model.OAuth1AccessToken

class Account(private val context : Context, val accountType: AccountType) {
    companion object {
        private const val PREF_TOKEN = "TOKEN"
        private const val PREF_SECRET = "SECRET"
        private const val PREF_USERNAME = "USERNAME"
    }

    private val preferences : SharedPreferences = context.getSharedPreferences("ACCOUNT_${accountType.name}", Context.MODE_PRIVATE)


    val authenticated : Boolean get() = preferences.contains(PREF_TOKEN)
    val accessToken: String? get() = preferences.getString(PREF_TOKEN, null)
    val accessSecret: String? get() = preferences.getString(PREF_SECRET, null)
    val userName: String? get() = preferences.getString(PREF_USERNAME, null)

    fun authorize(token: OAuth1AccessToken) {
        preferences.edit().apply {
            putString(PREF_TOKEN, token.token)
            putString(PREF_SECRET, token.tokenSecret)
        }.apply()
    }

    fun updateUser(user: User) {
        preferences.edit().apply {
            putString(PREF_USERNAME, user.username)
        }.apply()
    }

    fun remove() {
        preferences.edit().apply {
            remove(PREF_TOKEN)
            remove(PREF_SECRET)
            remove(PREF_USERNAME)
        }.apply()
    }
}