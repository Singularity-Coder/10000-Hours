package com.singularitycoder.tenthousandhours

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SkillDao {

    // Single Item CRUD ops ------------------------------------------------------------------------------------------------------------------------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(skill: Skill)

    @Transaction
    @Query("SELECT * FROM $TABLE_SKILL WHERE skillName LIKE :skillName LIMIT 1")
    suspend fun getSkillBySkillName(skillName: String): Skill?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(skill: Skill)

    @Delete
    suspend fun delete(skill: Skill)

    // ---------------------------------------------------------------------------------------------------------------------------------------------

    // All of the parameters of the Insert method must either be classes annotated with Entity or collections/array of it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(skillList: List<Skill>)

    @Query("SELECT * FROM $TABLE_SKILL")
    fun getAllSkillListLiveData(): LiveData<List<Skill>>

    @Query("SELECT * FROM $TABLE_SKILL")
    fun getLatestSkillLiveData(): LiveData<Skill>

    @Query("SELECT * FROM $TABLE_SKILL")
    suspend fun getAll(): List<Skill>

    @Query("DELETE FROM $TABLE_SKILL")
    suspend fun deleteAll()
}
