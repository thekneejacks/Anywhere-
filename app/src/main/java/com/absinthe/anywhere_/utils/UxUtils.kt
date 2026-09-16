package com.absinthe.anywhere_.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import com.absinthe.anywhere_.R
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


  fun getEntityIcon(context: Context, entity: AnywhereEntity, size: Int): Drawable =
    try {
      Drawable.createFromStream(
        context.contentResolver.openInputStream(Uri.parse(entity.iconUri)),
        null
      )
    } catch (e: Exception) {
      //Timber.e(e)
      getAppIcon(context, entity, size)
    } ?: getAppIcon(context, entity, size)

  /**
   * Tint the menu icon
   *
   * @param context context
   * @param item    a menu item
   * @param color   color
   */
  fun tintMenuIcon(context: Context, item: MenuItem?, @ColorRes color: Int) {
    if (item == null) {
      return
    }
    val normalDrawable = item.icon ?: return
    val wrapDrawable = DrawableCompat.wrap(normalDrawable)
    DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color))
    item.icon = wrapDrawable
  }

  fun tintToolbarIcon(context: Context, menu: Menu, toggle: ActionBarDrawerToggle?) {

    menu.children.forEach { tintMenuIcon(context, it, R.color.white) }

    toggle?.let {
      //if (type == Const.ACTION_BAR_TYPE_DARK) {
        it.drawerArrowDrawable.color = Color.WHITE
      //} else {
     //   it.drawerArrowDrawable.color = Color.WHITE
      //}
    }
  }
}
