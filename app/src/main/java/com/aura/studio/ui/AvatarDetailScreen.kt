package com.aura.studio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aura.studio.avatar.AvatarSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarDetailScreen(
    avatar: AvatarSpec,
    onEdit: () -> Unit,
    onBack: () -> Unit,
    onGenerate: () -> Unit = {}
) {
    var showPrompt by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(avatar.name) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(avatar.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        if (avatar.isNude) "Fully Nude Model" else "Clothed Model",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Text("Appearance", style = MaterialTheme.typography.titleMedium)

            DetailRow("Age", "${avatar.age}")
            DetailRow("Ethnicity", avatar.ethnicity)
            DetailRow("Body Type", avatar.bodyType)
            DetailRow("Breast Size", "${avatar.breastSize} cup")
            DetailRow("Eye Color", avatar.eyeColor)
            DetailRow("Hair", "${avatar.hairColor}, ${avatar.hairStyle}")
            DetailRow("Skin Tone", avatar.skinTone)
            DetailRow("Outfit", if (avatar.isNude) "None (Nude)" else avatar.clothing)

            if (avatar.extra.isNotBlank()) {
                DetailRow("Extra Details", avatar.extra)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Generation Prompt", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { showPrompt = !showPrompt }) {
                    Text(if (showPrompt) "Hide" else "Show")
                }
            }

            if (showPrompt) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = avatar.toPrompt(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onGenerate,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("Generate Picture")
            }

            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Avatar")
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
