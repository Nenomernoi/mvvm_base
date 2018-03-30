package org.mainsoft.basewithkodein.util

import android.app.Activity
import android.content.Context
import android.util.Base64
import androidx.content.edit

class Setting(private var context: Context) {

    object Param {
        internal var PREF_NAME = Setting::class.java.name + "_preference"
        internal var TAG_TOKEN = "token"
    }

    private var token: String? = null

    init {
        restore()
    }

    private fun restore() {
        val pref = context.getSharedPreferences(Param.PREF_NAME, Activity.MODE_PRIVATE)
        token = pref.getString(Param.TAG_TOKEN, null)
    }

    fun saveAll() {
        context.getSharedPreferences(Param.PREF_NAME, Activity.MODE_PRIVATE)
                .edit {
                    putString(Param.TAG_TOKEN, token)
                }
    }

    fun logOut() {
        token = null
        saveAll()
    }

    fun getToken(): String {
        return if (token == null) Param.TAG_TOKEN else String(Base64.decode(token, Base64.DEFAULT))
    }

    fun setToken(token: String?) {

        if (token == null) {
            this.token = null
            return
        }

        this.token = Base64.encodeToString(token.toByteArray(), Base64.DEFAULT)
    }
}