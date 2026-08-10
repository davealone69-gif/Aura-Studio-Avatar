package com.aura.studio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.data.AvatarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val repo: AvatarRepository
) : ViewModel() {

    val avatars: StateFlow<List<AvatarSpec>> = repo.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(spec: AvatarSpec) {
        viewModelScope.launch { repo.save(spec) }
    }

    fun delete(id: String) {
        viewModelScope.launch { repo.delete(id) }
    }
}
