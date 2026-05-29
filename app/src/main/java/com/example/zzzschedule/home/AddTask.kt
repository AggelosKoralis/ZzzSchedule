package com.example.zzzschedule.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable // Added for full card clickability
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.material.icons.filled.Event

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
    initialTitle: String = "",
    initialStartTime: String = "09:00",
    initialEndTime: String = "10:30",
    initialPriority: String = "Low",
    initialRepeat: String = "None",
    isPostponeMode: Boolean = false,
    onCancel: () -> Unit = {},
    // Added postponeDay to the return closure
    onSave: (title: String, startTime: String, endTime: String, priority: String, repeat: String, postponeDay: String?) -> Unit = { _, _, _, _, _, _ -> }
) {
    var title by remember { mutableStateOf(initialTitle) }
    var startTime by remember { mutableStateOf(initialStartTime) }
    var endTime by remember { mutableStateOf(initialEndTime) }
    var priority by remember { mutableStateOf(initialPriority) }
    var repeat by remember { mutableStateOf(initialRepeat) }

    // Postpone state
    val availableDates = remember { getNext7Days() }
    var postponeDay by remember { mutableStateOf(availableDates[1]) } // Default to tomorrow

    var showPriorityDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }
    var showPostponeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLow)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel, colors = ButtonDefaults.textButtonColors(contentColor = Secondary)) {
                Text(text = "Cancel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = if (isPostponeMode) "Postpone Task" else "Add Task",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    onSave(title, startTime, endTime, priority, repeat, if (isPostponeMode) postponeDay else null)
                },
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = Color(0xFF22005C))
            ) {
                Text(text = "Save", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isPostponeMode) {
            SelectionRow(
                title = "Postpone to Date",
                value = postponeDay,
                icon = Icons.Default.Event,
                iconColor = Secondary,
                onSelect = { showPostponeDialog = true }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                showPriorityDialog = false
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
                showRepeatDialog = false
            }
        )
    }
    if (showPostponeDialog) {
        SelectionDialog(
            title = "Postpone to",
            options = availableDates,
            selected = postponeDay,
            onDismiss = { showPostponeDialog = false },
            onSelected = {
                postponeDay = it
                showPostponeDialog = false
            }
        )
    }
}

@Composable
private fun TimeCard(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val timeParts = value.split(":")
    var hour by remember { mutableIntStateOf(timeParts.getOrNull(0)?.toIntOrNull() ?: 9) }
    var minute by remember { mutableIntStateOf(timeParts.getOrNull(1)?.toIntOrNull() ?: 0) }

    // Update parent when either picker changes
    LaunchedEffect(hour, minute) {
        onValueChange(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
    }

    val hourItems = remember { (0..23).map { String.format(Locale.getDefault(), "%02d", it) } }
    val minuteItems = remember { (0..59).map { String.format(Locale.getDefault(), "%02d", it) } }

    Column(
        modifier = modifier
            .background(Surface, RoundedCornerShape(20.dp))
            .border(1.dp, Outline.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, color = TextPrimary, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            InfiniteWheelPicker(
                items = hourItems,
                initialIndex = hour,
                onItemSelected = { hour = it },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = ":",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            InfiniteWheelPicker(
                items = minuteItems,
                initialIndex = minute,
                onItemSelected = { minute = it },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InfiniteWheelPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    initialIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val pageSize = items.size
    // Use a large multiple of pageSize for "infinite" scrolling effect
    val midIndex = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % pageSize)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = midIndex + initialIndex - 1)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val currentCenterIndex by remember {
        derivedStateOf {
            (listState.firstVisibleItemIndex + 1) % pageSize
        }
    }

    LaunchedEffect(currentCenterIndex) {
        onItemSelected(currentCenterIndex)
    }

    Box(modifier = modifier.height(120.dp), contentAlignment = Alignment.Center) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(Int.MAX_VALUE) { index ->
                val itemIndex = index % pageSize
                val isSelected = itemIndex == currentCenterIndex

                Box(
                    modifier = Modifier.height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[itemIndex],
                        fontSize = if (isSelected) 22.sp else 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) TextPrimary else TextSecondary.copy(alpha = 0.35f)
                    )
                }
            }
        }
        
        // Optional: faint selection indicators
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp).offset(y = (-20).dp),
            thickness = 0.5.dp,
            color = Outline.copy(alpha = 0.2f)
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp).offset(y = 20.dp),
            thickness = 0.5.dp,
            color = Outline.copy(alpha = 0.2f)
        )
    }
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

fun getNext7Days(): List<String> {
    // "EEE" adds the short day name (Mon, Tue, Wed...)
    val formatter = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<String>()

    for (i in 0..7) {
        dates.add(formatter.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return dates
}