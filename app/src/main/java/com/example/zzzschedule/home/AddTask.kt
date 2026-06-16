package com.example.zzzschedule.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.material.icons.filled.Event
import java.util.TimeZone

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
private val ErrorColor = Color(0xFFFFB4AB) // Reusing your high-priority color for error styling

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
    onSave: (title: String, startTime: String, endTime: String, priority: String, repeat: String, postponeDay: String?) -> Unit = { _, _, _, _, _, _ -> }
) {
    val focusManager = LocalFocusManager.current

    var title by remember { mutableStateOf(initialTitle) }
    var startTime by remember { mutableStateOf(initialStartTime) }
    var endTime by remember { mutableStateOf(initialEndTime) }
    var priority by remember { mutableStateOf(initialPriority) }
    var repeat by remember { mutableStateOf(initialRepeat) }

    val availableDates = remember { getNext7Days() }
    var postponeDay by remember { mutableStateOf(availableDates[1]) }

    var showPriorityDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }
    var showPostponeDialog by remember { mutableStateOf(false) }

    // --- Time Validation Logic ---
    val isTimeInvalid = remember(startTime, endTime) {
        val startParts = startTime.split(":")
        val endParts = endTime.split(":")

        val startMin = (startParts.getOrNull(0)?.toIntOrNull() ?: 0) * 60 + (startParts.getOrNull(1)?.toIntOrNull() ?: 0)
        val endMin = (endParts.getOrNull(0)?.toIntOrNull() ?: 0) * 60 + (endParts.getOrNull(1)?.toIntOrNull() ?: 0)

        startMin >= endMin
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceLow)
            .imePadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
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
                    focusManager.clearFocus()
                    // Guard against invalid time states on submission
                    if (!isTimeInvalid) {
                        onSave(title, startTime, endTime, priority, repeat, if (isPostponeMode) postponeDay else null)
                    }
                },
                shape = RoundedCornerShape(100.dp),
                // Visually soften the button state if time is invalid to signal it is blocked
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTimeInvalid) Secondary.copy(alpha = 0.5f) else Secondary,
                    contentColor = if (isTimeInvalid) Color(0xFF22005C).copy(alpha = 0.5f) else Color(0xFF22005C)
                )
            ) {
                Text(text = "Save", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isPostponeMode) {
            SelectionRow(
                title = "Move to Date",
                value = postponeDay,
                icon = Icons.Default.Event,
                iconColor = Secondary,
                onSelect = {
                    focusManager.clearFocus()
                    showPostponeDialog = true
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
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

        Spacer(modifier = Modifier.height(16.dp))

        SelectionRow(
            title = "Priority",
            value = priority,
            icon = Icons.Default.PriorityHigh,
            iconColor = ErrorColor,
            onSelect = {
                focusManager.clearFocus()
                showPriorityDialog = true
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SelectionRow(
            title = "Repeat",
            value = repeat,
            icon = Icons.Default.EventRepeat,
            iconColor = Secondary,
            onSelect = {
                focusManager.clearFocus()
                showRepeatDialog = true
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time cards setup with dynamic error parameters
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TimeCard(
                label = "Start",
                value = startTime,
                onValueChange = { startTime = it },
                isError = isTimeInvalid,
                modifier = Modifier.weight(1f)
            )
            TimeCard(
                label = "End",
                value = endTime,
                onValueChange = { endTime = it },
                isError = isTimeInvalid,
                modifier = Modifier.weight(1f)
            )
        }

        // Inline warning message rendered directly beneath the time pickers
        if (isTimeInvalid) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "End time must be after start time",
                color = ErrorColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
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
        CalendarDialog(
            onDismiss = { showPostponeDialog = false },
            onSelected = {
                postponeDay = it
                showPostponeDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarDialog(
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                return utcTimeMillis > calendar.timeInMillis
            }
        }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = SurfaceHigh
        ) {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Secondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Postpone to",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                val sdf = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
                                sdf.timeZone = TimeZone.getTimeZone("UTC")
                                onSelected(sdf.format(java.util.Date(it)))
                            }
                        },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = Color(0xFF22005C))
                    ) {
                        Text(text = "Save", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Box(modifier = Modifier.wrapContentSize()) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        title = null,
                        headline = null,
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Transparent,
                            titleContentColor = TextPrimary,
                            headlineContentColor = TextPrimary,
                            weekdayContentColor = TextSecondary,
                            subheadContentColor = TextSecondary,
                            yearContentColor = TextSecondary,
                            currentYearContentColor = Primary,
                            selectedYearContentColor = Color(0xFF22005C),
                            selectedYearContainerColor = Secondary,
                            dayContentColor = TextPrimary,
                            disabledDayContentColor = TextSecondary.copy(alpha = 0.3f),
                            selectedDayContentColor = Color(0xFF22005C),
                            selectedDayContainerColor = Secondary,
                            todayContentColor = Primary,
                            todayDateBorderColor = Primary
                        )
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = 16.dp, y = 12.dp)
                            .background(SurfaceHigh)
                            .size(width = 180.dp, height = 48.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        val displayedMonthText = remember(datePickerState.displayedMonthMillis) {
                            val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            sdf.format(java.util.Date(datePickerState.displayedMonthMillis))
                        }

                        Text(
                            text = displayedMonthText,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false, // Accepting structural validation states
    modifier: Modifier = Modifier
) {
    val timeParts = value.split(":")
    var hour by remember { mutableIntStateOf(timeParts.getOrNull(0)?.toIntOrNull() ?: 9) }
    var minute by remember { mutableIntStateOf(timeParts.getOrNull(1)?.toIntOrNull() ?: 0) }

    LaunchedEffect(hour, minute) {
        onValueChange(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
    }

    val hourItems = remember { (0..23).map { String.format(Locale.getDefault(), "%02d", it) } }
    val minuteItems = remember { (0..59).map { String.format(Locale.getDefault(), "%02d", it) } }

    Column(
        modifier = modifier
            .background(Surface, RoundedCornerShape(24.dp))
            // Tint the border color red/coral if an invalid time frame is parsed
            .border(
                width = 1.dp,
                color = if (isError) ErrorColor else Outline.copy(alpha = 0.15f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(vertical = 18.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tint the label to grab visual focus if invalid
        Text(
            text = label,
            color = if (isError) ErrorColor else Primary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            InfiniteWheelPicker(
                items = hourItems,
                initialIndex = hour,
                isError = isError,
                onItemSelected = { hour = it },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = ":",
                color = if (isError) ErrorColor else Primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            InfiniteWheelPicker(
                items = minuteItems,
                initialIndex = minute,
                isError = isError,
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
    isError: Boolean = false,
    onItemSelected: (Int) -> Unit
) {
    val pageSize = items.size
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

    Box(modifier = modifier.height(180.dp), contentAlignment = Alignment.Center) {
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
                    modifier = Modifier.height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[itemIndex],
                        fontSize = if (isSelected) 30.sp else 22.sp,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                        // Swap center highlight color based on validation state
                        color = if (isSelected) {
                            if (isError) ErrorColor else Primary
                        } else {
                            TextSecondary.copy(alpha = 0.35f)
                        }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp).offset(y = (-30).dp),
            thickness = 1.dp,
            color = if (isError) ErrorColor.copy(alpha = 0.3f) else Outline.copy(alpha = 0.3f)
        )
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 12.dp).offset(y = 30.dp),
            thickness = 1.dp,
            color = if (isError) ErrorColor.copy(alpha = 0.3f) else Outline.copy(alpha = 0.3f)
        )
    }
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
            .background(Surface, RoundedCornerShape(22.dp))
            .border(1.dp, Outline.copy(alpha = 0.1f), RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onSelect) // Keeps the whole row clickable
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = title, color = TextPrimary, fontWeight = FontWeight.Medium)
                Text(text = value, color = TextSecondary)
            }
        }

        // Styled pill container mimicking the top "Save" button
        Box(
            modifier = Modifier
                .background(Secondary, RoundedCornerShape(100.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select",
                color = Color(0xFF22005C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
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
        properties = DialogProperties(usePlatformDefaultWidth = false),
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
    val formatter = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<String>()

    for (i in 0..7) {
        dates.add(formatter.format(calendar.time))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return dates
}