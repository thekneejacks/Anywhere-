package com.absinthe.anywhere_.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.ImageButton
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.databinding.ActivityMainBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.backup.BackupActivity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class MainActivity : BaseActivity<ActivityMainBinding>() {

  override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {

    window.apply {
      requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
      sharedElementsUseOverlay = false
    }
    setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

    super.onCreate(savedInstanceState)
  }

  @SuppressLint("RestrictedApi")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.toolbar_settings -> {
        startActivity(Intent(this, BackupActivity::class.java))
      }
    }

    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun initView() {
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    binding.toolbar.title = ""


    val addButton: ImageButton = findViewById(R.id.fab)
    addButton.setOnClickListener {
      val ae = AnywhereEntity().apply {
        appName = "New Shell"
      }
      startActivityForResult(Intent(this, EditorActivity::class.java).apply {
        putExtra(EXTRA_ENTITY, ae)
        putExtra(EXTRA_EDIT_MODE, false)
      }, Const.REQUEST_CODE_OPEN_EDITOR)
    }

  }
}
