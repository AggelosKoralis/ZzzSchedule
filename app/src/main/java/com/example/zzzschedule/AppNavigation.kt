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

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // 1. LOGIN ROUTE
        composable(
            route = "login",
            // When leaving Login to go to Home, slide out to the left
            // TODO: Save this user's profile data
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
            // Horizontal slide-in from the right
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn(animationSpec = tween(150))
            },
            // Push back when returning from Add Task
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
                onAddTaskClick = {
                    navController.navigate("add_task")
                }
            )
        }

        // 3. ADD TASK ROUTE
        composable(
            route = "add_task",
            // Bottom Sheet/Full Screen slide up from the bottom
            enterTransition = {
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(300))
            },
            // Slide back down
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
            }
        ) {
            AddTaskScreen(
                onCancel = { navController.popBackStack() },
                onSave = { title, start, end, priority, repeat ->
                    val newTask = Task(title, start, end, priority, repeat)
                    tasks = tasks + newTask
                    navController.popBackStack()
                }
            )
        }
    }
}