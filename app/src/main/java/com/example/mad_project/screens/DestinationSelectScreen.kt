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
import java.util.Calendar

fun showDatePickerDialog(context: Context, onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected(selectedYear, selectedMonth + 1, selectedDay)
    }, year, month, day).show()
}

@Composable
fun DestinationSelectScreen(
    navController: NavController,
    viewModel: DestinationSelectViewModel
) {
    val context = LocalContext.current

    var selectedDestination by remember { mutableStateOf("") }
    var date1 by remember { mutableStateOf("") }
    var date2 by remember { mutableStateOf("") }

    val (startDate, endDate) = remember(date1, date2) {
        if (date1.isNotEmpty() && date2.isNotEmpty()) {
            val format = java.text.SimpleDateFormat("dd.mm.yyyy", java.util.Locale.getDefault())
            val d1 = format.parse(date1)
            val d2 = format.parse(date2)
            if (d1.before(d2)) {
                date1 to date2
            } else {
                date2 to date1
            }
        } else if (date1.isNotEmpty()) {
            date1 to ""
        } else {
            "" to ""
        }
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

            if (startDate.isNotEmpty() || endDate.isNotEmpty()) {
                Text("Start Date: $startDate", modifier = Modifier.padding(vertical = 4.dp))
                if (endDate.isNotEmpty()) {
                    Text("End Date: $endDate", modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
