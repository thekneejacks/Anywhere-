package com.absinthe.anywhere_.ui.shortcuts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.utils.CommandUtils

@kotlinx.serialization.InternalSerializationApi
class ShortcutsActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    handleIntent(intent)
    super.onCreate(savedInstanceState)
    finish()
  }
  private fun handleIntent(intent: Intent) {
    intent.getStringExtra(Const.INTENT_EXTRA_SHORTCUTS_ID)?.let { id ->
      AnywhereApplication.sRepository.getParamById(id)?.let { param ->
        CommandUtils.execShizukuCmd(param)
      }
    }
  }
}
