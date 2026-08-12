package com.aura.studio.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.studio.data.GenerationDao
import com.aura.studio.data.GenerationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val dao: GenerationDao) : ViewModel() {
    private val _favOnly = MutableStateFlow(false)
    val favoritesOnly: StateFlow<Boolean> = _favOnly
    val items: StateFlow<List<GenerationEntity>> = combine(dao.getAll(), dao.getFavorites(), _favOnly) { all, favs, only ->
        if (only) favs else all
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    fun toggleFavoritesOnly() { _favOnly.value = !_favOnly.value }
    fun toggleFavorite(item: GenerationEntity) = viewModelScope.launch { dao.setFavorite(item.id, !item.isFavorite) }
    fun delete(id: String) = viewModelScope.launch { dao.delete(id) }
}
