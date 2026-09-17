package com.absinthe.anywhere_.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.constants.GlobalValues
import com.absinthe.anywhere_.constants.GlobalValues.setsCategory
import com.absinthe.anywhere_.databinding.ActivityMainBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.model.database.PageEntity
import com.absinthe.anywhere_.ui.backup.BackupActivity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity
import com.absinthe.anywhere_.utils.UxUtils
import com.absinthe.anywhere_.utils.manager.URLManager
import com.absinthe.anywhere_.viewmodel.AnywhereViewModel
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

  private val viewModel by viewModels<AnywhereViewModel>()
  private lateinit var mObserver: Observer<List<PageEntity>?>

  //private var isBound = false
  private var isTitleShown = false
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
    initObserver()
    getAnywhereIntent(intent)

    checkCardCategory()


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
    getAnywhereIntent(intent)
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    //loadBackground(GlobalValues.backgroundUri)
    mToggle?.onConfigurationChanged(newConfig)
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    UxUtils.tintToolbarIcon(this, menu, mToggle)
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

    //binding.fullDraggableContainer.setEnableDrawer(GlobalValues.isPages)


    //initFab()
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

          // 当ViewPager切换页面时，改变页码
          registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
              super.onPageSelected(position)
              val pos = if (position >= it.size) it.size - 1 else position
              setsCategory(it[pos].title, pos)
            }
          })


          getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

          //isUserInputEnabled = GlobalValues.isPages
          isUserInputEnabled = false
          /*if (GlobalValues.isPages) {
            setCurrentItem(GlobalValues.currentPage, false)
          } else {*/
          setCurrentItem(0, false)
          //}
        }
      }
    }

    supportActionBar?.let {
      it.setHomeButtonEnabled(false)
      it.setDisplayHomeAsUpEnabled(false)
      binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
  }


  private fun initObserver() {
    mObserver = Observer { pageEntities ->
      if (pageEntities == null) return@Observer

      AnywhereApplication.sRepository.allPageEntities.removeObserver(mObserver)

      if (pageEntities.isEmpty() && !isPageInit) {
        val pe = PageEntity().apply {
          title = GlobalValues.category
          priority = 1
        }
        AnywhereApplication.sRepository.insertPage(pe)
        isPageInit = true
      }
    }

    AnywhereApplication.sRepository.allPageEntities.observe(this, mObserver)

    viewModel.background.observe(this) { s: String ->
      /*GlobalValues.backgroundUri = s

      if (s.isNotEmpty()) {
        //loadBackground(GlobalValues.backgroundUri)
        UxUtils.setAdaptiveToolbarTitleColor(this@MainActivity, binding.tsTitle)
        UxUtils.setActionBarTransparent(this)
      }*/
    }
    viewModel.shouldShowFab.observe(this) {
      binding.fab.isVisible = it
    }
  }

  private fun getAnywhereIntent(intent: Intent) {
    val action = intent.action
    //Timber.d("action = %s", action)

    if (action == null || action == Intent.ACTION_VIEW) {
      intent.data?.let {
        //Timber.d("Received Url = %s", it.toString())
        //Timber.d("Received path = %s", it.path)
        processUri(it)
      }
    }
  }

  private fun processUri(uri: Uri) {
    if (uri.host == URLManager.URL_HOST) {
      val param1 = uri.getQueryParameter(Const.INTENT_EXTRA_PARAM_1).orEmpty()
      val param2 = uri.getQueryParameter(Const.INTENT_EXTRA_PARAM_2).orEmpty()
      val param3 = uri.getQueryParameter(Const.INTENT_EXTRA_PARAM_3).orEmpty()

      val ae = AnywhereEntity().apply {
        this.appName = "New Shell"
        this.param1 = param1
        this.param2 = param2
        this.param3 = param3
        this.type = AnywhereType.Card.SHELL
      }
      startActivity(Intent(this, EditorActivity::class.java).apply {
        putExtra(EXTRA_ENTITY, ae)
        putExtra(EXTRA_EDIT_MODE, false)
      })
    }
  }

  private fun checkCardCategory() {
    AnywhereApplication.sRepository.allAnywhereEntities.observe(this) {
      lifecycleScope.launch(Dispatchers.IO) {
        it.asSequence().forEach {
          if (AnywhereApplication.sRepository.getPageEntityByTitle(it.category) == null) {
            AnywhereApplication.sRepository.insertPage(
              PageEntity().apply {
                title = it.category ?: AnywhereType.Category.DEFAULT_CATEGORY
                priority = AnywhereApplication.sRepository.allPageEntities.value?.size ?: 0
              }
            )
          }
        }
      }
    }
  }

  companion object {
    private var isPageInit = false
  }
}
