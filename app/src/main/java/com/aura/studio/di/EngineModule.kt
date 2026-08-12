package com.aura.studio.di

import com.aura.studio.generation.DiffusionImageEngine
import com.aura.studio.generation.DiffusionVideoEngine
import com.aura.studio.generation.DolphinLlmEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {
    @Provides @Singleton fun provideDolphin() = DolphinLlmEngine()
    @Provides @Singleton fun provideImageEngine() = DiffusionImageEngine()
    @Provides @Singleton fun provideVideoEngine() = DiffusionVideoEngine()
}
