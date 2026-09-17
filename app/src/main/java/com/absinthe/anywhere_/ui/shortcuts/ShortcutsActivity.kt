package com.absinthe.anywhere_.ui.shortcuts

import android.content.Intent
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.utils.CommandUtils

class ShortcutsActivity : BaseActivity<ViewBinding>() {

  override fun setViewBinding(): Nothing? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    handleIntent(intent)
    super.onCreate(savedInstanceState)
  }

  override fun onNewIntent(intent: Intent) {
    handleIntent(intent)
    super.onNewIntent(intent)
  }

  private fun handleIntent(intent: Intent) {
    intent.getStringExtra(Const.INTENT_EXTRA_SHORTCUTS_ID)?.let { id ->
      AnywhereApplication.sRepository.getParamById(id)?.let { param ->
        CommandUtils.execShizukuCmd(param)
      }
    }
  }
}
