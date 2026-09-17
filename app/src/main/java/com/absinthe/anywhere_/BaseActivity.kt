package com.absinthe.anywhere_

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.utils.manager.ActivityStackManager
import rikka.core.res.isNight
import rikka.material.app.MaterialActivity
import java.lang.ref.WeakReference


@SuppressLint("Registered, MissingSuperCall")
abstract class BaseActivity<T : ViewBinding> : MaterialActivity() {

  var shouldFinishOnResume = false

  private var onDocumentResultAction: ((uri: Uri) -> Unit)? = null
  private lateinit var reference: WeakReference<AppCompatActivity>
  private lateinit var openDocumentResultLauncher: ActivityResultLauncher<Array<String>>

  protected lateinit var binding: T
  protected lateinit var root: View

  protected abstract fun setViewBinding(): T?

  override fun onCreate(savedInstanceState: Bundle?) {
    //Timber.i("onCreate")
    super.onCreate(savedInstanceState)

    reference = WeakReference(this)
    ActivityStackManager.addActivity(reference)

    setViewBinding()?.let {
      binding = it
      root = binding.root
      setContentView(root)
    }
    initView()
  }

  override fun onResume() {
    super.onResume()
    if (shouldFinishOnResume) {
      finish()
    }
  }

  override fun onDestroy() {
    ActivityStackManager.removeActivity(reference)
    super.onDestroy()
  }

  override fun shouldApplyTranslucentSystemBars(): Boolean {
    return true
  }

  override fun computeUserThemeKey(): String {
    return Const.DARK_MODE_ON
  }

  override fun onApplyUserThemeResource(theme: Resources.Theme, isDecorView: Boolean) {

      if (resources.configuration.isNight()) {
        theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Dark, true)
      } else {
        theme.applyStyle(R.style.ThemeOverlay_DynamicColors_Light, true)
      }

    theme.applyStyle(R.style.ThemeOverlay, true)
  }

  override fun onApplyTranslucentSystemBars() {
    super.onApplyTranslucentSystemBars()
    window.statusBarColor = Color.TRANSPARENT
    window.decorView.post {
      window.navigationBarColor = Color.TRANSPARENT
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
      }
    }
  }

  fun setDocumentResult(mimeType: String, action: ((uri: Uri) -> Unit)?) {
    onDocumentResultAction = action
    openDocumentResultLauncher.launch(arrayOf(mimeType))
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun finish() {
    /*if (GlobalValues.isExcludeFromRecent) {
      finishAndRemoveTask()
    } else {
      super.finish()
    }*/
    finishAndRemoveTask()
  }

  protected open fun initView() {}

  fun isNightMode(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_YES > 0
  }

  class OpenDocument : ActivityResultContract<Array<String>, Intent?>() {
    @CallSuper
    override fun createIntent(context: Context, input: Array<String>): Intent {
      return Intent(Intent.ACTION_OPEN_DOCUMENT)
        .putExtra(Intent.EXTRA_MIME_TYPES, input)
        .setType("*/*")
    }

    override fun getSynchronousResult(
      context: Context,
      input: Array<String>
    ): SynchronousResult<Intent?>? = null

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
      return intent.takeIf { resultCode == Activity.RESULT_OK }
    }
  }
}
