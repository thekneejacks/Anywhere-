package com.absinthe.anywhere_.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.databinding.ActivityMainBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.backup.BackupActivity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class MainActivity : BaseActivity<ActivityMainBinding>() {
  private var shouldFinish = false
  private var hasResumed = false
  private var mToggle: ActionBarDrawerToggle? = null

  override fun setViewBinding() = ActivityMainBinding.inflate(layoutInflater)

  override fun onCreate(savedInstanceState: Bundle?) {

    window.apply {
      requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
      sharedElementsUseOverlay = false
    }
    setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

    super.onCreate(savedInstanceState)
  }

  override fun onResume() {
    super.onResume()
    if (shouldFinish && hasResumed) {
      finish()
      return
    }
    hasResumed = true
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    //getAnywhereIntent(intent)
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    //loadBackground(GlobalValues.backgroundUri)
    mToggle?.onConfigurationChanged(newConfig)
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    //UxUtils.tintToolbarIcon(this, menu, mToggle)
    return super.onPrepareOptionsMenu(menu)
  }

  @SuppressLint("RestrictedApi")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.toolbar_settings -> {
        startActivity(Intent(this, BackupActivity::class.java))
      }
    }

    return if (mToggle?.onOptionsItemSelected(item) == true) {
      true
    } else super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    when {
      binding.drawer.isDrawerVisible(GravityCompat.START) -> {
        binding.drawer.closeDrawer(GravityCompat.START)
      }

      else -> {
        //backupIfNeeded()
        finish()
      }
    }
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
        this.type = AnywhereType.Card.SHELL
        appName = "New Shell"
      }
      startActivityForResult(Intent(this, EditorActivity::class.java).apply {
        putExtra(EXTRA_ENTITY, ae)
        putExtra(EXTRA_EDIT_MODE, false)
      }, Const.REQUEST_CODE_OPEN_EDITOR)
    }

    AnywhereApplication.sRepository.allPageEntities.observe(this) {
      if (it.isNotEmpty()) {
        binding.viewPager.apply {
          offscreenPageLimit = 2
          adapter = object : FragmentStateAdapter(this@MainActivity) {
            override fun getItemCount(): Int {
              return it.size.coerceAtLeast(1)
            }

            override fun createFragment(position: Int): Fragment {
              return CategoryCardFragment.newInstance(it[position].title)
            }
          }

          getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
          isUserInputEnabled = false
          setCurrentItem(0, false)
        }
      }
    }

    supportActionBar?.let {
      it.setHomeButtonEnabled(false)
      it.setDisplayHomeAsUpEnabled(false)
      binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
  }
}
