package com.aura.studio.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.studio.data.prefs.GenDefaults
import com.aura.studio.data.prefs.UserPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: UserPrefs) : ViewModel() {
    val defaults: StateFlow<GenDefaults> = prefs.genDefaults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GenDefaults())
    fun setSteps(v: Int) = viewModelScope.launch { prefs.setSteps(v) }
    fun setCfg(v: Float) = viewModelScope.launch { prefs.setCfg(v) }
    fun setSize(w: Int, h: Int) = viewModelScope.launch { prefs.setSize(w, h) }
    fun setDefaultNude(v: Boolean) = viewModelScope.launch { prefs.setDefaultNude(v) }
    fun setAutoEnhance(v: Boolean) = viewModelScope.launch { prefs.setAutoEnhance(v) }
}
