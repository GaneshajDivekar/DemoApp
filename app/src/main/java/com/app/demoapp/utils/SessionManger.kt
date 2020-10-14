package com.app.demoapp.utils

import android.content.Context
import android.content.SharedPreferences


public class SessionManger(context: Context, prefFileName: String) {

    private val mPrefs: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
        editor = mPrefs.edit()
    }


    fun getCurrentUserLoggedInMode(): Boolean {
        return mPrefs.getBoolean(PREF_KEY_USER_LOGGED_IN_MODE, false)
    }

    fun setCurrentUserLoggedInMode(mode: Boolean) {
        mPrefs.edit().putBoolean(PREF_KEY_USER_LOGGED_IN_MODE, mode).apply()
    }



    fun clearSessionManger() {
        editor!!.clear()
        editor!!.commit()

    }

    fun clearAll() {
        mPrefs.edit().clear().commit()
    }


    companion object {
        const val PREF_FILE_NAME = "demoapp.pref"
         val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"



    }
}


