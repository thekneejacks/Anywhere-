package com.absinthe.anywhere_.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.absinthe.anywhere_.model.database.AnywhereEntity

@Dao
interface AnywhereDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(ae: AnywhereEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(list: List<AnywhereEntity>)

  @Update
  suspend fun update(ae: AnywhereEntity)

  @Update
  suspend fun update(list: List<AnywhereEntity>)

  @Delete
  suspend fun delete(ae: AnywhereEntity)

  @Delete
  suspend fun delete(list: List<AnywhereEntity>)

  @get:Query("SELECT * from anywhere_table ORDER BY time_stamp DESC")
  val allAnywhereEntitiesOrderByTimeDesc: LiveData<List<AnywhereEntity>>

  @Query("SELECT param_1 from anywhere_table WHERE _id LIKE :id")
  fun getParamById(id: String): String?
}
