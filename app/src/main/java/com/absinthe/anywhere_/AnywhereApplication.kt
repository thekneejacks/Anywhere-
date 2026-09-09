package com.absinthe.anywhere_

import android.app.Application
import com.absinthe.anywhere_.database.AnywhereRepository
import com.absinthe.anywhere_.model.Settings
import com.absinthe.libraries.utils.utils.Utility

class AnywhereApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    /*if (BuildConfig.DEBUG) {
      ////Timber.plant(ThreadAwareDebugTree())
    } else {
      //checkSignature()
      //PoliceMan.checkApplicationClass(this)
      //PoliceMan.checkPMProxy(this)
      ////Timber.plant(ReleaseTree())
      /*AppCenter.start(
        this, BuildConfig.APP_CENTER_SECRET,
        Analytics::class.java, Crashes::class.java
      )*/
    }*/

    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      HiddenApiBypass.addHiddenApiExemptions("")
    }*/

    //app = this
    //Once.initialise(this)
    Settings.initPrefs(this)
    //Settings.init()
    Utility.init(this)
    //Sui.init(BuildConfig.APPLICATION_ID)
    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    //Global.start()
    sRepository = AnywhereRepository(this)

    /*AccessibilityApi.apply {
      BASE_SERVICE_CLS = IzukoService::class.java
      GESTURE_SERVICE_CLS = IzukoService::class.java
    }*/
  }

  companion object {
    lateinit var sRepository: AnywhereRepository
    //lateinit var app: AnywhereApplication
  }
}
