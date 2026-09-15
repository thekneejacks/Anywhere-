package com.absinthe.anywhere_.utils

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import androidx.annotation.ChecksSdkIntAtLeast
import com.absinthe.anywhere_.BuildConfig
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.model.viewholder.FlowStepBean
import com.absinthe.anywhere_.utils.handler.URLSchemeHandler
import com.absinthe.anywhere_.utils.manager.URLManager
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object AppUtils {
  /**
   * Take a persistable URI permission grant that has been offered. Once
   * taken, the permission grant will be remembered across device reboots.
   * Only URI permissions granted with
   * [Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION] can be persisted. If
   * the grant has already been persisted, taking it again will touch
   * [UriPermission.getPersistedTime].
   *
   */
  @SuppressLint("WrongConstant")
  fun takePersistableUriPermission(context: Context, uri: Uri, intent: Intent) {
    val takeFlags = (intent.flags
      and (Intent.FLAG_GRANT_READ_URI_PERMISSION
      or Intent.FLAG_GRANT_WRITE_URI_PERMISSION))
    // Check for the freshest data.
    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
  }
}

/**
 * From drakeet
 */
fun doOnMainThreadIdle(action: () -> Unit, timeout: Long? = null) {
  val handler = Handler(Looper.getMainLooper())

  val idleHandler = MessageQueue.IdleHandler {
    handler.removeCallbacksAndMessages(null)
    action()
    return@IdleHandler false
  }

  fun setupIdleHandler(queue: MessageQueue) {
    if (timeout != null) {
      handler.postDelayed({
        queue.removeIdleHandler(idleHandler)
        action()
        if (BuildConfig.DEBUG) {
          //Timber.d("${timeout}ms timeout!")
        }
      }, timeout)
    }
    queue.addIdleHandler(idleHandler)
  }

  if (Looper.getMainLooper() == Looper.myLooper()) {
    setupIdleHandler(Looper.myQueue())
  } else {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      setupIdleHandler(Looper.getMainLooper().queue)
    } else {
      handler.post { setupIdleHandler(Looper.myQueue()) }
    }
  }
}
