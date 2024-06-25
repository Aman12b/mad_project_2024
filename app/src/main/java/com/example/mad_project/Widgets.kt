package com.example.mad_project


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mad_project.navigation.Screen
import com.google.gson.Gson
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ViewModel.DetailScreenViewModel
import com.example.ViewModel.FlightsViewModel
import com.example.ViewModel.SightsViewModel
import com.example.mad_project.classes.Feature

val gson = Gson()

@Composable
fun FeatureList(
    modifier: Modifier,
    navController: NavController,
    viewModel: SightsViewModel
) {
    LazyColumn(modifier = modifier) {
        items(viewModel.features) { feature ->
            FeatureRow(
                feature = feature,
                onItemClick = { featureitem ->
                    navController.navigate(route = Screen.DetailScreen.withInfo(gson.toJson(featureitem)))
                }
            )
        }
    }
}

@Composable
fun FeatureRow(
    modifier: Modifier = Modifier,
    feature: Feature,
    onItemClick: (Feature) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .clickable {
                onItemClick(feature)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                // Name and Location
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = feature.properties?.name ?: "Unknown",
                            color = Color.Black,
                            fontSize = 22.sp
                        )
                        Text(
                            text = feature.properties?.kinds?.split(',')?.getOrNull(0)?.replace("_", " ") ?: "",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val context = LocalContext.current
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = feature.properties?.rate.toString(),
                            color = Color.Black,
                            fontSize = 28.sp,
                            modifier = Modifier.width(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                onItemClickAroow(feature,context)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.Blue,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceDetails(modifier: Modifier, feature: Feature) {

    Column (modifier = modifier.padding(horizontal = 20.dp)) {
        Text(text = "Name: ${feature.properties?.name ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Kind: ${feature.properties?.kinds?.replace("_", " ") ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Rate: ${feature.properties?.rate ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "type: ${feature.type ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)

        Divider(modifier = Modifier.padding(5.dp))
        Text(text = "Description: ${feature.properties?.description ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
        }
}

fun openGoogleMaps(latitude: Double, longitude: Double, context: Context) {
    val mapUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude%2C$longitude")
    val intent = Intent(Intent.ACTION_VIEW, mapUri)
    context.startActivity(intent)
}

fun onItemClickAroow(feature: Feature, context: android.content.Context) {
    val url = "https://www.wikidata.org/wiki/${feature.properties?.wikidata}"
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}

@Composable
fun HorizontalScrollableImageView(viewModel: DetailScreenViewModel, modifier: Modifier) {

    val c = viewModel.feature?.properties?.images?.count()
    LazyRow(modifier = modifier) {
        items(viewModel.feature?.properties?.images ?: listOf()) { image ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp)
                    .size(240.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Movie poster",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun FlightList(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FlightsViewModel
) {
    val flights = viewModel.flights
    LazyColumn(modifier = modifier) {
        items(flights.size) { index ->
            val flight = flights[index]
            Text(text = "${flight.airline} - ${flight.departureAt} - ${flight.price}")
        }
    }
}