package com.example.zzzschedule

import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.myApp.login.LoginFileScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }
}

private val Background = ComposeColor(0xFF141317)
private val Surface = ComposeColor(0xFF201F23)
private val SurfaceHigh = ComposeColor(0xFF2B292D)
private val Primary = ComposeColor(0xFFE9DDFF)
private val Secondary = ComposeColor(0xFFD0BCFF)
private val TextPrimary = ComposeColor(0xFFE5E1E7)
private val TextSecondary = ComposeColor(0xFFCAC4D0)

@Composable
fun MyApp() {

    var showHomePage by remember {
        mutableStateOf(false)
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Primary,
            secondary = Secondary,
            surface = Surface,
            background = Background
        )
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {

            if (showHomePage) {
                HomePage()
            } else {
                LoginFileScreen(
                    onContinue = { age, occupation, sleepHours ->

                        // You can save data here later

                        showHomePage = true
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {

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
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = null)
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.DateRange, contentDescription = null)
                    },
                    label = { Text("Schedule") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Insights, contentDescription = null)
                    },
                    label = { Text("Insights") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    },
                    label = { Text("Settings") }
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

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

            Spacer(modifier = Modifier.height(30.dp))

            // HEADER

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Today's Schedule",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "View All",
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ALERT BANNER

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceHigh)
                    .padding(16.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = ComposeColor(0xFFFFB4AB)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        text = "Energy Dip Expected",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Energy is predicted to be lower around 2:00 PM. Consider postponing low-priority tasks.",
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TASKS

            TaskCard(
                title = "Project Presentation",
                time = "10:00 AM - 11:30 AM",
                priority = "HIGH",
                priorityColor = ComposeColor.Red
            )

            Spacer(modifier = Modifier.height(14.dp))

            TaskCard(
                title = "Weekly Sync",
                time = "1:00 PM - 2:00 PM",
                priority = "MEDIUM",
                priorityColor = ComposeColor(0xFF7E57C2)
            )

            Spacer(modifier = Modifier.height(14.dp))

            TaskCard(
                title = "Inbox Zero",
                time = "2:30 PM - 3:30 PM",
                priority = "LOW",
                priorityColor = ComposeColor.Gray,
                showPostpone = true
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
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

