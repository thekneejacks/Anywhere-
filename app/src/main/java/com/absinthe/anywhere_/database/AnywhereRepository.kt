package com.absinthe.anywhere_.database

import android.app.Application
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@kotlinx.serialization.InternalSerializationApi
class AnywhereRepository(application: Application) {

  private val mainActivity = MainActivity
  private val mAnywhereDao: AnywhereDao =
    AnywhereRoomDatabase.getDatabase(application).anywhereDao()

  val sortedEntities: List<AnywhereEntity>
    get() = mAnywhereDao.allAnywhereEntitiesOrderByTimeDesc
      /*when (GlobalValues.sortMode) {
      Const.SORT_MODE_TIME_ASC -> mAnywhereDao.allAnywhereEntitiesOrderByTimeAsc
      Const.SORT_MODE_NAME_ASC -> mAnywhereDao.allAnywhereEntitiesOrderByNameAsc
      Const.SORT_MODE_NAME_DESC -> mAnywhereDao.allAnywhereEntitiesOrderByNameDesc
      Const.SORT_MODE_TIME_DESC -> mAnywhereDao.allAnywhereEntitiesOrderByTimeDesc
      else -> mAnywhereDao.allAnywhereEntitiesOrderByTimeDesc
    }*/


  fun insert(ae: AnywhereEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.insert(ae)
    mainActivity.reload()
  }

  fun insert(list: List<AnywhereEntity>) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.insert(list)
    mainActivity.reload()
  }

  fun update(ae: AnywhereEntity) = GlobalScope.launch(Dispatchers.IO) {
    mAnywhereDao.update(ae)
    mainActivity.reload()
  }


  fun delete(ae: AnywhereEntity, delayTime: Long = 0L) = GlobalScope.launch(Dispatchers.IO) {
    delay(delayTime)
    mAnywhereDao.delete(ae)
    mainActivity.reload()
  }

  fun getParamById(id: String): String? {
    return mAnywhereDao.getParamById(id)
  }
}
