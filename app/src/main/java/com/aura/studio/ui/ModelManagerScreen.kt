package com.aura.studio.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import com.aura.studio.model.RecommendedModels
import com.aura.studio.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelManagerScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var models by remember {
        mutableStateOf(listOf(LocalModel(
            name = "Dolphin 3.0 8B Q4_K_M",
            type = ModelType.LLM,
            path = "/sdcard/Models/dolphin-3.0-llama3.1-8b.Q4_K_M.gguf",
            quant = "Q4_K_M", isDefault = true,
            notes = "Primary uncensored LLM. Pick file to update path."
        )))
    }
    var selectedId by remember { mutableStateOf(models.firstOrNull()?.id) }
    var pendingType by remember { mutableStateOf(ModelType.LLM) }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        try {
            context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (_: Exception) {}
        val name = uri.lastPathSegment?.substringAfterLast('/') ?: "model"
        val quant = when {
            name.contains("Q4", true) -> "Q4_K_M"
            name.contains("Q5", true) -> "Q5_K_M"
            name.contains("Q8", true) -> "Q8_0"
            else -> "unknown"
        }
        val model = LocalModel(name = name, type = pendingType, path = uri.toString(), quant = quant, notes = "Picked from storage")
        models = models + model
        selectedId = model.id
    }

    Box(Modifier = Modifier.fillMaxSize().background(CyberBg)) {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Local Models", color = CyberText, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = CyberCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberSurface)
            )
            Column(Modifier.padding(16.dp).fillMaxSize()) {
                Text("Token-free. Pick GGUF / weights from device.\nPrimary: Dolphin 3.0 (uncensored).", color = CyberTextDim, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Text("RECOMMENDED", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                listOf(RecommendedModels.DOLPHIN, RecommendedModels.HERMES, RecommendedModels.QWEN_UNCENSORED).forEach {
                    Text("• $it", color = CyberTextDim, fontSize = 12.sp)
                }
                Spacer(Modifier.height(16.dp))
                Text("REGISTERED MODELS", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    items(models, key = { it.id }) { model ->
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                .background(if (model.id == selectedId) CyberCyan.copy(0.12f) else CyberPanel)
                                .border(1.dp, if (model.id == selectedId) CyberCyan else CyberBorder, RoundedCornerShape(12.dp))
                                .clickable { selectedId = model.id }.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Memory, null, tint = if (model.id == selectedId) CyberCyan else CyberTextDim, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(model.name, color = CyberText, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("${model.type} • ${model.quant}", color = CyberTextDim, fontSize = 11.sp)
                                Text(model.path.take(48) + if (model.path.length > 48) "…" else "", color = CyberTextDim, fontSize = 10.sp, maxLines = 1)
                            }
                            if (model.id == selectedId) Icon(Icons.Default.CheckCircle, "Selected", tint = CyberCyan)
                        }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(ModelType.LLM to "LLM", ModelType.IMAGE to "IMAGE", ModelType.VIDEO to "VIDEO").forEach { (type, label) ->
                        Button(
                            onClick = { pendingType = type; filePicker.launch(arrayOf("*/*")) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = CyberBg),
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.FolderOpen, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(label, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
