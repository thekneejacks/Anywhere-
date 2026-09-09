package com.absinthe.anywhere_.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.model.database.PageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnywhereRepository(application: Application) {

  var allAnywhereEntities: LiveData<List<AnywhereEntity>>
    private set
  val allPageEntities: LiveData<List<PageEntity>>

  private val mAnywhereDao: AnywhereDao =
    AnywhereRoomDatabase.getDatabase(application).anywhereDao()

  private val sortedEntities: LiveData<List<AnywhereEntity>>
    get() = mAnywhereDao.allAnywhereEntitiesOrderByTimeDesc
      /*when (GlobalValues.sortMode) {
      Const.SORT_MODE_TIME_ASC -> mAnywhereDao.allAnywhereEntitiesOrderByTimeAsc
      Const.SORT_MODE_NAME_ASC -> mAnywhereDao.allAnywhereEntitiesOrderByNameAsc
      Const.SORT_MODE_NAME_DESC -> mAnywhereDao.allAnywhereEntitiesOrderByNameDesc
      Const.SORT_MODE_TIME_DESC -> mAnywhereDao.allAnywhereEntitiesOrderByTimeDesc
      else -> mAnywhereDao.allAnywhereEntitiesOrderByTimeDesc
    }*/


  init {
    allPageEntities = mAnywhereDao.allPageEntities
    allAnywhereEntities = sortedEntities
  }

  fun refresh() {
    allAnywhereEntities = sortedEntities
  }

  fun insert(ae: AnywhereEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.insert(ae)

  }

  fun insert(list: List<AnywhereEntity>) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.insert(list)

  }

  fun update(ae: AnywhereEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.update(ae)

  }

  fun update(list: List<AnywhereEntity>) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.update(list)

  }

  fun delete(ae: AnywhereEntity, delayTime: Long = 0L) = GlobalScope.launch(Dispatchers.IO) {
    delay(delayTime)
    mAnywhereDao.delete(ae)
    /*if (AppUtils.atLeastNMR1()) {
      ShortcutsUtils.removeShortcut(ae)
    }*/

  }

  fun delete(list: List<AnywhereEntity>, delayTime: Long = 0L) =
    GlobalScope.launch(Dispatchers.IO) {
      delay(delayTime)
      mAnywhereDao.delete(list)
      /*if (AppUtils.atLeastNMR1()) {
        list.forEach { ShortcutsUtils.removeShortcut(it) }
      }*/

    }

  fun insertPage(pe: PageEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.insertPage(pe)

  }

  fun insertPage(pageList: List<PageEntity>) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.insertPage(pageList)

  }

  fun updatePage(pe: PageEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.updatePage(pe)

  }

  fun deletePage(pe: PageEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.deletePage(pe)

  }

  fun getEntityById(id: String): AnywhereEntity? {
    return mAnywhereDao.getEntityById(id)
  }

  fun getParamById(id: String): String? {
    return mAnywhereDao.getParamById(id)
  }

  fun getPageEntityByTitle(title: String?): PageEntity? {
    if (title == null) {
      return null
    }
    return mAnywhereDao.getPageEntityByTitle(title)
  }
}
