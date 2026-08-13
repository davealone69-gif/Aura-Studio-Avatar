package com.aura.studio.di

import com.aura.studio.ai.AiChatService
import com.aura.studio.ai.LocalAiChatService
import com.aura.studio.domain.voice.LocalStubVoiceService
import com.aura.studio.domain.voice.VoiceService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GameModule {
    @Binds @Singleton abstract fun bindAiChat(impl: LocalAiChatService): AiChatService
    @Binds @Singleton abstract fun bindVoice(impl: LocalStubVoiceService): VoiceService
}
