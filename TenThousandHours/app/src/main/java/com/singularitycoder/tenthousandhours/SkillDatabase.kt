package com.singularitycoder.tenthousandhours

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Skill::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class SkillDatabase : RoomDatabase() {
    abstract fun skillDao(): SkillDao
}

