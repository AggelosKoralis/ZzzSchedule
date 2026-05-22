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

import com.example.zzzschedule.home.HomePageNoTaskScreen
import com.example.zzzschedule.login.LoginPageScreen
import com.example.zzzschedule.home.HomePageScreen

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
                HomePageNoTaskScreen()
            } else {
                LoginPageScreen(
                    onContinue = { username, age, occupation, sleepHours ->

                        // You can save data here later

                        showHomePage = true
                    }
                )
            }
        }
    }
}
