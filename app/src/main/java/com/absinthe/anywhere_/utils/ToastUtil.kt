package com.absinthe.anywhere_.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import com.absinthe.anywhere_.AwContextWrapper
import com.blankj.utilcode.util.Utils

object ToastUtil {

  private val contextWrapper by lazy { AwContextWrapper(Utils.getApp()) }
  private val handler = Handler(Looper.getMainLooper())

  /**
   * make a toast via a string
   *
   * @param context context
   * @param text a string text
   */
  fun makeText(context: Context, text: String) {
    if (Looper.getMainLooper().thread === Thread.currentThread()) {
      Toasty.show(context, text)
    } else {
      handler.post { Toasty.show(context, text) }
    }
  }

  /**
   * make a toast via a resource id
   *
   * @param context context
   * @param resId a string resource id
   */
  fun makeText(context: Context, @StringRes resId: Int) {
    makeText(context, context.getString(resId))
  }

  @JvmStatic
  fun makeText(text: String) {
    makeText(contextWrapper, text)
  }

  @JvmStatic
  fun makeText(@StringRes resId: Int) {
    makeText(contextWrapper, resId)
  }

  /**
   * <pre>
   * author : Absinthe
   * time : 2020/08/12
   * </pre>
   */
  object Toasty {

    fun show(context: Context, message: String) {
      show(context, message, Toast.LENGTH_SHORT)
    }

    private fun show(context: Context, message: String, duration: Int) {
      if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(context, message, duration).show()
      } else {
        Handler(Looper.getMainLooper()).post {
          Toast.makeText(context, message, duration).show()
        }
      }
    }

    /*private fun showInternal(context: Context, message: String, duration: Int) {
      toast?.get()?.cancel()
      toast = null

      Toast(context).also {
        it.duration = duration
        it.setText(message)
        toast = WeakReference(ToastCompat(context, it))
      }.show()

    }*/
  }
}
