package com.aura.studio.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import com.aura.studio.model.RecommendedModels
import com.aura.studio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelManagerScreen(
    onBack: () -> Unit
) {
    var models by remember {
        mutableStateOf(
            listOf(
                LocalModel(
                    name = "Dolphin 3.0 8B Q4_K_M",
                    type = ModelType.LLM,
                    path = "/sdcard/Models/dolphin-3.0-llama3.1-8b.Q4_K_M.gguf",
                    quant = "Q4_K_M",
                    isDefault = true,
                    notes = "Primary uncensored model. Place GGUF and update path."
                )
            )
        )
    }
    var selectedId by remember { mutableStateOf(models.firstOrNull()?.id) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text("Local Models", color = CyberText, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = CyberCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberSurface)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    "Token-free. Point the app at GGUF files on this device.\nPrimary target: Dolphin 3.0 (uncensored).",
                    color = CyberTextDim,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(12.dp))

                Text("RECOMMENDED", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                listOf(
                    RecommendedModels.DOLPHIN,
                    RecommendedModels.HERMES,
                    RecommendedModels.QWEN_UNCENSORED
                ).forEach { rec ->
                    Text("• $rec", color = CyberTextDim, fontSize = 12.sp)
                }

                Spacer(Modifier.height(16.dp))

                Text("REGISTERED MODELS", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(models, key = { it.id }) { model ->
                        ModelCard(
                            model = model,
                            selected = model.id == selectedId,
                            onSelect = { selectedId = model.id }
                        )
                    }
                }

                Button(
                    onClick = {
                        models = models + LocalModel(
                            name = "New GGUF model",
                            type = ModelType.LLM,
                            path = "/sdcard/Models/your-model.Q4_K_M.gguf",
                            notes = "Update path after placing the file"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan,
                        contentColor = CyberBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Register Dolphin / GGUF Model")
                }
            }
        }
    }
}

@Composable
private fun ModelCard(
    model: LocalModel,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) CyberCyan.copy(alpha = 0.12f) else CyberPanel)
            .border(
                1.dp,
                if (selected) CyberCyan else CyberBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Memory,
            contentDescription = null,
            tint = if (selected) CyberCyan else CyberTextDim,
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                model.name,
                color = CyberText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                "${model.type} • ${model.quant}",
                color = CyberTextDim,
                fontSize = 11.sp
            )
            if (model.notes.isNotBlank()) {
                Text(model.notes, color = CyberTextDim, fontSize = 10.sp, maxLines = 2)
            }
        }
        if (selected) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = "Selected",
                tint = CyberCyan
            )
        }
    }
}
