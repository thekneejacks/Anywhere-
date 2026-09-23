package com.absinthe.anywhere_.utils.manager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.absinthe.anywhere_.AnywhereApplication
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.model.database.AnywhereEntity
import com.absinthe.anywhere_.ui.shortcuts.CreateShortcutDialogFragment
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


  fun showCreatePinnedShortcutDialog(activity: AppCompatActivity, ae: AnywhereEntity) {
    val fragment = CreateShortcutDialogFragment.newInstance(ae)
    fragment.show(activity.supportFragmentManager, fragment.tag)
  }

}
