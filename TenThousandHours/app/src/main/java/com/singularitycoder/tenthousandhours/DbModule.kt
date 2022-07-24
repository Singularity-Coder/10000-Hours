package com.singularitycoder.tenthousandhours

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun injectSkillRoomDatabase(@ApplicationContext context: Context): SkillDatabase {
        return Room.databaseBuilder(context, SkillDatabase::class.java, DB_SKILL).build()
    }

    @Singleton
    @Provides
    fun injectSkillDao(db: SkillDatabase): SkillDao = db.skillDao()
}
