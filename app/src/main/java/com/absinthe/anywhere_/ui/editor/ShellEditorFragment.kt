package com.absinthe.anywhere_.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.databinding.EditorShellBinding
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.CommandUtils

@kotlinx.serialization.InternalSerializationApi
class ShellEditorFragment : Fragment(), IEditor {

  private lateinit var binding: EditorShellBinding

  protected val item by lazy {
    arguments?.getParcelable(EXTRA_ENTITY) ?: AnywhereEntity()
  }
  protected val isEditMode by lazy { requireArguments().getBoolean(EXTRA_EDIT_MODE) }
  //protected val isFromWorkflow by lazy { requireArguments().getBoolean(EXTRA_FROM_WORKFLOW) }
  protected var doneItem: AnywhereEntity = AnywhereEntity()

  fun setBinding(inflater: LayoutInflater, container: ViewGroup?): View {
    binding = EditorShellBinding.inflate(inflater, container, false)
    return binding.root
  }

  fun initView() {
    binding.tietAppName.setText(item.appName)
    binding.etShellContent.setText(item.param1)
  }

  override fun tryRunning() {
    if (binding.etShellContent.text.isNullOrBlank()) {
      binding.etShellContent.error = getString(R.string.bsd_error_should_not_empty)
      return
    }
    val param1 = binding.etShellContent.text.toString()
    CommandUtils.execShizukuCmd(param1)
  }

  override fun doneEdit(): Boolean {
    if (binding.tietAppName.text.isNullOrBlank()) {
      binding.tilAppName.error = getString(R.string.bsd_error_should_not_empty)
      return false
    }
    if (binding.etShellContent.text.isNullOrBlank()) {
      binding.etShellContent.error = getString(R.string.bsd_error_should_not_empty)
      return false
    }

    doneItem = item.copy().apply {
      appName = binding.tietAppName.text.toString()
      param1 = binding.etShellContent.text.toString()
    }

    //if (doneEdit()) return true
    if (isEditMode && doneItem == item) return true

    if (isEditMode) {
      /*if (doneItem.appName != item.appName) {
        if (GlobalValues.shortcutsList.contains(doneItem.id)) {
          if (AppUtils.atLeastNMR1()) {
            ShortcutsUtils.updateShortcut(doneItem)
          }
        }
      }*/
      AnywhereApplication.sRepository.update(doneItem)
    } else {
      doneItem.id = System.currentTimeMillis().toString()
      AnywhereApplication.sRepository.insert(doneItem)
    }

    return true
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val root = setBinding(inflater, container)
    initView()
    return root
  }
}
