package com.github.donghune.kitenavi.model.local

import android.content.Context
import androidx.room.*

@Database(entities = [Group::class, Address::class], version = 1)
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

@Dao
interface GroupDao {
    @Query("SELECT * FROM `Group`")
    suspend fun getGroups(): List<Group>

    @Insert
    suspend fun insert(group: Group)

    @Delete
    suspend fun delete(group: Group)
}

@Dao
interface AddressDao {
    @Query("SELECT * FROM Address WHERE groupId == (:groupId)")
    suspend fun getAddressListByGroup(groupId: Int): List<Address>

    @Insert
    suspend fun insert(address: Address)

    @Delete
    suspend fun delete(address: Address)
}

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String
)

@Entity
data class Address(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val groupId: Int
)
