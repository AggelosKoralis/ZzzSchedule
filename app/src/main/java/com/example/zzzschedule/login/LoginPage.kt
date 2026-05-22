package com.example.zzzschedule.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF141317)
private val Surface = Color(0xFF201F23)
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
        sleepHours: Float
    ) -> Unit
) {

    var username by remember {mutableStateOf("")}

    var age by remember { mutableStateOf("") }

    var occupation by remember {
        mutableStateOf("Select")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var sleepHours by remember {
        mutableFloatStateOf(7f)
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val occupations = listOf(
        "Student",
        "Working Student",
        "Working Full Time",
        "Working Part Time",
        "Unemployed",
        "Select"
    )

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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your daily cycle is unique. Tell us a bit about yourself.",
                color = TextSecondary,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // MAIN CARD

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Color(0xFF25233D).copy(alpha = 0.4f)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(24.dp)
            ) {

                // USERNAME

                Text(
                    text = "What should we call you?",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },

                    modifier = Modifier.fillMaxWidth(),

                    placeholder = {
                        Text("Enter your username")
                    },

                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null
                        )
                    },

                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = 14.dp,
                        bottomEnd = 14.dp
                    ),

                    singleLine = true,

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Outline,
                        focusedContainerColor = SurfaceLow,
                        unfocusedContainerColor = SurfaceLow,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                // AGE

                Text(
                    text = "How old are you?",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },

                    modifier = Modifier.fillMaxWidth(),

                    placeholder = {
                        Text("Enter your age")
                    },

                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null
                        )
                    },

                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = 14.dp,
                        bottomEnd = 14.dp
                    ),

                    singleLine = true,

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Outline,
                        focusedContainerColor = SurfaceLow,
                        unfocusedContainerColor = SurfaceLow,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                // OCCUPATION

                Text(
                    text = "What is your occupation?",
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value = occupation,
                        onValueChange = {},

                        readOnly = true,

                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),

                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = 14.dp,
                            bottomEnd = 14.dp
                        ),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Outline,
                            focusedContainerColor = SurfaceLow,
                            unfocusedContainerColor = SurfaceLow,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },

                        containerColor = SurfaceHigh
                    ) {

                        occupations.forEach { item ->

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        item,
                                        color = TextPrimary
                                    )
                                },

                                onClick = {

                                    occupation = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // SLEEP SLIDER

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {

                    Text(
                        text = "Average night sleep:",
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {

                        Text(
                            text = sleepHours.toInt().toString(),
                            color = Secondary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "hours",
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sleepHours,

                    onValueChange = {
                        sleepHours = it
                    },

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

                Spacer(modifier = Modifier.height(24.dp))

                // LOOKS GOOD BUTTON

                Button(
                    onClick = {
                        loading = true
                        onContinue(username, age, occupation, sleepHours)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Secondary,
                        contentColor = Color(0xFF22005C)
                    )
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = Color.Black
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