package com.aura.studio.di

import com.aura.studio.animation.AnimationEngine
import com.aura.studio.animation.LogAnimationEngine
import com.aura.studio.voice.LocalVoiceService
import com.aura.studio.voice.VoiceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameModule {
    @Provides @Singleton fun voiceService(): VoiceService = LocalVoiceService()
    @Provides @Singleton fun animationEngine(): AnimationEngine = LogAnimationEngine()
}
