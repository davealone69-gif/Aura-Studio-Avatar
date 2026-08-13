package com.aura.studio.voice

interface VoiceService {
    suspend fun transcribe(audio: ByteArray, sampleRate: Int = 16000): String
    suspend fun synthesize(text: String, voiceId: String, emotionHint: String = ""): ByteArray
    fun availableVoices(): List<VoiceProfile>
}

data class VoiceProfile(val id: String, val displayName: String, val language: String = "en", val style: String = "neutral")

class LocalVoiceService : VoiceService {
    override suspend fun transcribe(audio: ByteArray, sampleRate: Int) =
        if (audio.isEmpty()) "" else "[voice transcript placeholder — link Whisper/native STT]"
    override suspend fun synthesize(text: String, voiceId: String, emotionHint: String) = ByteArray(0)
    override fun availableVoices() = listOf(
        VoiceProfile("default", "Default"),
        VoiceProfile("warm", "Warm", style = "warm"),
        VoiceProfile("soft", "Soft", style = "soft")
    )
}
