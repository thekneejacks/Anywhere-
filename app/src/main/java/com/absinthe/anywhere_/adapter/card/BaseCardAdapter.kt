package com.absinthe.anywhere_.adapter.card

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity
import com.absinthe.anywhere_.view.card.CardItemView
import com.absinthe.anywhere_.view.card.StreamItemView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.card.MaterialCardView

class BaseCardAdapter(
) : BaseQuickAdapter<AnywhereEntity, BaseViewHolder>(0) {

  private val selectedIndex = mutableListOf<Int>()

  override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return createBaseViewHolder(CardItemView(context, StreamItemView(context)))
  }

  @Suppress("UNCHECKED_CAST")
  override fun convert(holder: BaseViewHolder, item: AnywhereEntity) {

    val itemView = holder.itemView as CardItemView<*>
    itemView.appName.text = item.appName
    itemView.cardBackground.setImageDrawable(null)



    if (!selectedIndex.contains(holder.layoutPosition)) {
      (holder.itemView as MaterialCardView).apply {
        scaleX = 1.0f
        scaleY = 1.0f
        isChecked = false
      }
    }
  }

  /*override fun onMove(fromPosition: Int, toPosition: Int) {
    data.add(toPosition, data.removeAt(fromPosition))
    notifyItemMoved(fromPosition, toPosition)
  }

  override fun onSwiped(position: Int) { /* Do nothing */
  }*/

  override fun getItemId(position: Int): Long = data[position].id.toLong()

  fun clickItem(v: View, position: Int) {
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
