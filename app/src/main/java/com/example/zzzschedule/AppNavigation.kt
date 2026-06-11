package com.example.zzzschedule

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zzzschedule.home.HomePageNoTaskScreen
import com.example.zzzschedule.home.Task
import com.example.zzzschedule.home.sampleTasksForToday
import com.example.zzzschedule.login.LoginPageScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var tasks by remember { mutableStateOf(sampleTasksForToday) }

    // Hoisted state to track selected day across screen switches
    var selectedDay by remember { mutableStateOf("Today") }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // 1. LOGIN ROUTE
        composable(
            route = "login",
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(animationSpec = tween(150))
            }
        ) {
            LoginPageScreen(
                onContinue = { username, age, occupation, sleepHours ->
                    navController.navigate("home?username=$username&age=$age&occupation=$occupation&sleepHours=$sleepHours") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 2. HOME ROUTE (Now self-contained with its creation sheet)
        composable(
            route = "home?username={username}&age={age}&occupation={occupation}&sleepHours={sleepHours}",
            arguments = listOf(
                navArgument("username") { defaultValue = "" },
                navArgument("age") { defaultValue = "" },
                navArgument("occupation") { defaultValue = "" },
                navArgument("sleepHours") {
                    type = NavType.IntType
                    defaultValue = 8
                }
            ),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(150))
            }
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val age = backStackEntry.arguments?.getString("age") ?: ""
            val occupation = backStackEntry.arguments?.getString("occupation") ?: ""
            val sleepHours = backStackEntry.arguments?.getInt("sleepHours") ?: 8

            HomePageNoTaskScreen(
                username = username,
                age = age,
                occupation = occupation,
                sleepHours = sleepHours,
                tasks = tasks,
                selectedDay = selectedDay,
                onDayChange = { selectedDay = it },
                onSaveNewTask = { title, start, end, priority, repeat, isTomorrow ->
                    val newTask = Task(
                        title = title,
                        startTime = start,
                        endTime = end,
                        priority = priority,
                        repeat = repeat,
                        isTomorrow = isTomorrow
                    )
                    tasks = tasks + newTask
                },
                onPostponeTask = { oldTask, newTitle, newStart, newEnd, newPriority, newRepeat, postponeDate ->
                    val updatedTasks = tasks.map { task ->
                        if (task == oldTask) {
                            task.copy(isPostponed = true, postponedToDate = postponeDate)
                        } else {
                            task
                        }
                    }
                    
                    val tomorrowDate = com.example.zzzschedule.home.getNext7Days().getOrNull(1)
                    val isTomorrow = postponeDate == tomorrowDate || postponeDate == "Tomorrow"
                    
                    val newTask = Task(
                        title = newTitle,
                        startTime = newStart,
                        endTime = newEnd,
                        priority = newPriority,
                        repeat = newRepeat,
                        isTomorrow = isTomorrow,
                        postponedToDate = if (!isTomorrow && postponeDate != com.example.zzzschedule.home.getNext7Days().getOrNull(0)) postponeDate else null
                    )
                    tasks = updatedTasks + newTask
                }
            )
        }
    }
}