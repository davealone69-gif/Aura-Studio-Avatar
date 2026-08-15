package com.aura.studio.ui

import androidx.compose.runtime.Composable
import com.aura.studio.avatar.AvatarSpec

/**
 * Designer entry point used by AuraNavGraph.
 * Delegates to AvatarCreatorScreen and surfaces an optional Generate action.
 */
@Composable
fun AvatarDesignerScreen(
    initial: AvatarSpec = AvatarSpec(),
    onSave: (AvatarSpec) -> Unit = {},
    onBack: () -> Unit = {},
    onGenerate: (AvatarSpec) -> Unit = {}
) {
    AvatarCreatorScreen(
        initial = initial,
        onSave = onSave,
        onBack = onBack
        // Generate is available after save via list/detail; keep creator focused.
    )
}
