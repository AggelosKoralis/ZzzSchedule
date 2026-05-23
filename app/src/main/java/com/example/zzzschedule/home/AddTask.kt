package com.example.zzzschedule.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
    onSave: (
        title: String,
        startTime: String,
        endTime: String,
        priority: String,
        repeat: String
    ) -> Unit = { _, _, _, _, _ -> }

) {

    var title by remember { mutableStateOf("") }

    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:30") }

    var priority by remember { mutableStateOf("Low") }
    var repeat by remember { mutableStateOf("None") }

    var showPriorityDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }

    Scaffold(

        containerColor = Background,

        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 12.dp),
                title = {
                    Text(
                        text = "Add Task",
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary,
                            contentColor = Color(0xFF22005C)
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            onSave(
                                title,
                                startTime,
                                endTime,
                                priority,
                                repeat
                            )
                        },
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary,
                            contentColor = Color(0xFF22005C)
                        )
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors( // Updated color builder
                    containerColor = SurfaceLow
                )
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceLow)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp)

        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // TASK TITLE

            Text(
                text = "Task Title",
                color = Primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(

                value = title,
                onValueChange = { title = it },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text("Enter task title")
                },


                shape = RoundedCornerShape(14.dp),

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Outline,
                    focusedContainerColor = SurfaceLow,
                    unfocusedContainerColor = SurfaceLow,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            // alternative version for the input field

//            OutlinedTextField(
//
//                value = title,
//                onValueChange = { title = it },
//
//                modifier = Modifier.fillMaxWidth(),
//
//                placeholder = {
//                    Text(
//                        "Enter task title",
//                        color = TextSecondary.copy(alpha = 0.5f)
//                    )
//                },
//
//                colors = OutlinedTextFieldDefaults.colors(
//
//                    focusedContainerColor = SurfaceHighest,
//                    unfocusedContainerColor = SurfaceHighest,
//
//                    focusedBorderColor = Primary,
//                    unfocusedBorderColor = Outline,
//
//                    focusedTextColor = TextPrimary,
//                    unfocusedTextColor = TextPrimary
//                ),
//
//                shape = RoundedCornerShape(18.dp)
//            )

            Spacer(modifier = Modifier.height(32.dp))

            // TIME RANGE

            Text(
                text = "Time Range",
                color = Primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TimeCard(
                    label = "Start",
//                    icon = Icons.Default.Schedule,
                    value = startTime,
                    onValueChange = { startTime = it },
                    modifier = Modifier.weight(1f)
                )

                TimeCard(
                    label = "End",
//                    icon = Icons.Default.Timer,
                    value = endTime,
                    onValueChange = { endTime = it },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PRIORITY

            SelectionRow(

                title = "Priority",
                value = priority,
                icon = Icons.Default.PriorityHigh,
                iconColor = Color(0xFFFFB4AB),

                onSelect = {
                    showPriorityDialog = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // REPEAT

            SelectionRow(

                title = "Repeat",
                value = repeat,
                icon = Icons.Default.EventRepeat,
                iconColor = Secondary,

                onSelect = {
                    showRepeatDialog = true
                }
            )
        }
    }

    // PRIORITY DIALOG

    if (showPriorityDialog) {

        SelectionDialog(

            title = "Select Priority",

            options = listOf(
                "Low",
                "Medium",
                "High"
            ),

            selected = priority,

            onDismiss = {
                showPriorityDialog = false
            },

            onSelected = {

                priority = it
                showPriorityDialog = false
            }
        )
    }

    // REPEAT DIALOG

    if (showRepeatDialog) {

        SelectionDialog(

            title = "Repeat Cycle",

            options = listOf(
                "Daily",
                "Weekly",
                "Monthly",
                "None"
            ),

            selected = repeat,

            onDismiss = {
                showRepeatDialog = false
            },

            onSelected = {

                repeat = it
                showRepeatDialog = false
            }
        )
    }
}

@Composable
private fun TimeCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    // Set the initial default value to "09:00" if the parent passes an empty string
    LaunchedEffect(Unit) {
        if (value.isEmpty()) {
            onValueChange("09:00")
        }
    }

    // Use TextFieldValue to manage both the text and the cursor position
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value.ifEmpty { "09:00" }
            )
        )
    }

    // Sync state if parent explicitly changes the value (e.g., clearing the form)
    LaunchedEffect(value) {
        if (value.isNotEmpty() && value != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = value)
        }
    }

    Column(
        modifier = modifier
            .background(Surface, RoundedCornerShape(20.dp))
            .border(1.dp, Outline.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {

        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textFieldValue, // Using TextFieldValue instead of String
                onValueChange = { newValue ->
                    // Process the input through our overwrite logic
                    val processedValue = handleTimeInput(textFieldValue, newValue)

                    // Update local cursor/text state
                    textFieldValue = processedValue

                    // Send just the string back to the parent
                    onValueChange(processedValue.text)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}

/**
 * Handles the logic for overwriting characters, jumping the colon, and zeroing out on backspace.
 */
private fun handleTimeInput(old: TextFieldValue, new: TextFieldValue): TextFieldValue {
    // 1. If user highlights all and deletes, reset to default
    if (new.text.isEmpty()) {
        return TextFieldValue("00:00", TextRange(0))
    }

    // Prevent multi-character operations (like pasting a whole sentence) to keep the mask stable
    val lengthDiff = new.text.length - old.text.length
    if (Math.abs(lengthDiff) > 1) return old

    // 2. Handle Typing (Insertion)
    if (lengthDiff == 1) {
        val cursorPosition = old.selection.start
        if (cursorPosition >= 5) return old // Block typing past the 5th character (e.g., 09:00)

        val insertedChar = new.text[cursorPosition]
        if (!insertedChar.isDigit()) return old // Only allow numbers

        val chars = old.text.toCharArray()
        var nextCursor = cursorPosition + 1

        if (cursorPosition == 2) {
            // User typed exactly at the colon. Apply the digit to the minutes instead.
            chars[3] = insertedChar
            nextCursor = 4
        } else {
            // Overwrite the existing digit
            chars[cursorPosition] = insertedChar
            // Skip the cursor over the colon
            if (nextCursor == 2) nextCursor = 3
        }

        return TextFieldValue(String(chars), TextRange(nextCursor))
    }

    // 3. Handle Backspace (Deletion)
    if (lengthDiff == -1) {
        val deletedPosition = old.selection.start - 1
        if (deletedPosition < 0) return old

        val chars = old.text.toCharArray()
        var nextCursor = deletedPosition

        if (deletedPosition == 2) {
            // User backspaced the colon. We delete the hour digit before the colon instead.
            chars[1] = '0'
            nextCursor = 1
        } else {
            // Replace deleted digit with a '0'
            chars[deletedPosition] = '0'
        }

        return TextFieldValue(String(chars), TextRange(nextCursor))
    }

    // 4. Handle simple cursor movement (clicking around)
    return new
}

@Composable
private fun SelectionRow(

    title: String,
    value: String,

    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,

    onSelect: () -> Unit

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                Surface,
                RoundedCornerShape(22.dp)
            )
            .border(
                1.dp,
                Outline.copy(alpha = 0.1f),
                RoundedCornerShape(22.dp)
            )
            .padding(16.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(

                modifier = Modifier
                    .size(40.dp)
                    .background(
                        iconColor.copy(alpha = 0.15f),
                        CircleShape
                    ),

                contentAlignment = Alignment.Center

            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {

                Text(
                    text = title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = value,
                    color = TextSecondary
                )
            }
        }

        TextButton(
            onClick = onSelect
        ) {

            Text(
                text = "Select",
                color = Primary
            )
        }
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

        title = {

            Text(
                text = title,
                color = TextPrimary
            )
        },

        text = {

            Column(
                modifier = Modifier.selectableGroup()
            ) {

                options.forEach { option ->

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option == selected),
                                onClick = {
                                    onSelected(option)
                                },
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

                        Text(
                            text = option,
                            color = TextPrimary
                        )
                    }
                }
            }
        },

        confirmButton = {}
    )
}