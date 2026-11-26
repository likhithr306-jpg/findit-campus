package com.example.lostandfound.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lostandfound.data.local.dao.ItemDao
import com.example.lostandfound.data.local.dao.MatchDao
import com.example.lostandfound.data.local.entity.LostFoundItem
import com.example.lostandfound.data.local.entity.MatchCandidate

@Database(
    entities = [LostFoundItem::class, MatchCandidate::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun matchDao(): MatchDao

    companion object {
        private const val DATABASE_NAME = "findit_campus_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
