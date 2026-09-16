package com.absinthe.anywhere_.utils.manager

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.backup.RestoreApplyFragmentDialog
import com.absinthe.anywhere_.ui.shortcuts.CreateShortcutDialogFragment
import com.absinthe.anywhere_.utils.ToastUtil
import com.absinthe.anywhere_.view.app.AnywhereDialogBuilder

/**
 * Dialog Manager
 *
 *
 * To manage all Dialogs / DialogFragments / BottomSheetDialogs in App.
 */
object DialogManager {


  fun showDeleteAnywhereDialog(activity: Activity, ae: AnywhereEntity) {
    AnywhereDialogBuilder(activity)
      .setTitle(R.string.dialog_delete_title)
      .setMessage(
        HtmlCompat.fromHtml(
          String.format(
            activity.getString(R.string.dialog_delete_message),
            "<b>" + ae.appName + "</b>"
          ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
      )
      .setPositiveButton(R.string.dialog_delete_positive_button) { _, _ ->
        activity.onBackPressed()
        AnywhereApplication.sRepository.delete(ae)
      }
      .setNegativeButton(android.R.string.cancel, null)
      .show()
  }

 /* @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun showAddShortcutDialog(
    context: Context,
    builder: AnywhereDialogBuilder,
    ae: AnywhereEntity,
    action: () -> Unit
  ) {
    builder.setTitle(R.string.dialog_add_shortcut_title)
      .setMessage(
        HtmlCompat.fromHtml(
          String.format(
            context.getString(R.string.dialog_add_shortcut_message),
            "<b>" + ae.appName + "</b>"
          ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
      )
      .setPositiveButton(R.string.dialog_delete_positive_button) { _, _ -> action() }
      .setNegativeButton(android.R.string.cancel, null)
      .show()
  }*/

  /*@RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun showCannotAddShortcutDialog(context: Context, action: () -> Unit) {
    AnywhereDialogBuilder(context)
      .setTitle(R.string.dialog_cant_add_shortcut_title)
      .setMessage(R.string.dialog_cant_add_shortcut_message)
      .setPositiveButton(R.string.dialog_delete_positive_button, null)
      .setNeutralButton(R.string.dialog_add_shortcut_anymore_button) { _, _ -> action() }
      .show()
  }*/

  /*@RequiresApi(api = Build.VERSION_CODES.N_MR1)
  fun showRemoveShortcutDialog(context: Context, ae: AnywhereEntity, action: () -> Unit) {
    val builder = AnywhereDialogBuilder(context)
    builder.setTitle(R.string.dialog_remove_shortcut_title)
      .setMessage(
        HtmlCompat.fromHtml(
          String.format(
            context.getString(R.string.dialog_remove_shortcut_message),
            "<b>" + ae.appName + "</b>"
          ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
      )
      .setPositiveButton(R.string.dialog_delete_positive_button) { _, _ -> action() }
      .setNegativeButton(android.R.string.cancel, null)
    builder.show()
  }*/

  fun showDeleteSelectCardDialog(context: Context, action: () -> Unit) {
    AnywhereDialogBuilder(context)
      .setTitle(R.string.dialog_delete_selected_title)
      .setMessage(R.string.dialog_delete_selected_message)
      .setPositiveButton(R.string.dialog_delete_positive_button) { _, _ -> action() }
      .setNegativeButton(android.R.string.cancel, null)
      .show()
  }


  fun showPageListDialog(context: Context, action: (title: String) -> Unit) {
    val items = mutableListOf<String>()

    AnywhereApplication.sRepository.allPageEntities.value?.let { list ->
      list.iterator().forEach { items.add(it.title) }

      AnywhereDialogBuilder(context).apply {
        setTitle(R.string.menu_move_to_page)
        setItems(items.toTypedArray()) { _, which ->
          action(items[which])
        }
        show()
      }
    }
  }


  fun showMultiSelectCreatingShortcutDialog(context: Context, action: () -> Unit) {
    AnywhereDialogBuilder(context).apply {
      setTitle(R.string.dialog_add_home_shortcut_title)
      setMessage(R.string.dialog_add_select_shortcut_message)
      setPositiveButton(android.R.string.ok) { _, _ -> action() }
      setNegativeButton(android.R.string.cancel, null)
      show()
    }
  }


  fun showRestoreApplyDialog(activity: AppCompatActivity) {
    val dialog = RestoreApplyFragmentDialog()
    dialog.show(activity.supportFragmentManager, dialog.tag)
  }

  fun showCreatePinnedShortcutDialog(activity: AppCompatActivity, ae: AnywhereEntity) {
    val fragment = CreateShortcutDialogFragment.newInstance(ae)
    fragment.show(activity.supportFragmentManager, fragment.tag)
  }

}
