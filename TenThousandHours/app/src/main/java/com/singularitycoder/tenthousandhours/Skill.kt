package com.singularitycoder.tenthousandhours

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TABLE_SKILL)
data class Skill(
    @PrimaryKey @ColumnInfo(name = "skillName") val skillName: String,
    @ColumnInfo(name = "hours") val hours: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "level") val level: SkillLevel
) {
    constructor() : this("", "", "", SkillLevel.BEGINNER)
}
