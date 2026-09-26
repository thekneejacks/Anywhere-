package com.absinthe.anywhere_.ui.editor

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.databinding.ActivityEditorBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.manager.DialogManager
import com.absinthe.anywhere_.utils.manager.DialogManager.showCreatePinnedShortcutDialog


const val EXTRA_ENTITY = "EXTRA_ENTITY"
const val EXTRA_EDIT_MODE = "EXTRA_EDIT_MODE"

const val ACTION_EDITOR = "com.absinthe.anywhere_.intent.action.EDITOR"
const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"

@kotlinx.serialization.InternalSerializationApi
class EditorActivity : BaseActivity<ActivityEditorBinding>() {
  private lateinit var editor: IEditor
  private lateinit var entity: AnywhereEntity

  private val isEditMode by lazy { intent.getBooleanExtra(EXTRA_EDIT_MODE, false) }


  override fun setViewBinding() = ActivityEditorBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {
    if (intent.action == ACTION_EDITOR) {
      entity = AnywhereEntity().apply {
        appName =
          com.blankj.utilcode.util.AppUtils.getAppName(intent.getStringExtra(EXTRA_PACKAGE_NAME))
        param1 = intent.getStringExtra(EXTRA_PACKAGE_NAME).toString()
      }
    } else {
      (intent.getParcelableExtra(EXTRA_ENTITY) as? AnywhereEntity)?.let {
        entity = it
      } ?: run {
        super.onCreate(savedInstanceState)
        finish()
        return
      }
    }

    super.onCreate(savedInstanceState)
  }



  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    if (isEditMode) menuInflater.inflate(R.menu.editor_bottom_bar_edit_mode_menu, menu)
    return true
  }

  override fun initView() {
    if (!this::entity.isInitialized) {
      return
    }
    /*setSupportActionBar(binding.bar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)*/

    editor = ShellEditorFragment()

    val fragment = editor as ShellEditorFragment
    fragment.apply {
      arguments = Bundle().apply {
        putParcelable(EXTRA_ENTITY, entity)
        putBoolean(EXTRA_EDIT_MODE, isEditMode)
      }
    }
    supportFragmentManager
      .beginTransaction()
      .replace(binding.fragmentContainerView.id, fragment)
      .commitNow()

    binding.fab.apply {
      val color = Color.GREEN
      backgroundTintList = ColorStateList.valueOf(color)

      setOnClickListener {
        if (editor.doneEdit()) {
          onBackPressed()
        }
      }
    }

  }


  @SuppressLint("RestrictedApi")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.launch -> {
        editor.tryRunning()
      }
      R.id.add_home_shortcuts -> {
        showCreatePinnedShortcutDialog(this@EditorActivity, entity)
      }
      R.id.delete -> {
        DialogManager.showDeleteAnywhereDialog(this@EditorActivity, entity)
      }
    }

    return super.onOptionsItemSelected(item)
  }

}
