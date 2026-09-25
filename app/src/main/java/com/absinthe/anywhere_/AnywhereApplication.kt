package com.absinthe.anywhere_

import android.app.Application
import com.absinthe.anywhere_.database.AnywhereRepository

class AnywhereApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    sRepository = AnywhereRepository(this)
  }

  companion object {
    lateinit var sRepository: AnywhereRepository
  }
}
