package com.example.zzzschedule.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable // Added for full card clickability
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // Added to clip ripple effect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.DialogProperties


private val Background = Color(0xFF141317)
private val Surface = Color(0xFF201F23)
private val SurfaceLow = Color(0xFF1C1B1F)
private val SurfaceHigh = Color(0xFF2B292D)
private val SurfaceHighest = Color(0xFF353438)

private val Primary = Color(0xFFE9DDFF)
private val Secondary = Color(0xFFD0BCFF)

private val TextPrimary = Color(0xFFE5E1E7)
private val TextSecondary = Color(0xFFCAC4D0)

private val Outline = Color(0xFF49454F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onCancel: () -> Unit = {},
    onSave: (title: String, startTime: String, endTime: String, priority: String, repeat: String) -> Unit = { _, _, _, _, _ -> }
) {
    var title by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:30") }
    var priority by remember { mutableStateOf("Low") }
    var repeat by remember { mutableStateOf("None") }

    var showPriorityDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLow)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        // Dynamic Bottom Sheet Headers replacing TopAppBar
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel, colors = ButtonDefaults.textButtonColors(contentColor = Secondary)) {
                Text(text = "Cancel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = "Add Task", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Button(
                onClick = { onSave(title, startTime, endTime, priority, repeat) },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = Color(0xFF22005C))
            ) {
                Text(text = "Save", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Task Title", color = Primary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter task title") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = Outline,
                focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TimeCard(label = "Start", value = startTime, onValueChange = { startTime = it }, modifier = Modifier.weight(1f))
            TimeCard(label = "End", value = endTime, onValueChange = { endTime = it }, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        SelectionRow(title = "Priority", value = priority, icon = Icons.Default.PriorityHigh, iconColor = Color(0xFFFFB4AB), onSelect = { showPriorityDialog = true })
        Spacer(modifier = Modifier.height(16.dp))
        SelectionRow(title = "Repeat", value = repeat, icon = Icons.Default.EventRepeat, iconColor = Secondary, onSelect = { showRepeatDialog = true })

        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showPriorityDialog) {
        SelectionDialog(
            title = "Select Priority",
            options = listOf("Low", "Medium", "High"),
            selected = priority,
            onDismiss = { showPriorityDialog = false },
            onSelected = {
                priority = it
                showPriorityDialog = false // <-- Added this
            }
        )
    }
    if (showRepeatDialog) {
        SelectionDialog(
            title = "Repeat Cycle",
            options = listOf("Daily", "Weekly", "Monthly", "None"),
            selected = repeat,
            onDismiss = { showRepeatDialog = false },
            onSelected = {
                repeat = it
                showRepeatDialog = false // <-- Added this
            }
        )
    }
}

@Composable
private fun TimeCard(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) { if (value.isEmpty()) onValueChange("09:00") }

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = value.ifEmpty { "09:00" })) }
    LaunchedEffect(value) {
        if (value.isNotEmpty() && value != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = value)
        }
    }

    Column(
        modifier = modifier
            .background(Surface, RoundedCornerShape(20.dp))
            .border(1.dp, Outline.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Text(text = label, color = TextPrimary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                val processedValue = handleTimeInput(textFieldValue, newValue)
                textFieldValue = processedValue
                onValueChange(processedValue.text)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
        )
    }
}

private fun handleTimeInput(old: TextFieldValue, new: TextFieldValue): TextFieldValue {
    if (new.text.isEmpty()) return TextFieldValue("00:00", TextRange(0))
    val lengthDiff = new.text.length - old.text.length
    if (Math.abs(lengthDiff) > 1) return old

    if (lengthDiff == 1) {
        val cursorPosition = old.selection.start
        if (cursorPosition >= 5) return old
        val insertedChar = new.text[cursorPosition]
        if (!insertedChar.isDigit()) return old

        val chars = old.text.toCharArray()
        var nextCursor = cursorPosition + 1
        if (cursorPosition == 2) {
            chars[3] = insertedChar
            nextCursor = 4
        } else {
            chars[cursorPosition] = insertedChar
            if (nextCursor == 2) nextCursor = 3
        }
        return TextFieldValue(String(chars), TextRange(nextCursor))
    }

    if (lengthDiff == -1) {
        val deletedPosition = old.selection.start - 1
        if (deletedPosition < 0) return old
        val chars = old.text.toCharArray()
        var nextCursor = deletedPosition
        if (deletedPosition == 2) {
            chars[1] = '0'
            nextCursor = 1
        } else {
            chars[deletedPosition] = '0'
        }
        return TextFieldValue(String(chars), TextRange(nextCursor))
    }
    return new
}

@Composable
private fun SelectionRow(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface, RoundedCornerShape(22.dp))
            .border(1.dp, Outline.copy(alpha = 0.1f), RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onSelect)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, color = TextPrimary, fontWeight = FontWeight.Medium)
                Text(text = value, color = TextSecondary)
            }
        }
        Text(text = "Select", color = Primary, fontWeight = FontWeight.Medium, modifier = Modifier.padding(end = 8.dp))
    }
}

@Composable
private fun SelectionDialog(
    title: String,
    options: List<String>,
    selected: String,
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceHigh,
        // 1. Turn off default platform stretching behavior
        properties = DialogProperties(usePlatformDefaultWidth = false),
        // 2. Set a custom width (e.g., 85% of screen width or fixed dp like 300.dp)
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .wrapContentHeight(),
        title = { Text(text = title, color = TextPrimary) },
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option == selected),
                                onClick = { onSelected(option) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selected),
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Primary,
                                unselectedColor = TextSecondary
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = option, color = TextPrimary)
                    }
                }
            }
        },
        confirmButton = {}
    )
}