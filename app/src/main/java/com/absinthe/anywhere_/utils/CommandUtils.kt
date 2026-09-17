package com.absinthe.anywhere_.utils

import com.absinthe.anywhere_.constants.CommandResult
import com.absinthe.anywhere_.model.ShizukuProcess

object CommandUtils {
  fun execShizukuCmd(cmd: String): String {
    //Timber.d(cmd)
    val sb = StringBuilder()

    try {
      sb.append(ShizukuProcess.exec(cmd))
      //Timber.e(sb.toString())
    } catch (e: Exception) {
      //Timber.e(e)
      sb.append(CommandResult.RESULT_SHIZUKU_PERM_ERROR)
    }

    if (sb.toString().isEmpty()) {
      sb.append(CommandResult.RESULT_EMPTY)
    }

    return sb.toString()
  }
}
