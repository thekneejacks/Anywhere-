package com.absinthe.anywhere_.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.absinthe.anywhere_.model.database.AnywhereEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnywhereRepository(application: Application) {

  var allAnywhereEntities: LiveData<List<AnywhereEntity>>
    private set

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


  fun delete(ae: AnywhereEntity, delayTime: Long = 0L) = GlobalScope.launch(Dispatchers.IO) {
    delay(delayTime)
    mAnywhereDao.delete(ae)
    /*if (AppUtils.atLeastNMR1()) {
      ShortcutsUtils.removeShortcut(ae)
    }*/

  }

  fun getParamById(id: String): String? {
    return mAnywhereDao.getParamById(id)
  }
}
