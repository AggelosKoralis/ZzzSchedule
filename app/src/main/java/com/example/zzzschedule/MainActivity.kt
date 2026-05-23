package com.example.zzzschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.zzzschedule.ui.theme.ZzzScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZzzScheduleTheme {
                // Boot up the navigation graph
                AppNavigation()
            }
        }
    }
}