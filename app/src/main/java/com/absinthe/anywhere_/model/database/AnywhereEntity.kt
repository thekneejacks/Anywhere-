package com.absinthe.anywhere_.model.database

import android.os.Parcelable
import android.provider.BaseColumns
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "anywhere_table")
data class AnywhereEntity(
  @PrimaryKey
  @ColumnInfo(name = BaseColumns._ID)
  var id: String = System.currentTimeMillis().toString(),

  @SerializedName(APP_NAME)
  @ColumnInfo(name = APP_NAME)
  var appName: String = "",

  @SerializedName(PARAM_1)
  @ColumnInfo(name = PARAM_1)
  var param1: String = "",

  @SerializedName(TIME_STAMP)
  @ColumnInfo(name = TIME_STAMP)
  var timeStamp: String = id,

) : Parcelable {

  companion object {

    const val APP_NAME = "app_name"
    const val PARAM_1 = "param_1"
    const val TIME_STAMP = "time_stamp"

  }
}

