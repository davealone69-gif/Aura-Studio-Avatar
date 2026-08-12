package com.aura.studio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.ui.components.EmptyState
import com.aura.studio.ui.components.StatusChip
import com.aura.studio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarListScreen(viewModel: AvatarViewModel, onCreateNew: () -> Unit, onOpen: (AvatarSpec) -> Unit, onOpenModels: () -> Unit = {}) {
    val avatars by viewModel.avatars.collectAsState()
    var query by remember { mutableStateOf("") }
    val filtered = remember(avatars, query) {
        if (query.isBlank()) avatars else avatars.filter {
            it.name.contains(query, true) || it.ethnicity.contains(query, true) || it.bodyType.contains(query, true)
        }
    }
    CyberBackground {
        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Aura Studio", color = CyberText, fontWeight = FontWeight.Bold)
                            Text("${avatars.size} avatar${if (avatars.size == 1) "" else "s"}", color = CyberTextDim, fontSize = 12.sp)
                        }
                    },
                    actions = { IconButton(onClick = onOpenModels) { Icon(Icons.Default.Memory, "Models", tint = CyberCyan) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberSurface.copy(alpha = 0.92f))
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onCreateNew, containerColor = CyberCyan, contentColor = CyberBg,
                    icon = { Icon(Icons.Default.Add, null) }, text = { Text("New Avatar", fontWeight = FontWeight.Bold) }
                )
            }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                if (avatars.isNotEmpty()) {
                    OutlinedTextField(
                        value = query, onValueChange = { query = it },
                        placeholder = { Text("Search name, ethnicity, body…", color = CyberTextDim) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = CyberCyan) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan, unfocusedBorderColor = CyberBorder,
                            focusedTextColor = CyberText, unfocusedTextColor = CyberText, cursorColor = CyberCyan,
                            focusedContainerColor = CyberPanel, unfocusedContainerColor = CyberPanel
                        ),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
                when {
                    avatars.isEmpty() -> EmptyState("No avatars yet", "Design your first model — local only, no cloud tokens.", Icons.Default.Person, "Create Avatar", onCreateNew)
                    filtered.isEmpty() -> EmptyState("No matches", "Try a different search.", Icons.Default.Search)
                    else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filtered, key = { it.id }) { avatar ->
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(CyberPanel)
                                    .border(1.dp, CyberBorder, RoundedCornerShape(16.dp)).clickable { onOpen(avatar) }.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(Modifier.size(58.dp).clip(CircleShape).background(CyberCyan.copy(0.12f)).border(1.dp, CyberCyan.copy(0.4f), CircleShape), contentAlignment = Alignment.Center) {
                                    Text(avatar.name.take(1).uppercase(), color = CyberCyan, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(avatar.name, color = CyberText, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text("${avatar.age} · ${avatar.ethnicity} · ${avatar.bodyType}", color = CyberTextDim, fontSize = 12.sp)
                                    Spacer(Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        StatusChip(if (avatar.isNude) "NUDE" else avatar.outfitStyle.ifBlank { "CLOTHED" }, danger = avatar.isNude, active = !avatar.isNude)
                                        StatusChip("${avatar.breastSize} cup", active = true)
                                    }
                                }
                                IconButton(onClick = { viewModel.delete(avatar.id) }) {
                                    Icon(Icons.Default.Delete, "Delete", tint = CyberError)
                                }
                            }
                        }
                        item { Spacer(Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }
}
