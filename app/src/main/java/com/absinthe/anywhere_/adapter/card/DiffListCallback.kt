package com.absinthe.anywhere_.adapter.card

import androidx.recyclerview.widget.DiffUtil
import com.absinthe.anywhere_.model.database.AnywhereEntity

class DiffListCallback : DiffUtil.ItemCallback<AnywhereEntity>() {

  override fun areItemsTheSame(oldItem: AnywhereEntity, newItem: AnywhereEntity): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: AnywhereEntity, newItem: AnywhereEntity): Boolean {
    return oldItem.appName == newItem.appName &&
      oldItem.param1 == newItem.param1
  }
}
