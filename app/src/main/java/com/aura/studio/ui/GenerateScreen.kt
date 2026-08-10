package com.aura.studio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aura.studio.avatar.AvatarSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    avatar: AvatarSpec,
    onBack: () -> Unit
) {
    var isGenerating by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate - ${avatar.name}") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Avatar", style = MaterialTheme.typography.titleMedium)
            Text("${avatar.name} • ${avatar.age} • ${avatar.ethnicity}")
            Text("${avatar.bodyType} • ${avatar.breastSize} cup • ${if (avatar.isNude) "Nude" else avatar.clothing}")

            Divider()

            Text("Prompt", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = avatar.toPrompt(),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    isGenerating = true
                    resultMessage = null
                    // Placeholder - real generation comes later
                    isGenerating = false
                    resultMessage = "Prompt ready. Real image generation will be wired here.\n\n${avatar.toPrompt()}"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isGenerating
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generating...")
                } else {
                    Text("Generate Picture")
                }
            }

            resultMessage?.let { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
