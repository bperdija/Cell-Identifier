package com.example.cell_identifier.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CellEntity::class], version = 1)
abstract class CellDatabase {
    abstract class CellDatabase : RoomDatabase() {
        abstract val cellDBDao: CellDBDao

        companion object {
            @Volatile
            private var INSTANCE: CellDatabase? = null

            fun getInstance(context: Context): CellDatabase {
                synchronized(this) {
                    var instance = INSTANCE
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            CellDatabase::class.java,
                            "cell_identifier_db"
                        ).build()
                        INSTANCE = instance
                    }
                    return instance
                }
            }
        }
    }
}