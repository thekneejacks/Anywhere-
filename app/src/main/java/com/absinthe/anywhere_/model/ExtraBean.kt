package com.absinthe.anywhere_.model

import com.google.gson.annotations.SerializedName

data class ExtraBean(
  @SerializedName("data") val data: String = "",
  @SerializedName("action") val action: String = "",
  @SerializedName("category") val category: String = "",
  @SerializedName("extras") val extras: List<ExtraItem> = emptyList()
) {
  override fun toString(): String {
    val sb = StringBuilder()
    if (action.isNotBlank()) {
      sb.append("-a ").append(action)
    }
    if (data.isNotBlank()) {
      sb.append(" -d ").append(data)
    }

    for (extra in extras) {
      sb.append(" ").append(extra.toString())
    }
    return sb.toString()
  }

  data class ExtraItem(
    @SerializedName("type") var type: String,
    @SerializedName("key") var key: String,
    @SerializedName("value") var value: String
  ) {
    override fun toString(): String {
      return "$type \"$key\" \"$value\""
    }
  }
}
