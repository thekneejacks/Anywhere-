package com.absinthe.anywhere_.model

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.absinthe.anywhere_.AnywhereApplication

object Settings {

  //val iconPackManager by lazy { IconPackManager() }
  //var iconPack: IconPack? = null
  var prefs : SharedPreferences? = null

  /*val date: String by lazy {
    val dateFormat = SimpleDateFormat("MM-dd", Locale.CHINA)
    dateFormat.format(Date())
  }*/

  /*fun init() {
    //setLogger()
    //initIconPackManager()
  }*/

  /*fun getTheme(): Int {
    /*return when (GlobalValues.darkMode) {
      Const.DARK_MODE_OFF, "" -> AppCompatDelegate.MODE_NIGHT_NO
      Const.DARK_MODE_ON -> AppCompatDelegate.MODE_NIGHT_YES
      Const.DARK_MODE_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
      Const.DARK_MODE_BATTERY -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
      Const.DARK_MODE_AUTO -> UxUtils.getAutoDarkMode()
      else -> AppCompatDelegate.MODE_NIGHT_NO
    }*/
    return AppCompatDelegate.MODE_NIGHT_YES
  }*/

  /*fun setLogger() {
    GlobalValues.sIsDebugMode = BuildConfig.DEBUG or GlobalValues.sIsDebugMode
  }*/

  /*fun initIconPackManager() {
    iconPack = iconPackManager.getAvailableIconPacks(true)[GlobalValues.iconPack]
  }*/

  fun initPrefs(application: AnywhereApplication) {
    prefs = PreferenceManager.getDefaultSharedPreferences(application)
    //MMKV.initialize(application)

    /*if (!Once.beenDone(Once.THIS_APP_INSTALL, OnceTag.MMKV_MIGRATE)) {
      val sp = application.getSharedPreferences(GlobalValues.spName, MODE_PRIVATE)
      MMKV.mmkvWithID(GlobalValues.spName)?.importFromSharedPreferences(sp)
      Once.markDone(OnceTag.MMKV_MIGRATE)
    }*/
  }
}
