package com.example.zzzschedule.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color as ComposeColor
import kotlin.math.pow

data class Task(
    val title: String,
    val startTime: String,
    val endTime: String,
    val priority: String,
    val repeat: String,
)


private val Background = Color(0xFF141317)
private val Surface = Color(0xFF201F23)
private val SurfaceHigh = Color(0xFF2B292D)
private val SurfaceHighest = Color(0xFF353438)

private val Primary = Color(0xFFE9DDFF)
private val Secondary = Color(0xFFD0BCFF)

private val TextPrimary = Color(0xFFE5E1E7)
private val TextSecondary = Color(0xFFCAC4D0)

private val Outline = Color(0xFF49454F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageNoTaskScreen(
    username: String = "",
    age: String = "",
    occupation: String = "",
    sleepHours: Int = 8,
    tasks: List<Task> = emptyList(),
    onHomeClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onInsightsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAddTaskClick: () -> Unit = {}
) {

    Scaffold(

        containerColor = Background,

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (username.isNotEmpty()) "Hi, $username!" else "Daily Overview",
                        color = Primary,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.NotificationsNone,
                            contentDescription = null,
                            tint = Primary
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },

        bottomBar = {

            NavigationBar(
                containerColor = Surface
            ) {

                NavigationBarItem(
                    selected = true,
                    onClick = onHomeClick,

                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Home")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF22005C),
                        selectedTextColor = Secondary,
                        indicatorColor = Secondary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onScheduleClick,

                    icon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Schedule")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onInsightsClick,

                    icon = {
                        Icon(
                            Icons.Default.Insights,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Insights")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onSettingsClick,

                    icon = {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Settings")
                    }
                )
            }
        }

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
                    .border(
                        width = 1.dp,
                        color = ComposeColor.White.copy(alpha = 0.06f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(24.dp)
            ) {

                Column {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column {

                            Text(
                                text = "Estimated energy",
                                color = TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (occupation.isNotEmpty() && occupation != "Select") 
                                    "Based on your lifestyle"
                                    else "Based on last night's rest",
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            val energyProgress = calculateEnergy(sleepHours.toFloat())

                            CircularProgressIndicator(
                                progress = { energyProgress },
                                modifier = Modifier.size(140.dp),
                                strokeWidth = 12.dp,
                                color = Primary,
                                trackColor = SurfaceHigh
                            )

                            Text(
                                text = "${(energyProgress * 100).toInt()}%",
                                color = Primary,
                                fontSize = 46.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (tasks.isEmpty()) {
                // EMPTY SCHEDULE SECTION

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(SurfaceHigh),

                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Your schedule is clear today",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(

                        onClick = onAddTaskClick,

                        shape = RoundedCornerShape(100.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = Color(0xFF37265E)
                        ),

                        modifier = Modifier.height(56.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Add Task",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                // TASKS LIST

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Schedule",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(onClick = onAddTaskClick) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Task",
                            tint = Primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val sortedTasks = tasks.sortedBy { it.startTime }

                sortedTasks.forEach { task ->
                    TaskCard(
                        title = task.title,
                        time = "${task.startTime} - ${task.endTime}",
                        priority = task.priority.uppercase(),
                        priorityColor = when (task.priority) {
                            "High" -> ComposeColor.Red
                            "Medium" -> ComposeColor(0xFF7E57C2)
                            else -> ComposeColor.Gray
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

//            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

fun calculateEnergy(sleepHours: Float): Float {
    // We clip the input between 0 and 12 hours just to keep bounds safe
    val hours = sleepHours.coerceIn(0f, 12f)

    // Centers the curve around 6.5 hours and controls the steepness
    val midpoint = 6.5f
    val steepness = 1.2f

    val k = (hours - midpoint) * steepness
    val sigmoid = (k / (1f + kotlin.math.abs(k))) // Fast sigmoid approximation

    // Normalize the result so that 4h feels low (~15%) and 8h feels great (~90%)
    // Then coerce to make sure it's strictly between 0.0 and 1.0
    return ((sigmoid + 1f) / 2f).coerceIn(0f, 1f)
}

@Composable
fun TaskCard(
    title: String,
    time: String,
    priority: String,
    priorityColor: ComposeColor,
    showPostpone: Boolean = false
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Surface)
            .border(
                1.dp,
                ComposeColor.White.copy(alpha = 0.05f),
                RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        1.dp,
                        TextSecondary,
                        CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(priorityColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {

                        Text(
                            text = priority,
                            color = priorityColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = time,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = TextSecondary
            )
        }

        if (showPostpone) {

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(
                        1.dp,
                        ComposeColor.White.copy(alpha = 0.12f)
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Update,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = TextPrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Postpone",
                        color = TextPrimary
                    )
                }
            }
        }
    }
}