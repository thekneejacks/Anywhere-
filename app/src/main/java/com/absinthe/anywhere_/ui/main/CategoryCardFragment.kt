package com.absinthe.anywhere_.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.adapter.ItemTouchCallBack
import com.absinthe.anywhere_.adapter.SpacesItemDecoration
import com.absinthe.anywhere_.adapter.card.BaseCardAdapter
import com.absinthe.anywhere_.adapter.card.DiffListCallback
import com.absinthe.anywhere_.adapter.manager.WrapContentStaggeredGridLayoutManager
import com.absinthe.anywhere_.constants.GlobalValues
import com.absinthe.anywhere_.databinding.FragmentCategoryCardBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.doOnMainThreadIdle
import java.lang.ref.WeakReference

class CategoryCardFragment : Fragment() {

  private lateinit var decoration: SpacesItemDecoration

  private lateinit var binding: FragmentCategoryCardBinding
  private lateinit var adapter: BaseCardAdapter
  private lateinit var itemTouchHelper: ItemTouchHelper
  private var isFirstLoadItems = true

  private val listObserver = Observer<List<AnywhereEntity>> { list ->
    if (isFirstLoadItems) {
      isFirstLoadItems = false
      updateItems(list)
    } else {
      doOnMainThreadIdle({
        binding.root.postDelayed({ updateItems(list) }, 300)
      })
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentCategoryCardBinding.inflate(inflater, container, false)
    initView()
    return binding.root
  }

  override fun onResume() {
    super.onResume()
    currentReference = WeakReference(this)

    if (GlobalValues.shortcutListChanged) {
      adapter.notifyDataSetChanged()
      GlobalValues.shortcutListChanged = false
    }
  }

  override fun onDetach() {
    super.onDetach()
    unregisterObservers()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    setRecyclerViewLayoutManager(binding.recyclerView, newConfig)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    menu.findItem(R.id.toolbar_settings).isVisible = true
    super.onPrepareOptionsMenu(menu)
  }


  private fun initView() {
    setHasOptionsMenu(true)
    setupRecyclerView()
    initObservers()
  }

  private fun setupRecyclerView() {
    decoration = SpacesItemDecoration(resources.getDimension(R.dimen.cardview_item_margin).toInt())

    adapter = BaseCardAdapter()

    adapter.apply {
      setDiffCallback(DiffListCallback())
      setOnItemClickListener { _, view, i ->
        clickItem(view, i)
      }
      setHasStableIds(true)
    }

    with(binding.recyclerView) {
      adapter = this@CategoryCardFragment.adapter
      setRecyclerViewLayoutManager(this, resources.configuration)
      addItemDecoration(decoration)
    }

    itemTouchHelper = ItemTouchHelper(ItemTouchCallBack().apply {
      setOnItemTouchListener(adapter)
    }).apply {
      attachToRecyclerView(null)
    }
  }

  private fun initObservers() {
    //GlobalValues.cardModeLiveData.observe(viewLifecycleOwner, cardObserver)
    observeEntitiesList()
  }

  private fun unregisterObservers() {
    //GlobalValues.cardModeLiveData.removeObserver(cardObserver)
    AnywhereApplication.sRepository.allAnywhereEntities.removeObserver(listObserver)
  }

  private fun updateItems(list: List<AnywhereEntity>) {
    adapter.setDiffNewData(

        list.toMutableList()
    )
    //updateWidget(Utils.getApp())
  }

  private fun setRecyclerViewLayoutManager(
    recyclerView: RecyclerView,
    configuration: Configuration
  ) {
    recyclerView.layoutManager =
      if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        val spanCount = 4
        WrapContentStaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
      } else {
        val spanCount =2
        WrapContentStaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
      }
  }

  private fun observeEntitiesList() {
    AnywhereApplication.sRepository.allAnywhereEntities.removeObserver(listObserver)
    AnywhereApplication.sRepository.allAnywhereEntities.observe(viewLifecycleOwner, listObserver)
  }

  companion object {
    var currentReference: WeakReference<CategoryCardFragment>? = null
  }
}
