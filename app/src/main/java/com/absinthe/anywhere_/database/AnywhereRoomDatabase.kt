package com.absinthe.anywhere_.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.absinthe.anywhere_.model.database.AnywhereEntity

@kotlinx.serialization.InternalSerializationApi
@Database(entities = [AnywhereEntity::class], version = 14, exportSchema = false)
abstract class AnywhereRoomDatabase : RoomDatabase() {

  abstract fun anywhereDao(): AnywhereDao

  companion object {

    @Volatile
    private var INSTANCE: AnywhereRoomDatabase? = null

    fun getDatabase(context: Context): AnywhereRoomDatabase {
      val tempInstance = INSTANCE
      if (tempInstance != null) {
        return tempInstance
      }
      synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AnywhereRoomDatabase::class.java,
          "anywhere_database"
        )
          .allowMainThreadQueries() //should be very careful
          .build()
        INSTANCE = instance
        return instance
      }
    }

    /*private val MIGRATION_11_12: Migration = object : Migration(11, 12) {
      override fun migrate(database: SupportSQLiteDatabase) {
        // Create the new table
        database.execSQL(
          "CREATE TABLE anywhere_new (_id TEXT NOT NULL, app_name TEXT NOT NULL, param_1 TEXT NOT NULL, time_stamp TEXT NOT NULL, PRIMARY KEY(_id))"
        )
        // Copy the data
        database.execSQL(
          "INSERT INTO anywhere_new (_id, app_name, param_1, time_stamp) SELECT _id, app_name, param_1, time_stamp FROM anywhere_table"
        )
        // Remove the old table
        database.execSQL("DROP TABLE anywhere_table")
        // Change the table name to the correct one
        database.execSQL("ALTER TABLE anywhere_new RENAME TO anywhere_table")
      }
    }*/
  }
}
