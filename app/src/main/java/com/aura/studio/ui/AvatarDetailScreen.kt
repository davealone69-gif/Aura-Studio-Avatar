package com.aura.studio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Details", style = MaterialTheme.typography.titleMedium)

            DetailRow("Age", "${avatar.age}")
            DetailRow("Ethnicity", avatar.ethnicity)
            DetailRow("Body", avatar.bodyType)
            DetailRow("Breasts", "${avatar.breastSize} cup")
            DetailRow("Eyes", avatar.eyeColor)
            DetailRow("Hair", "${avatar.hairColor} ${avatar.hairStyle}")
            DetailRow("Skin", avatar.skinTone)
            DetailRow("Clothing", if (avatar.isNude) "Nude" else avatar.clothing)

            if (avatar.extra.isNotBlank()) {
                DetailRow("Extra", avatar.extra)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Prompt", style = MaterialTheme.typography.titleMedium)
            Text(
                text = avatar.toPrompt(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGenerate,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Picture")
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
