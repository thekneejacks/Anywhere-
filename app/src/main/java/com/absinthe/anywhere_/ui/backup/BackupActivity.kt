package com.absinthe.anywhere_.ui.backup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.absinthe.anywhere_.BaseActivity
import com.absinthe.anywhere_.R
import com.absinthe.anywhere_.constants.Const
import com.absinthe.anywhere_.databinding.ActivityBackupBinding
import com.absinthe.anywhere_.utils.StorageUtils
import com.absinthe.anywhere_.utils.ToastUtil
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@kotlinx.serialization.InternalSerializationApi
class BackupActivity : BaseActivity<ActivityBackupBinding>() {

  override fun setViewBinding() = ActivityBackupBinding.inflate(layoutInflater)

  class BackupFragment : PreferenceFragmentCompat() {

    private lateinit var backupResultLauncher: ActivityResultLauncher<String>
    private lateinit var restoreResultLauncher: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
      super.onAttach(context)
      backupResultLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument()) {
          it?.let {
            try {
              Utils.getApp().contentResolver.openOutputStream(it)?.let { os ->
                StorageUtils.exportAnywhereEntityJsonString()?.let { content ->
                    os.write(content.toByteArray())
                    os.close()
                    ToastUtil.makeText(requireContext(), getString(R.string.toast_backup_success))
                }
              }
            } catch (e: IOException) {
              e.printStackTrace()
              ToastUtil.makeText(requireContext(), getString(R.string.toast_runtime_error))
            }
          }
        }
      restoreResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { data ->
          try {
            Utils.getApp().contentResolver.openInputStream(data)?.let { inputStream ->
              val reader = BufferedReader(InputStreamReader(inputStream))
              val stringBuilder = StringBuilder()
              var line: String?

              while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
              }

              lifecycleScope.launch(Dispatchers.IO) {
                StorageUtils.restoreFromJson(requireContext(), stringBuilder.toString())
              }

              inputStream.close()
              reader.close()
            }
          } catch (e: IOException) {
            e.printStackTrace()
          }
        }
      }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      setPreferencesFromResource(R.xml.settings_backup, rootKey)

      findPreference<Preference>(Const.PREF_BACKUP)?.apply {
        setOnPreferenceClickListener {
          if (StorageUtils.isExternalStorageWritable) {
            runCatching {
              backupResultLauncher.launch("Anywhere-Backups-$currentFormatDate.awbackups")
            }.onFailure {
              //Timber.e(it)
              ToastUtil.makeText(context, "Document API not working")
            }
          } else {
            ToastUtil.makeText(R.string.toast_check_device_storage_state)
          }
          true
        }
      }
      findPreference<Preference>(Const.PREF_RESTORE)?.apply {
        setOnPreferenceClickListener {
          runCatching {
            restoreResultLauncher.launch("*/*")
          }.onFailure {
            //Timber.e(it)
            ToastUtil.makeText(context, "Document API not working")
          }
          true
        }
      }
    }

    override fun onCreateRecyclerView(
      inflater: LayoutInflater,
      parent: ViewGroup,
      savedInstanceState: Bundle?
    ): RecyclerView {
      val recyclerView =
        super.onCreateRecyclerView(inflater, parent, savedInstanceState)
      recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
      recyclerView.isVerticalScrollBarEnabled = false

      /*val lp = recyclerView.layoutParams
      if (lp is FrameLayout.LayoutParams) {
        lp.rightMargin =
          recyclerView.context.resources.getDimension(rikka.material.R.dimen.rd_activity_horizontal_margin)
            .toInt()
        lp.leftMargin = lp.rightMargin
      }*/

      return recyclerView
    }

    val currentFormatDate: String
      get() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        return simpleDateFormat.format(date)
      }

  }
}
