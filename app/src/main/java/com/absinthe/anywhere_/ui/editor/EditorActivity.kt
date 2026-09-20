package com.absinthe.anywhere_.ui.editor

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.databinding.ActivityEditorBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.editor.impl.ShellEditorFragment
import com.absinthe.anywhere_.utils.manager.DialogManager
import com.absinthe.anywhere_.utils.manager.DialogManager.showCreatePinnedShortcutDialog


const val EXTRA_ENTITY = "EXTRA_ENTITY"
const val EXTRA_EDIT_MODE = "EXTRA_EDIT_MODE"

const val ACTION_EDITOR = "com.absinthe.anywhere_.intent.action.EDITOR"
const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"
const val EXTRA_CLASS_NAME = "EXTRA_CLASS_NAME"

class EditorActivity : BaseActivity<ActivityEditorBinding>() {
  private lateinit var editor: IEditor
  private lateinit var entity: AnywhereEntity

  private val isEditMode by lazy { intent.getBooleanExtra(EXTRA_EDIT_MODE, false) }


  override fun setViewBinding() = ActivityEditorBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {
    if (intent.action == ACTION_EDITOR) {
      entity = AnywhereEntity().apply {
        type = AnywhereType.Card.ACTIVITY
        appName =
          com.blankj.utilcode.util.AppUtils.getAppName(intent.getStringExtra(EXTRA_PACKAGE_NAME))
        param1 = intent.getStringExtra(EXTRA_PACKAGE_NAME).toString()
        param2 = intent.getStringExtra(EXTRA_CLASS_NAME).toString()
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

    setUpBottomDrawer()
  }



  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    if (isEditMode) menuInflater.inflate(R.menu.editor_bottom_bar_edit_mode_menu, menu)
    return true
  }

  override fun initView() {
    if (!this::entity.isInitialized) {
      return
    }
    setSupportActionBar(binding.bar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    editor = ShellEditorFragment()

    val fragment = editor as BaseEditorFragment
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

  }

  private fun setUpBottomDrawer() {
    binding.bar.apply {
      if (!isEditMode) {
        navigationIcon?.alpha = 64
        setNavigationOnClickListener(null)
      } else {
        navigationIcon?.alpha = 255
        setNavigationOnClickListener { editor.tryRunning() }
      }
      setOnMenuItemClickListener {
        when (it.itemId) {
          R.id.add_home_shortcuts -> {
            showCreatePinnedShortcutDialog(this@EditorActivity, entity)
          }
          R.id.delete -> {
            DialogManager.showDeleteAnywhereDialog(this@EditorActivity, entity)
          }
        }
        true
      }
    }

    binding.fab.apply {
      val color = //if (entity.color == 0) {
        //context.getColorByAttr(com.google.android.material.R.attr.colorSecondaryContainer)
      //*} else {
        Color.GREEN
      //}*/
      backgroundTintList = ColorStateList.valueOf(color)

      //imageTintList = if (UxUtils.isLightColor(color)) {
       // ColorStateList.valueOf(Color.BLACK)
      //} else {
      //  ColorStateList.valueOf(Color.WHITE)
      //}

      setOnClickListener {
        if (editor.doneEdit()) {
          onBackPressed()
        }
      }
    }
  }


}
