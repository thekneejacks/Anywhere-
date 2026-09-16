package com.absinthe.anywhere_.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.constants.AnywhereType
import com.absinthe.anywhere_.database.AnywhereRepository
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.model.database.PageEntity

class AnywhereViewModel(application: Application) : AndroidViewModel(application) {

  private val mRepository: AnywhereRepository = AnywhereApplication.sRepository
  val allAnywhereEntities: LiveData<List<AnywhereEntity>> = mRepository.allAnywhereEntities
  var shouldShowFab: MutableLiveData<Boolean> = MutableLiveData()
  var background: MutableLiveData<String> = MutableLiveData()
    private set

  fun insert(ae: AnywhereEntity) {
    mRepository.insert(ae)
  }

  fun update(ae: AnywhereEntity) {
    mRepository.update(ae)
  }

  fun delete(ae: AnywhereEntity) {
    mRepository.delete(ae)
  }




  /*fun startCollector(activity: Activity, listener: OnStartCollectorListener) {
    when (GlobalValues.workingMode) {
      Const.WORKING_MODE_URL_SCHEME -> {
        ToastUtil.makeText(R.string.toast_works_on_root_or_shizuku)
      }
      Const.WORKING_MODE_SHIZUKU -> {
        if (PermissionUtils.isGrantedDrawOverlays()) {
          if (ShizukuHelper.checkPermission(activity)) {
            listener.onStart()
          }
        } else {
          if (AppUtils.atLeastR()) {
            ToastUtil.makeText(R.string.toast_overlay_choose_anywhere)
          }
          PermissionUtils.requestDrawOverlays(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {
              if (ShizukuHelper.checkPermission(activity)) {
                listener.onStart()
              }
            }

            override fun onDenied() {}
          })
        }
      }
      Const.WORKING_MODE_ROOT -> {
        if (PermissionUtils.isGrantedDrawOverlays()) {
          if (Shell.isAppGrantedRoot() == true) {
            listener.onStart()
          } else {
            //Timber.d("ROOT permission denied.")
            ToastUtil.makeText(R.string.toast_root_permission_denied)
            ShellManager.acquireRoot()
          }
        } else {
          if (AppUtils.atLeastR()) {
            ToastUtil.makeText(R.string.toast_overlay_choose_anywhere)
          }
          PermissionUtils.requestDrawOverlays(object : PermissionUtils.SimpleCallback {
            override fun onGranted() {
              if (Shell.isAppGrantedRoot() == true) {
                listener.onStart()
              } else {
                //Timber.d("ROOT permission denied.")
                ToastUtil.makeText(R.string.toast_root_permission_denied)
                ShellManager.acquireRoot()
              }
            }

            override fun onDenied() {}
          })
        }
      }
    }
  }*/

  fun addPage() {
    mRepository.allPageEntities.value?.let { pages ->
      val pe = PageEntity().apply {
        if (pages.isNotEmpty()) {
          var count = 1
          var t = "Page " + (pages.size + count++)
          while (pages.any { it.title == t }) {
            t = "Page " + (pages.size + count++)
          }
          title = t
          priority = pages.size + 1
        } else {
          title = AnywhereType.Category.DEFAULT_CATEGORY
          priority = 1
        }
        type = AnywhereType.Page.CARD_PAGE
      }
      mRepository.insertPage(pe)
    }
  }

  interface OnStartCollectorListener {
    fun onStart()
  }
}
