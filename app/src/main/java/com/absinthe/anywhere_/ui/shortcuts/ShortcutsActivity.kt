package com.absinthe.anywhere_.ui.shortcuts

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.viewbinding.ViewBinding
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.CommandUtils
import com.absinthe.anywhere_.utils.ToastUtil
import com.absinthe.anywhere_.utils.UxUtils
import com.absinthe.anywhere_.view.app.AnywhereDialogBuilder
import com.absinthe.anywhere_.viewmodel.AnywhereViewModel
import com.absinthe.libraries.utils.extensions.dp
import com.blankj.utilcode.util.Utils

class ShortcutsActivity : BaseActivity<ViewBinding>() {
  private val viewModel by viewModels<AnywhereViewModel>()

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
    intent.action?.let {
      when (it) {
        ACTION_START_ENTITY -> {
          intent.getStringExtra(Const.INTENT_EXTRA_SHORTCUTS_ID)?.let { id ->
            AnywhereApplication.sRepository.getParamById(id)?.let { param ->
              CommandUtils.execShizukuCmd(param)
            }
          }
        }

        Intent.ACTION_CREATE_SHORTCUT -> {
          viewModel.allAnywhereEntities.observe(this) { anywhereEntities: List<AnywhereEntity>? ->
            val arrayAdapter = ArrayAdapter<String>(
              Utils.getApp(),
              android.R.layout.select_dialog_singlechoice
            )

            anywhereEntities?.let { entities ->
              //Timber.d("list = %s", entities)

              for (ae in entities) {
                arrayAdapter.add(ae.appName)
              }

              AnywhereDialogBuilder(this)
                .setAdapter(arrayAdapter) { _: DialogInterface?, i: Int ->
                  val entity = entities[i]
                  val shortcutIntent =
                    Intent(this@ShortcutsActivity, ShortcutsActivity::class.java).apply {
                      if (entities[i].type == AnywhereType.Card.IMAGE) {
                        action = ACTION_START_IMAGE
                        putExtra(Const.INTENT_EXTRA_SHORTCUTS_CMD, entity.param1)
                      } else {
                        action = ACTION_START_ENTITY
                        putExtra(Const.INTENT_EXTRA_SHORTCUTS_ID, entity.id)
                      }
                    }

                  setResult(
                    Activity.RESULT_OK, ShortcutManagerCompat.createShortcutResultIntent(
                      this,
                      ShortcutInfoCompat.Builder(this, entity.id)
                        .setIntent(shortcutIntent)
                        .setShortLabel(entity.appName)
                        .setIcon(
                          IconCompat.createWithAdaptiveBitmap(
                            UxUtils.getAppIcon(
                              this,
                              entity,
                              45
                            ).toBitmap(45.dp, 45.dp)
                          )
                        )
                        .build()
                    )
                  )
                  ToastUtil.makeText(R.string.toast_try_to_add_pinned_shortcut)
                  shouldFinishOnResume = true
                }
                .setOnCancelListener {
                  shouldFinishOnResume = true
                  finish()
                }
                .show()
            }
          }
        }
        else -> shouldFinishOnResume = true
      }
    }
  }

  companion object {
    //const val ACTION_START_COLLECTOR = "START_COLLECTOR"
    const val ACTION_START_ENTITY = "START_ENTITY"
    //const val ACTION_START_FROM_WIDGET = "START_FROM_WIDGET"
    const val ACTION_START_IMAGE = "START_IMAGE" //Old Scheme
    const val ACTION_START_DEVICE_CONTROL = "ACTION_START_DEVICE_CONTROL"
  }
}
