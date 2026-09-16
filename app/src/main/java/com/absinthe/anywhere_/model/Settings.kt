package com.absinthe.anywhere_.model

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.absinthe.anywhere_.AnywhereApplication

object Settings {

  var prefs : SharedPreferences? = null

  fun initPrefs(application: AnywhereApplication) {
    prefs = PreferenceManager.getDefaultSharedPreferences(application)
  }
}
