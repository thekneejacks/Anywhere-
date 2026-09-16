package com.absinthe.anywhere_.utils.handler

import android.annotation.SuppressLint
import android.content.Context
import com.absinthe.anywhere_.model.ExtraBean
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.CommandUtils
import java.lang.ref.WeakReference

@SuppressLint("WrongCommentType")
object Opener {

  private var context: WeakReference<Context>? = null
  private var item: AnywhereEntity? = null
  private var command: String? = null
  //private var type: Int = TYPE_NONE
  private var extraItem: ExtraBean.ExtraItem? = null
  private var extraItems: Array<ExtraBean.ExtraItem>? = null

  fun with(context: Context): Opener {
    this.context = WeakReference(context)
    //type = TYPE_NONE
    item = null
    command = null
    extraItem = null
    extraItems = null
    return this
  }

  fun load(item: AnywhereEntity): Opener {
    //type = TYPE_ENTITY
    this.item = item
    return this
  }

  @Throws(NullPointerException::class)
  fun open() {
    //openShellSimple()
  }

  private fun openShellSimple() {
    CommandUtils.execAdbCmd(command!!)

  }

}
