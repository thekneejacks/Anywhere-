package com.absinthe.anywhere_

import android.app.Application
import com.absinthe.anywhere_.database.AnywhereRepository
import com.absinthe.libraries.utils.utils.Utility

class AnywhereApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    Utility.init(this)
    sRepository = AnywhereRepository(this)
  }

  companion object {
    lateinit var sRepository: AnywhereRepository
  }
}
