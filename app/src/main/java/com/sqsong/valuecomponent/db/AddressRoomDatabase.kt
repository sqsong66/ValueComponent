package com.sqsong.valuecomponent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sqsong.valuecomponent.bean.City
import com.sqsong.valuecomponent.bean.County
import com.sqsong.valuecomponent.bean.Province
import com.sqsong.valuecomponent.bean.Town

@Database(entities = [Province::class, City::class, County::class, Town::class], exportSchema = false, version = 1)
abstract class AddressRoomDatabase : RoomDatabase() {

    abstract fun addressDao(): AddressDao

    companion object {
        @Volatile
        private var instance: AddressRoomDatabase? = null

        fun getInstance(context: Context): AddressRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AddressRoomDatabase {
            return Room.databaseBuilder(
                context,
                AddressRoomDatabase::class.java,
                "china_cities.db"
            ).build()
        }
    }

}