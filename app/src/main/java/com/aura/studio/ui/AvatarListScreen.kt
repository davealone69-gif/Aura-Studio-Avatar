package com.aura.studio.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.studio.avatar.AvatarSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarListScreen(
    viewModel: AvatarViewModel = hiltViewModel(),
    onCreateNew: () -> Unit,
    onEdit: (AvatarSpec) -> Unit
) {
    val avatars by viewModel.avatars.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Avatars") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNew) {
                Icon(Icons.Default.Add, contentDescription = "New Avatar")
            }
        }
    ) { padding ->
        if (avatars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No avatars yet.\nTap + to create one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(avatars, key = { it.id }) { avatar ->
                    AvatarCard(
                        avatar = avatar,
                        onClick = { onEdit(avatar) },
                        onDelete = { viewModel.delete(avatar.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AvatarCard(
    avatar: AvatarSpec,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(avatar.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${avatar.age} • ${avatar.ethnicity} • ${avatar.bodyType} • ${avatar.breastSize} cup",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    if (avatar.isNude) "Nude" else avatar.clothing,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
