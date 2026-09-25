package com.absinthe.anywhere_.adapter.card

import android.content.Intent
import android.view.ViewGroup
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity
import com.absinthe.anywhere_.view.card.CardItemView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BaseCardAdapter(
) : BaseQuickAdapter<AnywhereEntity, BaseViewHolder>(0) {

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return createBaseViewHolder(CardItemView(context))
  }

  @Suppress("UNCHECKED_CAST")
  override fun convert(holder: BaseViewHolder, item: AnywhereEntity) {

    val itemView = holder.itemView as CardItemView
    itemView.appName.text = item.appName
  }



  override fun getItemId(position: Int): Long = data[position].id.toLong()

  fun clickItem(position: Int) {
    try {
      val item = getItem(position)
      val intent = Intent(context, EditorActivity::class.java).apply {
        putExtra(EXTRA_ENTITY, item)
        putExtra(EXTRA_EDIT_MODE, true)
      }
      context.startActivity(intent)
    } catch (e: IndexOutOfBoundsException) {
      e.printStackTrace()
    }
  }
}
