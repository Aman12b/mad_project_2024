package com.example.mad_project.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ViewModel.DestinationSelectViewModel
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Remove
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import org.json.JSONObject

fun showDatePickerDialog(context: Context, onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected(selectedYear, selectedMonth + 1, selectedDay)
    }, year, month, day).show()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DestinationSelectScreen(
    navController: NavController,
    viewModel: DestinationSelectViewModel
) {
    val context = LocalContext.current

    var selectedDestination by remember { mutableStateOf("") }
    var date1 by remember { mutableStateOf("") }
    var date2 by remember { mutableStateOf("") }
    var directFlight by remember { mutableStateOf(false) }
    var luggageCount by remember { mutableStateOf(0) }

    val formatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    val startDate = remember(date1) { if (date1.isNotEmpty()) LocalDate.parse(date1, formatter) else null }
    val endDate = remember(date2) { if (date2.isNotEmpty()) LocalDate.parse(date2, formatter) else null }

    // Determine the correct start and end dates
    val (finalStartDate, finalEndDate) = remember(startDate, endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isBefore(endDate)) {
                startDate to endDate
            } else {
                endDate to startDate
            }
        } else if (startDate != null) {
            startDate to null
        } else {
            null to null
        }
    }

    val tripDuration = remember(finalStartDate, finalEndDate) {
        if (finalStartDate != null && finalEndDate != null) {
            ChronoUnit.DAYS.between(finalStartDate, finalEndDate).toString()
        } else {
            ""
        }
    }

    fun gatherInfoAndNavigate() {
        val jsonObject = JSONObject().apply {
            put("destination", selectedDestination)
            put("startDate", finalStartDate?.format(formatter) ?: "")
            put("endDate", finalEndDate?.format(formatter) ?: "")
            put("directFlight", directFlight)
            put("luggageCount", luggageCount)
        }
        val jsonString = jsonObject.toString()
        navController.navigate("next_screen_route/$jsonString")
    }

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Select Destination", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicTextField(
                    value = selectedDestination,
                    onValueChange = { selectedDestination = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 8.dp, vertical = 12.dp)
                        ) {
                            if (selectedDestination.isEmpty()) {
                                Text("Search for a destination")
                            }
                            innerTextField()
                        }
                    }
                )

                if (selectedDestination.isEmpty()) {
                    viewModel.destinations.forEach { destination ->
                        Text(
                            text = destination,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedDestination = destination
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date picker buttons
                Button(
                    onClick = {
                        showDatePickerDialog(context) { year, month, day ->
                            date1 = "$day.$month.$year"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Start Date")
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select Start Date",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showDatePickerDialog(context) { year, month, day ->
                            date2 = "$day.$month.$year"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select End Date")
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select End Date",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (finalStartDate != null || finalEndDate != null) {
                    if (finalStartDate != null) {
                        Text("Start Date: ${finalStartDate.format(formatter)}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                    if (finalEndDate != null) {
                        Text("End Date: ${finalEndDate.format(formatter)}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                    if (tripDuration.isNotEmpty()) {
                        Text("Trip Duration: $tripDuration days", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Direct Flight Switch and Luggage Counter
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Direct Flight")
                        Switch(
                            checked = directFlight,
                            onCheckedChange = { directFlight = it },
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Luggage:")
                        IconButton(onClick = { if (luggageCount > 0) luggageCount -= 1 }) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Remove Luggage")
                        }
                        Text(text = luggageCount.toString())
                        IconButton(onClick = { luggageCount += 1 }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Luggage")
                        }
                    }
                }
            }

            // Button to initiate the next step
            Button(
                onClick = {
                    gatherInfoAndNavigate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Check Flights" + Icons.Default.ArrowForward)
            }
        }
    }
}
