package com.github.donghune.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.donghune.domain.entity.Group

@Entity
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String
)

fun Group.toGroupEntity(): GroupEntity {
    return GroupEntity(
        id,
        name
    )
}

fun GroupEntity.toGroup(): Group {
    return Group(
        id,
        name
    )
}