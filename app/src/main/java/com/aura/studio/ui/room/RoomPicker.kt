package com.aura.studio.ui.room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.studio.domain.room.RoomDefinition
import com.aura.studio.ui.theme.*

@Composable
fun RoomPicker(
    rooms: List<RoomDefinition>,
    selectedRoomId: String?,
    selectedInteractionId: String?,
    onSelectRoom: (String?) -> Unit,
    onSelectInteraction: (String?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("SCENE ROOM", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text(
                    "None",
                    color = if (selectedRoomId == null) CyberBg else CyberText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .background(if (selectedRoomId == null) CyberCyan else CyberPanel)
                        .border(1.dp, if (selectedRoomId == null) CyberCyan else CyberBorder, RoundedCornerShape(20.dp))
                        .clickable { onSelectRoom(null); onSelectInteraction(null) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
            items(rooms, key = { it.id }) { room ->
                val sel = selectedRoomId == room.id
                Text(
                    room.displayName,
                    color = if (sel) CyberBg else CyberText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .background(if (sel) CyberCyan else CyberPanel)
                        .border(1.dp, if (sel) CyberCyan else CyberBorder, RoundedCornerShape(20.dp))
                        .clickable { onSelectRoom(room.id); onSelectInteraction(null) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
        val room = rooms.find { it.id == selectedRoomId }
        if (room != null && room.interactions.isNotEmpty()) {
            Text("INTERACTION", color = CyberCyanDim, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(room.interactions, key = { it.id }) { inter ->
                    FilterChip(
                        selected = selectedInteractionId == inter.id,
                        onClick = {
                            onSelectInteraction(if (selectedInteractionId == inter.id) null else inter.id)
                        },
                        label = { Text(inter.label, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyberMagenta.copy(0.25f),
                            selectedLabelColor = CyberMagenta
                        )
                    )
                }
            }
        }
    }
}
