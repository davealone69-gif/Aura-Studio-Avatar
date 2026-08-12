package com.aura.studio.ui.vm

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.studio.ai.AIService
import com.aura.studio.ai.DolphinService
import com.aura.studio.ai.GenerateRequest
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.prefs.UserPrefs
import com.aura.studio.domain.room.RoomDefinition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val dolphin: DolphinService,
    private val ai: AIService,
    private val prefs: UserPrefs
) : ViewModel() {
    private val _prompt = MutableStateFlow("")
    val prompt: StateFlow<String> = _prompt.asStateFlow()
    private val _status = MutableStateFlow("Ready")
    val status: StateFlow<String> = _status.asStateFlow()
    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap.asStateFlow()
    private val _rooms = MutableStateFlow<List<RoomDefinition>>(emptyList())
    val rooms: StateFlow<List<RoomDefinition>> = _rooms.asStateFlow()
    private val _roomId = MutableStateFlow<String?>(null)
    val roomId: StateFlow<String?> = _roomId.asStateFlow()
    private val _interactionId = MutableStateFlow<String?>(null)
    val interactionId: StateFlow<String?> = _interactionId.asStateFlow()

    fun init(avatar: AvatarSpec) {
        _prompt.value = avatar.toPrompt()
        _rooms.value = ai.rooms()
        viewModelScope.launch { _status.value = dolphin.status().message }
    }

    fun setRoom(id: String?) { _roomId.value = id; _interactionId.value = null }
    fun setInteraction(id: String?) { _interactionId.value = id }
    fun setPrompt(p: String) { _prompt.value = p }

    fun enhance(avatar: AvatarSpec, video: Boolean) {
        viewModelScope.launch {
            _busy.value = true
            _status.value = "Dolphin enhancing…"
            try {
                dolphin.ensureLoaded()
                _prompt.value = dolphin.enhance(_prompt.value.ifBlank { avatar.toPrompt() }, video)
                _status.value = "Enhanced"
            } catch (e: Exception) {
                _status.value = "Enhance error: ${e.message}"
            } finally {
                _busy.value = false
            }
        }
    }

    fun generate(avatar: AvatarSpec, video: Boolean) {
        viewModelScope.launch {
            _busy.value = true
            _bitmap.value = null
            _status.value = if (video) "Generating video…" else "Generating image…"
            try {
                val d = prefs.genDefaults.first()
                val result = ai.generate(
                    GenerateRequest(
                        avatar = avatar,
                        roomId = _roomId.value,
                        interactionId = _interactionId.value,
                        mode = if (video) GenerateRequest.Mode.VIDEO else GenerateRequest.Mode.IMAGE,
                        steps = d.steps, cfg = d.cfg, width = d.width, height = d.height
                    )
                )
                _prompt.value = result.prompt
                _bitmap.value = result.bitmap
                _status.value = when {
                    result.bitmap != null -> "Image ready"
                    result.videoPath != null -> "Video: ${result.videoPath}"
                    else -> "Done (check native backends)"
                }
            } catch (e: Exception) {
                _status.value = "Generate error: ${e.message}"
            } finally {
                _busy.value = false
            }
        }
    }
}
