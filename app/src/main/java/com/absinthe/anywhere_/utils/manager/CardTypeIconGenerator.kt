package com.absinthe.anywhere_.utils.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.absinthe.anywhere_.R

object CardTypeIconGenerator {

  @SuppressLint("UseCompatLoadingForDrawables")
  fun getAdvancedIcon(context: Context, size: Int): Drawable {
    val foreDrawable = context.getDrawable(R.drawable.ic_card_shell)?.apply {
      setTintList(ColorStateList.valueOf(Color.parseColor("#66FFFFFF")))
    }
    val backDrawable = context.getDrawable(R.drawable.bg_circle)?.apply {
      val colorRes = context.getColor(R.color.material_deep_purple_300)
      setTintList(ColorStateList.valueOf(colorRes))
    }
    return LayerDrawable(listOf(backDrawable, foreDrawable).toTypedArray()).apply {
      val inset = size / 4
      setLayerInset(1, inset, inset, inset, inset)
      setBounds(0, 0, size, size)
    }
  }

}
