package com.singularitycoder.tenthousandhours

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_SKILL)
data class Skill(
    @PrimaryKey @ColumnInfo(name = "skillName") val name: String,
    @ColumnInfo(name = "hours") var hours: Short,
    @ColumnInfo(name = "date") val dateAdded: Long,
    @ColumnInfo(name = "level") val level: String
) {
    constructor() : this("", 0, 0L, "")
}
