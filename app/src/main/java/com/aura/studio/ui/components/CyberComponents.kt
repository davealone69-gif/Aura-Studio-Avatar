package com.aura.studio.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.studio.ui.theme.*

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(CyberPanel)
            .border(1.dp, CyberBorder, RoundedCornerShape(16.dp)).padding(14.dp),
        content = content
    )
}

@Composable
fun NeonButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, icon: ImageVector? = null, magenta: Boolean = false, enabled: Boolean = true, loading: Boolean = false) {
    val bg = if (magenta) CyberMagenta else CyberCyan
    Button(onClick = onClick, enabled = enabled && !loading, colors = ButtonDefaults.buttonColors(containerColor = bg, contentColor = CyberBg), modifier = modifier.height(52.dp), shape = RoundedCornerShape(14.dp)) {
        if (loading) { CircularProgressIndicator(Modifier.size(20.dp), color = CyberBg, strokeWidth = 2.dp); Spacer(Modifier.width(10.dp)) }
        else if (icon != null) { Icon(icon, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)) }
        Text(text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EmptyState(title: String, subtitle: String, icon: ImageVector, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Column(Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(88.dp).clip(CircleShape).background(CyberCyan.copy(0.12f)).border(1.dp, CyberCyan.copy(0.35f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = CyberCyan, modifier = Modifier.size(40.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, color = CyberText, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, color = CyberTextDim, fontSize = 14.sp, textAlign = TextAlign.Center)
        if (actionLabel != null && onAction != null) { Spacer(Modifier.height(20.dp)); NeonButton(text = actionLabel, onClick = onAction) }
    }
}

@Composable
fun PulsingOrb(size: Dp = 160.dp, label: String = "A") {
    val transition = rememberInfiniteTransition(label = "orb")
    val scale by transition.animateFloat(0.96f, 1.04f, infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse), label = "s")
    val glow by transition.animateFloat(0.25f, 0.55f, infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Reverse), label = "g")
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size).scale(scale).clip(CircleShape).background(Brush.radialGradient(listOf(CyberCyan.copy(glow * 0.35f), CyberPanel))).border(2.dp, CyberCyan.copy(0.55f + glow * 0.3f), CircleShape)) {
        Text(label.take(1).uppercase().ifBlank { "A" }, color = CyberCyan, fontSize = (size.value * 0.32f).sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatusChip(text: String, active: Boolean = false, danger: Boolean = false) {
    val color = when { danger -> CyberMagenta; active -> CyberCyan; else -> CyberTextDim }
    Box(Modifier.clip(RoundedCornerShape(8.dp)).background(color.copy(0.15f)).border(1.dp, color.copy(0.4f), RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
