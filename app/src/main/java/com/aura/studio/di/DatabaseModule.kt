package com.aura.studio.di

import android.content.Context
import androidx.room.Room
import com.aura.studio.data.AppDatabase
import com.aura.studio.data.AvatarDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aura.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAvatarDao(db: AppDatabase): AvatarDao = db.avatarDao()
}
