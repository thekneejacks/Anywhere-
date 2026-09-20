package com.absinthe.anywhere_

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import rikka.material.app.MaterialActivity


@SuppressLint("Registered, MissingSuperCall")
abstract class BaseActivity<T : ViewBinding> : MaterialActivity() {

  protected lateinit var binding: T
  protected lateinit var root: View

  protected abstract fun setViewBinding(): T?

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setViewBinding()?.let {
      binding = it
      root = binding.root
      setContentView(root)
    }
    initView()
  }

  protected open fun initView() {}
}
