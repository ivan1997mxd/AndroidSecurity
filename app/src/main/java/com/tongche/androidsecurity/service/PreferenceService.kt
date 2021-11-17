package com.tongche.androidsecurity.service

import android.R
import android.content.Context
import android.util.Log
import java.util.ArrayList

object PreferencesService {
    private const val PREF_NAME = "PermissionFriendlyAppsPreferences"
    private const val PREF_THEME = "Theme"
    private const val THEME_LIGHT = 0
    private const val THEME_DARK = 1
    private var mTheme = 0
    private val mThemeListeners: ArrayList<ThemeChangesListener> = ArrayList<ThemeChangesListener>()
    val themeId: Int
        get() {
            return if (isThemeDark) R.style.Theme_Material else R.style.Theme_Material_Light_DarkActionBar
        }
    val isThemeDark: Boolean
        get() = mTheme != THEME_LIGHT

    fun addThemeListener(listener: ThemeChangesListener) {
        mThemeListeners.add(listener)
    }

    fun notifyThemeListeners(context: Context, bDarkTheme: Boolean) {
        mTheme = if (bDarkTheme) THEME_DARK else THEME_LIGHT
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(PREF_THEME, mTheme)
        editor.apply()
        Log.i(Constants.LOG_TAG, "New theme stored : $mTheme")
        for (listener in mThemeListeners) {
            listener.onChangeTheme()
        }
    }

    fun loadPreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        mTheme = prefs.getInt(PREF_THEME, THEME_LIGHT)
        Log.i(Constants.LOG_TAG, "Theme loaded : $mTheme")
    }
}