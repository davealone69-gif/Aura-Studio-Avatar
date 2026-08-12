package com.aura.studio.ui.diagnostics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.studio.monitor.ComponentHealth
import com.aura.studio.monitor.HealthLevel
import com.aura.studio.monitor.RepairAction
import com.aura.studio.ui.theme.*
import com.aura.studio.ui.vm.DiagnosticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticsScreen(onBack: () -> Unit, vm: DiagnosticsViewModel = hiltViewModel()) {
    val report by vm.report.collectAsState()
    val busy by vm.busy.collectAsState()
    val log by vm.log.collectAsState()
    val lastRepair by vm.lastRepair.collectAsState()

    Box(Modifier.fillMaxSize().background(CyberBg)) {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("System Monitor", color = CyberText, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = CyberCyan) }
                },
                actions = {
                    IconButton(onClick = { vm.refresh() }, enabled = !busy) {
                        Icon(Icons.Default.Refresh, null, tint = CyberCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberSurface)
            )
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    val level = report?.overall ?: HealthLevel.UNKNOWN
                    val color = levelColor(level)
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(color.copy(0.15f))
                            .border(1.dp, color, RoundedCornerShape(12.dp)).padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.HealthAndSafety, null, tint = color)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Overall: ${level.name}", color = color, fontWeight = FontWeight.Bold)
                            Text(if (busy) "Working…" else "Auto-repair heals degraded systems", color = CyberTextDim, fontSize = 12.sp)
                        }
                        if (busy) CircularProgressIndicator(Modifier.size(20.dp), color = color, strokeWidth = 2.dp)
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { vm.autoRepair() }, enabled = !busy,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberMagenta, contentColor = CyberBg),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Build, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Auto-repair", fontSize = 13.sp)
                        }
                        OutlinedButton(
                            onClick = { vm.repair("full_self_repair") }, enabled = !busy,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            modifier = Modifier.weight(1f)
                        ) { Text("Full repair", fontSize = 13.sp) }
                    }
                }
                lastRepair?.let { r ->
                    item {
                        Text(
                            if (r.success) "Last repair OK: ${r.message}" else "Last repair failed: ${r.message}",
                            color = if (r.success) CyberSuccess else CyberError, fontSize = 12.sp
                        )
                    }
                }
                item { Text("COMPONENTS", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                items(report?.components.orEmpty(), key = { it.id.name }) { c -> ComponentRow(c) }
                item { Text("REPAIRS", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                items(report?.availableRepairs.orEmpty(), key = { it.id }) { a ->
                    RepairRow(a, !busy) { vm.repair(a.id) }
                }
                item { Text("EVENT LOG", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                items(log.asReversed()) { line -> Text(line, color = CyberTextDim, fontSize = 11.sp) }
            }
        }
    }
}

@Composable
private fun ComponentRow(c: ComponentHealth) {
    val color = levelColor(c.level)
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(CyberPanel)
            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp)).padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(color))
            Spacer(Modifier.width(8.dp))
            Text(c.title, color = CyberText, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Spacer(Modifier.weight(1f))
            Text(c.level.name, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        Text(c.detail, color = CyberTextDim, fontSize = 11.sp)
    }
}

@Composable
private fun RepairRow(action: RepairAction, enabled: Boolean, onRun: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(CyberPanel)
            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(action.label, color = CyberText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(action.description, color = CyberTextDim, fontSize = 11.sp)
        }
        TextButton(onClick = onRun, enabled = enabled) { Text("Run", color = CyberCyan, fontSize = 12.sp) }
    }
}

private fun levelColor(level: HealthLevel): Color = when (level) {
    HealthLevel.HEALTHY -> CyberSuccess
    HealthLevel.DEGRADED -> Color(0xFFFFB020)
    HealthLevel.UNHEALTHY -> CyberError
    HealthLevel.UNKNOWN -> CyberTextDim
}
