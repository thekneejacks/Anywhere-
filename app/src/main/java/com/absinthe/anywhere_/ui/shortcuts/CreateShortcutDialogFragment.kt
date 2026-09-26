package com.absinthe.anywhere_.ui.shortcuts

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.utils.ShortcutsUtils
import com.absinthe.anywhere_.utils.UxUtils
import com.absinthe.anywhere_.view.app.AnywhereDialogBuilder
import com.absinthe.anywhere_.view.app.AnywhereDialogFragment
import com.absinthe.anywhere_.viewbuilder.entity.CreateShortcutDialogBuilder
import com.blankj.utilcode.util.Utils

@kotlinx.serialization.InternalSerializationApi
class CreateShortcutDialogFragment : AnywhereDialogFragment() {
  private lateinit var mBuilder: CreateShortcutDialogBuilder
  private lateinit var imageResultLauncher: ActivityResultLauncher<String>

  override fun onAttach(context: Context) {
    super.onAttach(context)
    imageResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
      it?.let { uri ->
        mBuilder.ivIcon.setImageURI(uri)
        /*activity?.let { activity ->
          Glide.with(activity.applicationContext)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(mBuilder.ivIcon)
        }*/
        takePersistableUriPermission(requireContext(), it, Intent())

      }

    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val entity = arguments?.getParcelable(EXTRA_ENTITY) ?: AnywhereEntity()
    mBuilder = CreateShortcutDialogBuilder(requireContext())
    val builder = AnywhereDialogBuilder(requireContext())
    mBuilder.apply {
      etName.setText(entity.appName)
      ivIcon.apply {
        setImageDrawable(UxUtils.getAppIcon(Utils.getApp(), 45.dp))
        setOnClickListener {
          imageResultLauncher.launch("image/*")
        }
      }
    }

    return builder.setView(mBuilder.root)
      .setTitle(R.string.dialog_set_icon_and_name_title)
      .setPositiveButton(R.string.dialog_delete_positive_button) { _: DialogInterface?, _: Int ->
          ShortcutsUtils.addPinnedShortcut(
            entity,
            mBuilder.ivIcon.drawable, mBuilder.etName.text.toString()
          )
      }
      .setNegativeButton(android.R.string.cancel, null)
      .create()
  }

  @SuppressLint("WrongConstant")
  fun takePersistableUriPermission(context: Context, uri: Uri, intent: Intent) {
    val takeFlags = (intent.flags
      and (Intent.FLAG_GRANT_READ_URI_PERMISSION
      or Intent.FLAG_GRANT_WRITE_URI_PERMISSION))
    // Check for the freshest data.
    context.contentResolver.takePersistableUriPermission(uri, takeFlags)
  }

  companion object {

    const val EXTRA_ENTITY = "EXTRA_ENTITY"

    fun newInstance(entity: AnywhereEntity): CreateShortcutDialogFragment {
      return CreateShortcutDialogFragment().apply {
        arguments = Bundle().apply {
          putParcelable(EXTRA_ENTITY, entity)
        }
      }
    }
  }
}
