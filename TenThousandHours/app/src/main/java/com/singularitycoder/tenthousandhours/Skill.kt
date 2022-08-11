package com.singularitycoder.tenthousandhours

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_SKILL)
data class Skill(
    @ColumnInfo(name = "skillName") var name: String,
    @ColumnInfo(name = "hours") var hours: Short,
    @PrimaryKey @ColumnInfo(name = "dateAdded") val dateAdded: Long,
    @ColumnInfo(name = "dateLastAttempted") var dateLastAttempted: Long,
    @ColumnInfo(name = "level") var level: String,
) {
    constructor() : this("", 0, 0L, 0L, "")
}
