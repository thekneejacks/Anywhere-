package com.absinthe.anywhere_.model

import com.absinthe.anywhere_.model.database.AnywhereEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@kotlinx.serialization.InternalSerializationApi
data class BackupBean(
  @SerialName("anywhereList") val anywhereList: List<AnywhereEntity>,
)
