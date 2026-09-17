package com.absinthe.anywhere_.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.manager.CardTypeIconGenerator

object UxUtils {

  /**
   * Get app icon by package name
   *
   * @param context for get manager
   * @param item    for get package name
   */
  fun getAppIcon(context: Context, item: AnywhereEntity, size: Int): Drawable {
    return CardTypeIconGenerator.getAdvancedIcon(context, item.type, size)
  }


}
