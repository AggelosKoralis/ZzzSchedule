package com.example.zzzschedule.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
private val SuccessGreen = Color(0xFF81C784) // Added success color for checkmarks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPageScreen(
    onContinue: (
        username: String,
        age: String,
        occupation: String,
        sleepQuality: Int
    ) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("Select") }
    var showOccupationDialog by remember { mutableStateOf(false) }
    var sleepQuality by remember { mutableFloatStateOf(65f) }
    var loading by remember { mutableStateOf(false) }

    var showErrors by remember { mutableStateOf(false) }

    val occupations = listOf(
        "Student",
        "Working Student",
        "Working Full Time",
        "Working Part Time",
        "Unemployed",
        "Select"
    )

    val isUsernameValid = username.isNotBlank() && username.length <= 10
    val isAgeValid = age.toIntOrNull()?.let { it in 13..99 } ?: false
    val isOccupationValid = occupation in occupations && occupation != "Select"
    val isFormValid = isUsernameValid && isAgeValid && isOccupationValid

    Scaffold(
        containerColor = Background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(padding)
                .imePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
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

            Spacer(modifier = Modifier.height(6.6.dp))

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
                    onValueChange = {
                        if (it.length <= 10 && !it.contains(" ")) username = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your username") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    isError = showErrors && !isUsernameValid,
                    trailingIcon = {
                        if (isUsernameValid) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid username",
                                tint = SuccessGreen
                            )
                        }
                    },
                    supportingText = {
                        if (showErrors && !isUsernameValid) {
                            Text("Username cannot be empty and max 10 chars")
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
                        if (input.all { it.isDigit() } && input.length <= 2) {
                            age = input
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your age") },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    isError = showErrors && !isAgeValid,
                    trailingIcon = {
                        if (isAgeValid) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid age",
                                tint = SuccessGreen
                            )
                        }
                    },
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
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // OCCUPATION
                Text(
                    text = "What is your occupation? *",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = if (occupation == "Select") "" else occupation,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select your occupation") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        isError = showErrors && !isOccupationValid,
                        trailingIcon = {
                            if (isOccupationValid) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Valid occupation",
                                    tint = SuccessGreen
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Open dropdown",
                                    tint = TextSecondary
                                )
                            }
                        },
                        supportingText = {
                            if (showErrors && !isOccupationValid) {
                                Text("Please select a valid occupation")
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

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                focusManager.clearFocus()
                                showOccupationDialog = true
                            }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // SLEEP QUALITY SLIDER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Average sleep quality:",
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Slider(
                    value = sleepQuality,
                    onValueChange = { sleepQuality = it },
                    valueRange = 1f..100f,
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
                    Text("Terrible", color = Color(0xFF807E8F))
                    Text("Great", color = Color(0xFF807E8F))
                }

                Spacer(modifier = Modifier.height(26.dp))

                // LOOKS GOOD BUTTON
                Button(
                    onClick = {
                        if (isFormValid) {
                            loading = true
                            onContinue(username, age, occupation, sleepQuality.toInt())
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

    if (showOccupationDialog) {
        SelectionDialog(
            title = "Select Occupation",
            options = occupations,
            selected = occupation,
            onDismiss = { showOccupationDialog = false },
            onSelected = {
                occupation = it
                showOccupationDialog = false
            }
        )
    }
}

@Composable
private fun SelectionDialog(
    title: String,
    options: List<String>,
    selected: String,
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceHigh,
        title = {
            Text(
                text = title,
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.selectableGroup()
            ) {
                options.forEach { option ->
                    if (option != "Select") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (option == selected),
                                    onClick = {
                                        onSelected(option)
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (option == selected),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Primary,
                                    unselectedColor = TextSecondary
                                )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = option,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {}
    )
}