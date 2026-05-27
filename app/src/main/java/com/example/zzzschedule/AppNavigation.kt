package com.example.zzzschedule

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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

import com.example.zzzschedule.home.AddTaskScreen
import com.example.zzzschedule.home.HomePageNoTaskScreen
import com.example.zzzschedule.home.Task
import com.example.zzzschedule.login.LoginPageScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var tasks by remember { mutableStateOf(listOf<Task>()) }

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

        // 2. HOME ROUTE
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
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(0))
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
                selectedDay = selectedDay,          // Pass down current selection state
                onDayChange = { selectedDay = it }, // Update state when toggled on home screen
                onAddTaskClick = { isTomorrow ->
                    navController.navigate("add_task/$isTomorrow")
                }
            )
        }

        // 3. ADD TASK ROUTE
        composable(
            route = "add_task/{isTomorrow}",
            arguments = listOf(
                navArgument("isTomorrow") { type = NavType.BoolType }
            ),
            enterTransition = {
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val isTomorrow = backStackEntry.arguments?.getBoolean("isTomorrow") ?: false

            AddTaskScreen(
                onCancel = { navController.popBackStack() },
                onSave = { title, start, end, priority, repeat ->
                    val newTask = Task(title, start, end, priority, repeat, isTomorrow)
                    tasks = tasks + newTask
                    navController.popBackStack()
                }
            )
        }
    }
}