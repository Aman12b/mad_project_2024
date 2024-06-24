package com.example.mad_project.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ViewModel.DestinationSelectViewModel
import com.example.ViewModel.DestinationSelectViewModelFactory
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DestinationSelectScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: DestinationSelectViewModel = viewModel(
        factory = DestinationSelectViewModelFactory(context)
    )

    var startLocation by remember { mutableStateOf("") }
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
            put("startLocation", startLocation)
            put("destination", selectedDestination)
            put("startDate", finalStartDate?.format(formatter) ?: "")
            put("endDate", finalEndDate?.format(formatter) ?: "")
            put("directFlight", directFlight)
            put("luggageCount", luggageCount)
        }
        val jsonString = jsonObject.toString()
        navController.navigate("next_screen_route/$jsonString")
    }

    var nestedSearch by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Start Location")
                        BasicTextField(
                            value = startLocation,
                            onValueChange = {
                                startLocation = it
                                viewModel.onOriginSearchQueryChange(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(horizontal = 8.dp, vertical = 12.dp)
                                ) {
                                    if (startLocation.isEmpty()) {
                                        Text("Search for a start location")
                                    }
                                    innerTextField()
                                }
                            }
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            val filteredLocations = viewModel.getFilteredLocations(nestedSearch, true)

                            items(filteredLocations) { location ->
                                Text(
                                    text = location,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            if (nestedSearch) {
                                                when {
                                                    viewModel.selectedOriginContinent.value == null -> viewModel.selectedOriginContinent.value = location
                                                    viewModel.selectedOriginCountry.value == null -> viewModel.selectedOriginCountry.value = location
                                                    else -> startLocation = location
                                                }
                                            } else {
                                                startLocation = location
                                            }
                                        }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Destination")
                        BasicTextField(
                            value = selectedDestination,
                            onValueChange = {
                                selectedDestination = it
                                viewModel.onDestinationSearchQueryChange(it)
                            },
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

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            val filteredDestinations = viewModel.getFilteredLocations(nestedSearch, false)

                            items(filteredDestinations) { destination ->
                                Text(
                                    text = destination,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            if (nestedSearch) {
                                                when {
                                                    viewModel.selectedDestinationContinent.value == null -> viewModel.selectedDestinationContinent.value = destination
                                                    viewModel.selectedDestinationCountry.value == null -> viewModel.selectedDestinationCountry.value = destination
                                                    else -> selectedDestination = destination
                                                }
                                            } else {
                                                selectedDestination = destination
                                            }
                                        }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Enable Nested Search")
                    Switch(
                        checked = nestedSearch,
                        onCheckedChange = {
                            nestedSearch = it
                            viewModel.selectedOriginContinent.value = null
                            viewModel.selectedOriginCountry.value = null
                            viewModel.selectedDestinationContinent.value = null
                            viewModel.selectedDestinationCountry.value = null
                        }
                    )
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (finalStartDate != null || finalEndDate != null) {
                        if (finalStartDate != null) {
                            Text("Start: ${finalStartDate.format(formatter)}", modifier = Modifier.padding(vertical = 4.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        if (finalEndDate != null) {
                            Text("End: ${finalEndDate.format(formatter)}", modifier = Modifier.padding(vertical = 4.dp))
                        }

                    }
                }
                if (finalStartDate != null || finalEndDate != null) {
                    if (tripDuration.isNotEmpty()) {
                        Text("Trip Duration: $tripDuration days", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                        Text("Luggage: $luggageCount")
                        IconButton(onClick = { if (luggageCount > 0) luggageCount -= 1 }) {
                            Icon(imageVector = Icons.Filled.Remove, contentDescription = "Remove Luggage")
                        }
                        IconButton(onClick = { luggageCount += 1 }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Luggage")
                        }
                    }
                }
            }

            Button(
                onClick = {
                    gatherInfoAndNavigate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Confirm and Proceed")
            }
        }
    }
}

fun showDatePickerDialog(context: Context, onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected(selectedYear, selectedMonth + 1, selectedDay)
    }, year, month, day).show()
}

