package com.example.zzzschedule.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF141317)
private val SurfaceLow = Color(0xFF1C1B1F)
private val SurfaceHigh = Color(0xFF2B292D)
private val Primary = Color(0xFFE9DDFF)
private val Secondary = Color(0xFFD0BCFF)
private val Outline = Color(0xFF49454F)
private val TextPrimary = Color(0xFFE5E1E7)
private val TextSecondary = Color(0xFFCAC4D0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPageScreen(
    onContinue: (
        username: String,
        age: String,
        occupation: String,
        sleepHours: Int
    ) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("Select") }
    var expanded by remember { mutableStateOf(false) }
    var sleepHours by remember { mutableFloatStateOf(7f) }
    var loading by remember { mutableStateOf(false) }

    // Track whether the user has attempted to submit (triggers error visibility)
    var showErrors by remember { mutableStateOf(false) }

    val occupations = listOf(
        "Student",
        "Working Student",
        "Working Full Time",
        "Working Part Time",
        "Unemployed",
        "Select"
    )

// Validation Rules
    val isUsernameValid = username.isNotBlank()
    val isAgeValid = age.toIntOrNull()?.let { it in 13..99 } ?: false // Strictly 13 to 99
    val isOccupationValid = occupation != "Select" && occupation.isNotBlank()
    val isFormValid = isUsernameValid && isAgeValid && isOccupationValid

    Scaffold(
        containerColor = Background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            // TITLE
            Text(
                text = "Let's adjust your rhythm.",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your daily cycle is unique. Tell us a bit about yourself.",
                color = TextSecondary,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // MAIN CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF25233D).copy(alpha = 0.4f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(22.dp)
            ) {

                // USERNAME
                Text(
                    text = "What should we call you? *",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your username") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    isError = showErrors && !isUsernameValid,
                    supportingText = {
                        if (showErrors && !isUsernameValid) {
                            Text("Username cannot be empty")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Outline,
                        focusedContainerColor = SurfaceLow,
                        unfocusedContainerColor = SurfaceLow,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        errorContainerColor = SurfaceLow
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // AGE
                Text(
                    text = "How old are you? *",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { input ->
                        // Limit text length to 2 digits max to prevent typing numbers like 1000
                        if (input.all { it.isDigit() } && input.length <= 2) {
                            age = input
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your age") },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                    },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    isError = showErrors && !isAgeValid,
                    supportingText = {
                        if (showErrors && !isAgeValid) {
                            Text("Age must be a number between 13 and 99")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Outline,
                        focusedContainerColor = SurfaceLow,
                        unfocusedContainerColor = SurfaceLow,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        errorContainerColor = SurfaceLow
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // OCCUPATION
                Text(
                    text = "What is your occupation? *",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = if (occupation == "Select") "" else occupation,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select your occupation") },
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        isError = showErrors && !isOccupationValid,
                        supportingText = {
                            if (showErrors && !isOccupationValid) {
                                Text("Please select an occupation")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Outline,
                            focusedContainerColor = SurfaceLow,
                            unfocusedContainerColor = SurfaceLow,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            errorContainerColor = SurfaceLow
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = SurfaceHigh
                    ) {
                        occupations.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item, color = TextPrimary) },
                                onClick = {
                                    occupation = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // SLEEP SLIDER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Average night sleep:",
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = sleepHours.toInt().toString(),
                            color = Secondary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "hours", color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    valueRange = 4f..12f,
                    steps = 7,
                    colors = SliderDefaults.colors(
                        thumbColor = Secondary,
                        activeTrackColor = Secondary,
                        inactiveTrackColor = Outline
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("4h", color = Outline)
                    Text("8h", color = Outline)
                    Text("12h", color = Outline)
                }

                Spacer(modifier = Modifier.height(26.dp))

                // LOOKS GOOD BUTTON
                Button(
                    onClick = {
                        if (isFormValid) {
                            loading = true
                            onContinue(username, age, occupation, sleepHours.toInt())
                        } else {
                            showErrors = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color(0xFF22005C)
                    )
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF22005C)
                        )
                    } else {
                        Text(
                            text = "Looks Good",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}