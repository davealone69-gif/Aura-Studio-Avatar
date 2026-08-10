package com.aura.studio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aura.studio.avatar.AvatarOptions
import com.aura.studio.avatar.AvatarSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarCreatorScreen(
    initial: AvatarSpec = AvatarSpec(),
    onSave: (AvatarSpec) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf(initial.name) }
    var age by remember { mutableStateOf(initial.age.toString()) }
    var ethnicity by remember { mutableStateOf(initial.ethnicity) }
    var bodyType by remember { mutableStateOf(initial.bodyType) }
    var breastSize by remember { mutableStateOf(initial.breastSize) }
    var eyeColor by remember { mutableStateOf(initial.eyeColor) }
    var hairColor by remember { mutableStateOf(initial.hairColor) }
    var hairStyle by remember { mutableStateOf(initial.hairStyle) }
    var skinTone by remember { mutableStateOf(initial.skinTone) }
    var clothing by remember { mutableStateOf(initial.clothing) }
    var isNude by remember { mutableStateOf(initial.isNude) }
    var extra by remember { mutableStateOf(initial.extra) }

    val currentSpec = AvatarSpec(
        id = initial.id,
        name = name,
        age = age.toIntOrNull()?.coerceIn(18, 45) ?: 22,
        ethnicity = ethnicity,
        bodyType = bodyType,
        breastSize = breastSize,
        eyeColor = eyeColor,
        hairColor = hairColor,
        hairStyle = hairStyle,
        skinTone = skinTone,
        clothing = if (isNude) "None" else clothing,
        extra = extra,
        isNude = isNude
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avatar Creator") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                },
                actions = {
                    TextButton(onClick = { onSave(currentSpec) }) {
                        Text("Save")
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = age,
                onValueChange = { if (it.length <= 2) age = it.filter { c -> c.isDigit() } },
                label = { Text("Age (18+)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            DropdownField("Ethnicity", ethnicity, AvatarOptions.ethnicities) { ethnicity = it }
            DropdownField("Body Type", bodyType, AvatarOptions.bodyTypes) { bodyType = it }
            DropdownField("Breast Size", breastSize, AvatarOptions.breastSizes) { breastSize = it }
            DropdownField("Eye Color", eyeColor, AvatarOptions.eyeColors) { eyeColor = it }
            DropdownField("Hair Color", hairColor, AvatarOptions.hairColors) { hairColor = it }
            DropdownField("Hair Style", hairStyle, AvatarOptions.hairStyles) { hairStyle = it }
            DropdownField("Skin Tone", skinTone, AvatarOptions.skinTones) { skinTone = it }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("No Clothes (Nude)", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isNude,
                    onCheckedChange = { isNude = it }
                )
            }

            if (!isNude) {
                DropdownField("Clothing", clothing, AvatarOptions.clothing.filter { it != "None" }) {
                    clothing = it
                }
            }

            OutlinedTextField(
                value = extra,
                onValueChange = { extra = it },
                label = { Text("Extra (tattoos, makeup, etc.)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Divider()

            Text("Prompt Preview", style = MaterialTheme.typography.titleMedium)
            Text(
                text = currentSpec.toPrompt(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
