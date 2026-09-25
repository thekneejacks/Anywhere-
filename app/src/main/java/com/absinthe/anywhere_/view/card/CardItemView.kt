package com.absinthe.anywhere_.view.card

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import com.absinthe.anywhere_.R
import com.google.android.material.card.MaterialCardView

@SuppressLint("ViewConstructor")
class CardItemView(context: Context) : MaterialCardView(context) {

  val appName: TextView
  //val icon: AppCompatImageView
  //val badge: ImageView
  //val indicator: ImageView

  val Number.dp: Int get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

  init {
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 95.dp)
    isCheckable = true
    isClickable = true
    isFocusable = true
    translationZ = context.resources.getDimension(R.dimen.cardview_elevation)
    cardElevation = context.resources.getDimension(R.dimen.cardview_elevation)
    radius = context.resources.getDimension(R.dimen.cardview_corner_radius)

    //content.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)


    /*when (content) {
      is StreamItemView -> {
        appName = content.appName
        //icon = content.icon
        //badge = content.badge
        //indicator = content.indicator
        //addView(cardBackground)
      }
      else -> {
        appName = TextView(context)
        //icon = AppCompatImageView(context)
        //badge = ImageView(context)
        //ndicator = ImageView(context)
      }
    }*/

    appName = TextView(context).apply {
      id = generateViewId()
      setTypeface(null, Typeface.BOLD)
      setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
      maxLines = 2
      setPadding(6.dp,4.dp,0,0)
    }
    addView(appName)
    //addView(content)
  }
}
