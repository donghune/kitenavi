package com.github.donghune.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GroupEntity::class, AddressEntity::class], version = 1)
abstract class AddressDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun addressDao(): AddressDao

    companion object {
        private var instance: AddressDatabase? = null

        fun getInstance(context: Context): AddressDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        AddressDatabase::class.java, "address.db"
                    ).build()
                }
            }
            return instance!!
        }
    }
}

