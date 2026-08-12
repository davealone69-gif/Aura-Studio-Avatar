package com.aura.studio.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.studio.avatar.AvatarSpec
import com.aura.studio.generation.*
import com.aura.studio.model.LocalModel
import com.aura.studio.model.ModelType
import com.aura.studio.ui.theme.*
import com.aura.studio.util.MediaStoreHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(avatar: AvatarSpec, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val llm = remember { DolphinLlmEngine() }
    val imageEngine = remember { DiffusionImageEngine() }
    val videoEngine = remember { DiffusionVideoEngine() }

    var mode by remember { mutableStateOf(0) }
    var isGenerating by remember { mutableStateOf(false) }
    var isEnhancing by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Ready — local engines") }
    var currentPrompt by remember { mutableStateOf(avatar.toPrompt()) }
    var resultBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(mode) {
        currentPrompt = if (mode == 0) avatar.toPrompt() else avatar.toVideoPrompt()
        resultBitmap = null
    }

    Box(Modifier.fillMaxSize().background(CyberBg)) {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Generate — ${avatar.name}", color = CyberText, fontWeight = FontWeight.SemiBold) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back", color = CyberCyan) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberSurface)
            )
            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(CyberPanel)
                        .border(1.dp, CyberBorder, RoundedCornerShape(12.dp)).padding(4.dp)
                ) {
                    listOf(0 to (Icons.Default.Image to "IMAGE"), 1 to (Icons.Default.Videocam to "VIDEO")).forEach { (m, pair) ->
                        val (icon, label) = pair
                        val selected = mode == m
                        Row(
                            Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                                .background(if (selected) CyberCyan.copy(0.2f) else CyberPanel)
                                .clickable { mode = m }.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, null, tint = if (selected) CyberCyan else CyberTextDim, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(label, color = if (selected) CyberCyan else CyberTextDim, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Text(if (mode == 0) "Local Image Generation" else "Local Text-to-Video", color = CyberCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                Card(colors = CardDefaults.cardColors(containerColor = CyberPanel), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text(currentPrompt, color = CyberText, fontSize = 13.sp)
                        Spacer(Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText("aura_prompt", currentPrompt))
                                Toast.makeText(context, "Prompt copied", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(Icons.Default.ContentCopy, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Copy")
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isEnhancing = true
                            status = "Enhancing with Dolphin…"
                            llm.load(LocalModel("Dolphin 3.0", ModelType.LLM, "/sdcard/Models/dolphin.gguf"))
                            val system = if (mode == 0) PromptTemplates.AVATAR_SYSTEM else PromptTemplates.VIDEO_MOTION
                            currentPrompt = llm.generate(system, currentPrompt)
                            status = "Prompt enhanced"
                            isEnhancing = false
                        }
                    },
                    enabled = !isEnhancing && !isGenerating,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberMagenta),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isEnhancing) {
                        CircularProgressIndicator(Modifier.size(18.dp), color = CyberMagenta, strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Enhancing…")
                    } else {
                        Icon(Icons.Default.AutoAwesome, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Enhance with Dolphin")
                    }
                }

                Button(
                    onClick = {
                        scope.launch {
                            isGenerating = true
                            resultBitmap = null
                            if (mode == 0) {
                                status = "Generating image…"
                                imageEngine.load(LocalModel("Local SD", ModelType.IMAGE, "/sdcard/Models/sd.safetensors"))
                                resultBitmap = imageEngine.generate(currentPrompt)
                                status = if (resultBitmap != null) "Image ready" else "Generation failed"
                            } else {
                                status = "Generating video…"
                                videoEngine.load(LocalModel("Local Video", ModelType.VIDEO, "/sdcard/Models/video.gguf"))
                                val path = videoEngine.generate(currentPrompt)
                                status = if (path != null) "Video ready: $path" else "Video native not linked"
                            }
                            isGenerating = false
                        }
                    },
                    enabled = !isGenerating && !isEnhancing,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = CyberBg),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(Modifier.size(20.dp), color = CyberBg, strokeWidth = 2.dp)
                        Spacer(Modifier.width(10.dp))
                        Text("Generating…")
                    } else Text(if (mode == 0) "GENERATE IMAGE" else "GENERATE VIDEO")
                }

                Text(status, color = CyberTextDim, fontSize = 12.sp)

                resultBitmap?.let { bmp ->
                    Text("RESULT", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = "Generated",
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(bmp.width.toFloat() / bmp.height.coerceAtLeast(1))
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, CyberBorder, RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Button(
                        onClick = {
                            val ok = MediaStoreHelper.saveBitmap(
                                context, bmp,
                                "aura_${avatar.name.replace(" ", "_")}_${System.currentTimeMillis()}.png"
                            )
                            Toast.makeText(context, if (ok) "Saved to Pictures/AuraStudio" else "Save failed", Toast.LENGTH_SHORT).show()
                            if (ok) status = "Saved to gallery"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberMagenta, contentColor = CyberBg),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Save, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("SAVE TO GALLERY")
                    }
                }
            }
        }
    }
}
