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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ViewModel.DestinationSelectViewModel
import com.example.ViewModel.DestinationSelectViewModelFactory
import com.example.movieappmad24.components.Bars.SimpleTopAppBar
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import com.example.mad_project.navigation.Screen
import com.example.movieappmad24.components.Bars.TopAppBarAction
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

    var startLocation by remember { mutableStateOf(viewModel.startLocation.value) }
    var selectedDestination by remember { mutableStateOf(viewModel.selectedDestination.value) }
    var date1 by remember { mutableStateOf(viewModel.date1.value) }
    var date2 by remember { mutableStateOf(viewModel.date2.value) }
    var directFlight by remember { mutableStateOf(viewModel.directFlight.value) }
    var luggageCount by remember { mutableStateOf(viewModel.luggageCount.value) }

    val darkTheme = isSystemInDarkTheme()
    val textColor = if (darkTheme) Color.White else Color.Black

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val startDate = remember(date1) { if (date1.isNotEmpty()) LocalDate.parse(date1, formatter) else null }
    val endDate = remember(date2) { if (date2.isNotEmpty()) LocalDate.parse(date2, formatter) else null }


    // Swap dates if necessary
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

    var nestedSearch by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val savedDataList = remember { mutableStateListOf<String>() }

    fun createJsonOutofParams(): JSONObject {
        val destinationCoordinates = viewModel.getCoordinates(selectedDestination)
        val jsonObject = JSONObject().apply {
            put("startLocation", startLocation)
            put("destination", selectedDestination)
            put("startDate", if (startDate != null) formatWithZeroPadding(startDate.dayOfMonth, startDate.monthValue, startDate.year) else null)
            put("endDate", if (endDate != null) formatWithZeroPadding(endDate.dayOfMonth, endDate.monthValue, endDate.year) else null)
            put("directFlight", directFlight)
            put("luggageCount", luggageCount)
            put("destinationLat", destinationCoordinates?.lat)
            put("destinationLng", destinationCoordinates?.lng)
        }
        return jsonObject
    }

    fun fillViewWithLoadedData(jsonString: String) {
        val jsonObject = JSONObject(jsonString)
        startLocation = jsonObject.optString("startLocation", "")
        selectedDestination = jsonObject.optString("destination", "")
        date1 = jsonObject.optString("startDate", "")
        date2 = jsonObject.optString("endDate", "")
        directFlight = jsonObject.optBoolean("directFlight", false)
        luggageCount = jsonObject.optInt("luggageCount", 0)
    }

    fun saveDataLocal() {
        val jsonObject = createJsonOutofParams()

        val jsonString = jsonObject.toString()

        val sharedPref = context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
        val entryKey = System.currentTimeMillis().toString() // we use the timestamp as key
        with(sharedPref.edit()) {
            putString(entryKey, jsonString)
            apply()
        }
        savedDataList.add(jsonString)
    }

    fun clearAllData() {
        val sharedPref = context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            clear()
            apply()
        }
        savedDataList.clear()
    }

    fun readAllData() {
        val sharedPref = context.getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE)
        val entriesMap = sharedPref.all
        savedDataList.clear()
        entriesMap.values.forEach { value ->
            if (value is String) {
                savedDataList.add(value)
            }
        }
    }

    fun gatherInfoAndNavigate() {
        val jsonObject = createJsonOutofParams()
        navController.navigate(route = Screen.FlightsScreen.withJsonString(jsonObject.toString()))
    }

    val isFormComplete = remember(startLocation, selectedDestination, date1, date2) {
        startLocation.isNotEmpty() && selectedDestination.isNotEmpty() && date1.isNotEmpty() && date2.isNotEmpty()
    }

    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Select Destination",
                navController = navController,
                additionalActions = listOf(
                    TopAppBarAction(
                        icon = Icons.Default.Star,
                        onClick = { saveDataLocal() },
                        contentDescription = "Save"
                    ),
                    TopAppBarAction(
                        icon = Icons.Default.ExitToApp,
                        onClick = {
                            showDialog = showDialog.not()
                            readAllData()
                        },
                        contentDescription = "Load"
                    ))
            )
        }
    ) { innerPadding ->

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Saved Data") },
                text = {
                    Column {
                        savedDataList.forEachIndexed { index, jsonString ->
                            val jsonObject = JSONObject(jsonString)
                            Text("${jsonObject.optString("startLocation", "")} " +
                                    "- ${jsonObject.optString("destination", "")} - " +
                                    "${jsonObject.optString("startDate", "")}" ,
                                modifier = Modifier.clickable {
                                    fillViewWithLoadedData(jsonString)
                                    viewModel.onSelectDestination(selectedDestination, false)
                                    viewModel.onSelectDestination(startLocation, true)
                                    viewModel.setStartDate(date1)
                                    viewModel.setEndDate(date2)
                                    viewModel.directFlight.value = directFlight
                                    viewModel.luggageCount.value = luggageCount
                                    showDialog = false
                                })
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Close".uppercase())
                    }
                },
                dismissButton = {
                    TextButton(onClick = { clearAllData() }) {
                        Text("Clear All".uppercase())
                    }
                }
            )
        }
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
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
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
                                        Text("Type here", color = textColor.copy(alpha = 0.5f))
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
                                    color = textColor,
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
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
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
                                        Text("Type here", color = textColor.copy(alpha = 0.5f))
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
                                    color = textColor,
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

                // Geographic Search button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Geographic Search")
                    Spacer(modifier = Modifier.width(10.dp))
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
                            date1 = formatWithZeroPadding(day, month, year)
                            viewModel.setStartDate(date1)
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
                            date2 = formatWithZeroPadding(day, month, year)
                            viewModel.setEndDate(date2)
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
            }

            // Navigate to next screen
            if (isFormComplete) {
                Button(
                    onClick = {
                        gatherInfoAndNavigate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Confirm and Proceed")
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Confirm and Proceed",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

fun showDatePickerDialog(context: Context, onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = Calendar.getInstance().get(Calendar.MONTH)
    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        onDateSelected(selectedYear, selectedMonth + 1, selectedDay)
    }, year, month, day).show()
}

fun formatWithZeroPadding(day: Int, month: Int, year: Int): String {
    val dayString = if (day < 10) "0$day" else day.toString()
    val monthString = if (month < 10) "0$month" else month.toString()
    return "$dayString.$monthString.$year"
}
