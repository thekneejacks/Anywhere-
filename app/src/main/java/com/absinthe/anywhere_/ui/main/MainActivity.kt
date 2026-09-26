package com.absinthe.anywhere_.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.backup.BackupActivity
import com.absinthe.anywhere_.ui.editor.EXTRA_EDIT_MODE
import com.absinthe.anywhere_.ui.editor.EXTRA_ENTITY
import com.absinthe.anywhere_.ui.editor.EditorActivity

lateinit var entityAdapter: EntityAdapter
lateinit var recyclerView: RecyclerView

class MainActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    setContentView(R.layout.activity_main)

    val dataset = AnywhereApplication.sRepository.sortedEntities
    entityAdapter = EntityAdapter(dataset)

    recyclerView = findViewById(R.id.entity_recyclerview)
    recyclerView.layoutManager = GridLayoutManager(this, 2)
    recyclerView.adapter = entityAdapter


    val addButton: ImageButton = findViewById(R.id.fab)
    addButton.setOnClickListener {
      val ae = AnywhereEntity().apply {
        appName = "New Shell"
      }
      startActivityForResult(Intent(this, EditorActivity::class.java).apply {
        putExtra(EXTRA_ENTITY, ae)
        putExtra(EXTRA_EDIT_MODE, false)
      }, Const.REQUEST_CODE_OPEN_EDITOR)
    }

    super.onCreate(savedInstanceState)
  }

  @SuppressLint("RestrictedApi")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.toolbar_settings -> {
        startActivity(Intent(this, BackupActivity::class.java))
      }
    }

    return super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }


  companion object {
    fun reload() {
      Handler(Looper.getMainLooper()).postDelayed(
        {
          val dataset = AnywhereApplication.sRepository.sortedEntities
          entityAdapter = EntityAdapter(dataset)
          recyclerView.swapAdapter(entityAdapter, true)
        },
        300
      )
    }


  }

}
