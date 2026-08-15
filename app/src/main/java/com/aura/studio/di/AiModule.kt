package com.aura.studio.di

import com.aura.studio.ai.AiChatService
import com.aura.studio.ai.LocalAiChatService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {
    @Binds
    @Singleton
    abstract fun bindAiChatService(impl: LocalAiChatService): AiChatService
}
