package com.example.zzzschedule.home

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
                        text = "Daily Overview",
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
                        selectedTextColor = Color(0xFF22005C),
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
                                text = "Based on last night's rest",
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

                            CircularProgressIndicator(
                                progress = { 0.72f },
                                modifier = Modifier.size(140.dp),
                                strokeWidth = 12.dp,
                                color = Primary,
                                trackColor = SurfaceHigh
                            )

                            Text(
                                text = "72%",
                                color = Primary,
                                fontSize = 46.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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

//            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}