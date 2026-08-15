package com.aura.studio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aura.studio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onOpenDiagnostics: () -> Unit = {}
) {
    Scaffold(
        containerColor = CyberBg,
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = CyberText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = CyberCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberSurface)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Local-first. No cloud tokens.", color = CyberTextDim)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onOpenDiagnostics,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = CyberBg)
            ) {
                Text("Open Diagnostics")
            }
        }
    }
}
