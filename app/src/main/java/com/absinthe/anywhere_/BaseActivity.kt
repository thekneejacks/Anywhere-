package com.absinthe.anywhere_

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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

  private lateinit var reference: WeakReference<AppCompatActivity>

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
}
