package com.github.donghune.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.donghune.domain.entity.Address

@Entity
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val groupId: Int
)

fun Address.toAddressEntity(): AddressEntity {
    return AddressEntity(
        id,
        name,
        latitude,
        longitude,
        groupId,
    )
}

fun AddressEntity.toAddress(): Address {
    return Address(
        id,
        name,
        latitude,
        longitude,
        groupId,
    )
}