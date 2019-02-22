package com.ajithkp.application.appdatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ajithkp.application.appdatabase.MarketDao

/**
 * Created by kpajith on 23-08-2018 on 15:35.
 */

@Database(entities = arrayOf(Market::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao

    companion object {

        private val DATABASE_NAME = "market_room_db"


        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                        .allowMainThreadQueries().build()
            }
            return INSTANCE !!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}


/*@Database(entities = arrayOf(Market::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "market_databse.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}*/
