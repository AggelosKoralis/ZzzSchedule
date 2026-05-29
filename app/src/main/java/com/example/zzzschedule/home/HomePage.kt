package com.example.zzzschedule.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color as ComposeColor

// --- COLOR PALETTE DEFINITIONS ---
private val Background = Color(0xFF141317)
private val Surface = Color(0xFF201F23)
private val SurfaceLow = Color(0xFF1C1B1F)
private val SurfaceHigh = Color(0xFF2B292D)

private val Primary = Color(0xFFE9DDFF)
private val Secondary = Color(0xFFD0BCFF)

private val TextPrimary = Color(0xFFE5E1E7)
private val TextSecondary = Color(0xFFCAC4D0)

private val Outline = Color(0xFF49454F)

data class Task(
    val title: String,
    val startTime: String,
    val endTime: String,
    val priority: String,
    val repeat: String,
    val isTomorrow: Boolean = false,
    val isCompleted: Boolean = false,
    val isPostponed: Boolean = false,
    val postponedToDate: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageNoTaskScreen(
    username: String = "",
    age: String = "",
    occupation: String = "",
    sleepHours: Int = 8,
    tasks: List<Task> = emptyList(),
    selectedDay: String = "Today",
    onDayChange: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onInsightsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onSaveNewTask: (title: String, start: String, end: String, priority: String, repeat: String, isTomorrow: Boolean) -> Unit = { _, _, _, _, _, _ -> },
    onToggleTaskCompletion: (Task) -> Unit = {},
    // NEW CALLBACK: To handle postponing
    onPostponeTask: (oldTask: Task, newTitle: String, newStart: String, newEnd: String, newPriority: String, newRepeat: String, postponeDate: String) -> Unit = { _, _, _, _, _, _, _ -> }
) {
    var isCompletedExpanded by remember { mutableStateOf(false) }
    var isPostponedExpanded by remember { mutableStateOf(false) }

    // --- BOTTOM SHEET STATE MANAGEMENT ---
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var targetTaskDayIsTomorrow by remember { mutableStateOf(false) }

    // NEW STATE: Track if we are editing/postponing a specific task
    var taskToPostpone by remember { mutableStateOf<Task?>(null) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val currentDayTasks = remember(tasks, selectedDay) {
        if (selectedDay == "Today") {
            tasks.filter { !it.isTomorrow }
        } else {
            tasks.filter { it.isTomorrow || (!it.isTomorrow && it.repeat.equals("daily", ignoreCase = true)) }
                .map { task ->
                    if (!task.isTomorrow && task.repeat.equals("daily", ignoreCase = true)) {
                        task.copy(isCompleted = false)
                    } else {
                        task
                    }
                }
        }
    }

    val activeTasks = currentDayTasks.filter { !it.isCompleted && !it.isPostponed }
    val completedTasks = currentDayTasks.filter { it.isCompleted && !it.isPostponed }
    val postponedTasks = currentDayTasks.filter { it.isPostponed }

    // Calculate energy once to use across the UI
    val energyProgress = calculateEnergy(sleepHours.toFloat())
    val suggestPostpone = energyProgress < 0.60f && selectedDay == "Today"

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (username.isNotEmpty()) "Hello, $username!" else "Daily Overview",
                        color = Primary,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ENERGY CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Secondary.copy(alpha = 0.18f),
                                ComposeColor(0xFF4F378A).copy(alpha = 0.35f)
                            )
                        )
                    )
                    .border(width = 1.dp, color = ComposeColor.White.copy(alpha = 0.06f), shape = RoundedCornerShape(28.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(text = "Estimated energy", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (occupation.isNotEmpty() && occupation != "Select") "Based on your lifestyle" else "Based on last night's rest",
                                color = TextSecondary
                            )
                        }
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Primary, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { energyProgress },
                                modifier = Modifier.size(140.dp),
                                strokeWidth = 12.dp,
                                color = Primary,
                                trackColor = SurfaceHigh
                            )
                            Text(text = "${(energyProgress * 100).toInt()}%", color = Primary, fontSize = 46.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // ... (DAY SELECTOR AND EMPTY STATE REMAIN UNCHANGED) ...
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Today", "Tomorrow").forEach { day ->
                    val isSelected = selectedDay == day
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Primary else Color.Transparent)
                            .clickable { onDayChange(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            color = if (isSelected) Color(0xFF37265E) else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (activeTasks.isEmpty()) {
                // ... (EMPTY STATE REMAINS THE SAME) ...
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(90.dp).clip(CircleShape).background(SurfaceHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.EventAvailable, contentDescription = null, tint = Primary, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = if (selectedDay == "Today") "Lots of free time today!" else "You are free tomorrow!",
                        color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            taskToPostpone = null // Ensure it's a new task
                            targetTaskDayIsTomorrow = selectedDay == "Tomorrow"
                            showBottomSheet = true
                        },
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color(0xFF37265E)),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Add Task", fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                // TASKS LIST
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "$selectedDay's Schedule", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    IconButton(onClick = {
                        taskToPostpone = null // Ensure it's a new task
                        targetTaskDayIsTomorrow = selectedDay == "Tomorrow"
                        showBottomSheet = true
                    }) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Task", tint = Primary, modifier = Modifier.size(36.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val sortedTasks = activeTasks.sortedBy { it.startTime }
                sortedTasks.forEach { task ->
                    val isLowPriority = task.priority.equals("Low", ignoreCase = true)

                    TaskCard(
                        title = task.title,
                        time = "${task.startTime} - ${task.endTime}",
                        priority = task.priority.uppercase(),
                        isCompleted = task.isCompleted,
                        showCheckbox = selectedDay == "Today",
                        onCheckedChange = { onToggleTaskCompletion(task) },
                        priorityColor = when (task.priority) {
                            "High" -> ComposeColor.Red
                            "Medium" -> ComposeColor(0xFF7E57C2)
                            else -> ComposeColor.Gray
                        },
                        showPostpone = suggestPostpone && isLowPriority,
                        onPostponeClick = {
                            taskToPostpone = task
                            showBottomSheet = true
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // POSTPONED SECTION
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPostponedExpanded = !isPostponedExpanded }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Postponed", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isPostponedExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null, tint = TextSecondary
                    )
                }

                if (isPostponedExpanded) {
                    if (postponedTasks.isEmpty()) {
                        Text(text = "Nothing postponed yet", color = TextSecondary, modifier = Modifier.padding(vertical = 12.dp))
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        postponedTasks.sortedBy { it.startTime }.forEach { task ->
                            TaskCard(
                                title = task.title,
                                time = "Postponed to: ${task.postponedToDate ?: "Another day"}",
                                priority = task.priority.uppercase(),
                                isCompleted = false,
                                showCheckbox = false,
                                priorityColor = when (task.priority) {
                                    "High" -> ComposeColor.Red
                                    "Medium" -> ComposeColor(0xFF7E57C2)
                                    else -> ComposeColor.Gray
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            // COMPLETED SECTION
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isCompletedExpanded = !isCompletedExpanded }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Completed", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isCompletedExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null, tint = TextSecondary
                    )
                }

                if (isCompletedExpanded) {
                    if (completedTasks.isEmpty()) {
                        Text(text = "So empty...", color = TextSecondary, modifier = Modifier.padding(vertical = 12.dp))
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        completedTasks.sortedBy { it.startTime }.forEach { task ->
                            TaskCard(
                                title = task.title,
                                time = "${task.startTime} - ${task.endTime}",
                                priority = task.priority.uppercase(),
                                isCompleted = task.isCompleted,
                                showCheckbox = true,
                                onCheckedChange = { onToggleTaskCompletion(task) },
                                priorityColor = when (task.priority) {
                                    "High" -> ComposeColor.Red
                                    "Medium" -> ComposeColor(0xFF7E57C2)
                                    else -> ComposeColor.Gray
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // add / postpone task menu
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = SurfaceLow,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = TextSecondary.copy(alpha = 0.4f),
                    width = 42.dp,
                    height = 5.dp
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = SurfaceLow
            ) {
                if (taskToPostpone != null) {
                    // Render in Postpone Mode
                    AddTaskScreen(
                        initialTitle = taskToPostpone!!.title,
                        initialStartTime = taskToPostpone!!.startTime,
                        initialEndTime = taskToPostpone!!.endTime,
                        initialPriority = taskToPostpone!!.priority,
                        initialRepeat = taskToPostpone!!.repeat,
                        isPostponeMode = true,
                        onCancel = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        },
                        onSave = { title, startTime, endTime, priority, repeat, postponeDay ->
                            onPostponeTask(taskToPostpone!!, title, startTime, endTime, priority, repeat, postponeDay ?: "Tomorrow")
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        }
                    )
                } else {
                    // Render in Add Mode
                    AddTaskScreen(
                        isPostponeMode = false,
                        onCancel = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        },
                        onSave = { title, startTime, endTime, priority, repeat, _ ->
                            onSaveNewTask(title, startTime, endTime, priority, repeat, targetTaskDayIsTomorrow)
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}

fun calculateEnergy(sleepHours: Float): Float {
    val hours = sleepHours.coerceIn(0f, 12f)
    val midpoint = 6.5f
    val steepness = 1.2f
    val k = (hours - midpoint) * steepness
    val sigmoid = (k / (1f + kotlin.math.abs(k)))
    return ((sigmoid + 1f) / 2f).coerceIn(0f, 1f)
}

@Composable
fun TaskCard(
    title: String,
    time: String,
    priority: String,
    priorityColor: ComposeColor,
    isCompleted: Boolean = false,
    showCheckbox: Boolean = true,
    showPostpone: Boolean = false,
    onCheckedChange: () -> Unit = {},
    onPostponeClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Surface)
            .border(1.dp, ComposeColor.White.copy(alpha = 0.05f), RoundedCornerShape(22.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showCheckbox) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) Primary else Color.Transparent)
                        .border(1.dp, if (isCompleted) Primary else TextSecondary, CircleShape)
                        .clickable { onCheckedChange() },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF37265E), modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, color = if (isCompleted) TextSecondary else TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(priorityColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = priority, color = priorityColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(6.6.dp))
                Text(text = time, color = TextSecondary, fontSize = 13.sp)
            }
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
        }

        if (showPostpone && !isCompleted) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Secondary.copy(alpha = 0.15f))
                        .clickable { onPostponeClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Postpone",
                        color = Secondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}