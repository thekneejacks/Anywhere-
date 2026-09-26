package com.absinthe.anywhere_.utils

import android.content.Context
import android.os.Environment
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.model.BackupBean
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object StorageUtils {
  /* Checks if external storage is available for read and write */
  val isExternalStorageWritable: Boolean
    get() {
      val state = Environment.getExternalStorageState()
      return Environment.MEDIA_MOUNTED == state
    }

  /**
   * Export Anywhere- entities to json string
   *
   * @return json string
   */
  fun exportAnywhereEntityJsonString(): String? {
    val anywhereList = AnywhereApplication.sRepository.sortedEntities
    val finalList = mutableListOf<AnywhereEntity>()

    return if (anywhereList == null) {
      null
    } else {
      for (ae in anywhereList) {
        finalList.add(ae)
      }
      val backupBean = BackupBean(finalList)
      Gson().toJson(backupBean)
    }
  }

  suspend fun restoreFromJson(context: Context, content: String) {
    try {
      val backupBean = Gson().fromJson(content, BackupBean::class.java)
      if (backupBean == null) {
        withContext(Dispatchers.Main) {
          ToastUtil.makeText(R.string.toast_backup_file_error)
        }
      } else {
        val aeList = mutableListOf<AnywhereEntity>()

        for (ae in backupBean.anywhereList) {
          aeList.add(ae)
        }
        AnywhereApplication.sRepository.insert(aeList)

        withContext(Dispatchers.Main) {
          ToastUtil.makeText(context.getString(R.string.toast_restore_success))
        }
      }
    } catch (_: Exception) {
      //Timber.e(e)

      try {
        val entity = Gson().fromJson(content, AnywhereEntity::class.java)

        AnywhereApplication.sRepository.insert(entity)

        withContext(Dispatchers.Main) {
          ToastUtil.makeText(context.getString(R.string.toast_restore_success))
        }
      } catch (_: Exception) {
        //Timber.e(e)
        withContext(Dispatchers.Main) {
          ToastUtil.makeText(R.string.toast_backup_file_error)
        }
      }
    }
  }

  /*fun webdavBackup() {
    GlobalScope.launch(Dispatchers.IO) {
      val sardine = OkHttpSardine()
      sardine.setCredentials(GlobalValues.webdavUsername, GlobalValues.webdavPassword)

      try {
        val hostDir = GlobalValues.webdavHost + URLManager.BACKUP_DIR

        while (!sardine.exists(hostDir)) {
          sardine.createDirectory(hostDir)
          delay(300)
        }

        val backupName =
          "Anywhere-Backups-${AppTextUtils.webDavFormatDate}-${BuildConfig.VERSION_NAME}.awbackups"

        exportAnywhereEntityJsonString()?.let { content ->
          CipherUtils.encrypt(content)?.let { encrypted ->
            if (!sardine.exists(hostDir + backupName)) {
              sardine.put(hostDir + backupName, encrypted.toByteArray())

              withContext(Dispatchers.Main) {
                ToastUtil.makeText(R.string.toast_backup_success)
              }
            }
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
          ToastUtil.makeText("Backup failed: $e")
        }
      }
    }
  }*/
}
