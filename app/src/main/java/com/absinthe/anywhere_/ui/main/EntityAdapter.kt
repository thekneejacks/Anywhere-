package com.absinthe.anywhere_.ui.main
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity

@kotlinx.serialization.InternalSerializationApi
class EntityAdapter(private val dataSet: List<AnywhereEntity>) :
  RecyclerView.Adapter<EntityAdapter.ViewHolder>() {

  /**
   * Provide a reference to the type of views that you are using
   * (custom ViewHolder)
   */
  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.entity_text)
    val entityItem : CardView = view.findViewById(R.id.entity_item)

    val context : Context = view.context

  }

  // Create new views (invoked by the layout manager)
  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
    // Create a new view, which defines the UI of the list item
    val view = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.entity_item, viewGroup, false)

    return ViewHolder(view)
  }

  // Replace the contents of a view (invoked by the layout manager)
  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

    // Get element from your dataset at this position and replace the
    // contents of the view with that element
    viewHolder.textView.text = dataSet[position].appName

    viewHolder.entityItem.setOnClickListener {
      try {
        val item = dataSet[position]
        val intent = Intent(viewHolder.context, EditorActivity::class.java).apply {
          putExtra(EXTRA_ENTITY, item)
          putExtra(EXTRA_EDIT_MODE, true)
        }
        viewHolder.context.startActivity(intent)
      } catch (e: IndexOutOfBoundsException) {
        e.printStackTrace()
      }
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  override fun getItemCount() = dataSet.size


}
