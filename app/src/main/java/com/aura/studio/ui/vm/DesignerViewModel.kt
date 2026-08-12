package com.aura.studio.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.studio.ai.DolphinService
import com.aura.studio.avatar.AvatarSpec
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DesignerViewModel @Inject constructor(
    private val dolphin: DolphinService
) : ViewModel() {
    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()
    private val _status = MutableStateFlow("Dolphin ready")
    val status: StateFlow<String> = _status.asStateFlow()

    fun opinion(onResult: (AvatarSpec) -> Unit) {
        viewModelScope.launch {
            _busy.value = true
            _status.value = "Dolphin Opinion…"
            try {
                val st = dolphin.status()
                _status.value = st.message
                onResult(dolphin.opinion())
                _status.value = if (st.native) "Opinion from native Dolphin" else "Opinion (simulator)"
            } catch (e: Exception) {
                _status.value = "Opinion failed: ${e.message}"
            } finally {
                _busy.value = false
            }
        }
    }

    fun enhance(current: AvatarSpec, onResult: (String) -> Unit) {
        viewModelScope.launch {
            _busy.value = true
            _status.value = "Dolphin Enhance…"
            try {
                onResult(dolphin.enhance(current.toPrompt()))
                _status.value = "Prompt enhanced"
            } catch (e: Exception) {
                _status.value = "Enhance failed: ${e.message}"
            } finally {
                _busy.value = false
            }
        }
    }
}
