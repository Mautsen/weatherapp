package com.example.myapplication.composables

import DailyForecastComponent
import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.MainViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.theme.antonRegular
import com.example.myapplication.ui.theme.gradientBackground
import com.example.myapplication.ui.theme.valeraRound

/**
 * The main composable function for the application. This function sets up the UI and handles
 * the necessary permissions and state management.
 */
@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: MainViewModel = viewModel()

    // Launch a permission request for location access
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                viewModel.startLocationUpdates()
            }
        }
    )

    // Request location permissions when the composable is first launched
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    var isDataLoading by remember { mutableStateOf(true) }
    val locationState = viewModel.location.observeAsState()
    val weatherState = viewModel.weather.observeAsState()
    val isCelsius = viewModel.isCelsius.observeAsState(true)
    val dailyForecastState = viewModel.dailyForecast.observeAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val location = locationState.value
    val weather = weatherState.value
    val dailyForecast = dailyForecastState.value

    // Prepare data for display
    val latitude = location?.latitude ?: "Not available"
    val longitude = location?.longitude ?: "Not available"
    val temperature = weather?.current?.temperature ?: 0.0
    val displayTemperature = if (isCelsius.value) {
        "${temperature}°C"
    } else {
        val fahrenheitTemp = temperature * 9.0 / 5.0 + 32.0
        String.format("%.1f°F", fahrenheitTemp)
    }
    val weatherCode = weather?.current?.weatherCode ?: -1

    val imageResId = when (weatherCode) {
        0 -> R.drawable.sunny
        1, 2 -> R.drawable.partly_cloudy
        3 -> R.drawable.cloudy
        51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> R.drawable.rainy
        71, 73, 75, 77, 85, 86 -> R.drawable.snowy
        95, 96, 99 -> R.drawable.stormy
        45, 48 -> R.drawable.foggy
        else -> R.drawable.default_image
    }

    // Update loading state based on location and weather data
    LaunchedEffect(location, weather) {
        isDataLoading = location == null || weather == null || dailyForecast == null
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Search location") },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(listOf("Valkeakoski", "Kerava", "GPS")) { city ->
                            ListItem(
                                modifier = Modifier.clickable {
                                    text = city
                                    active = false
                                    when (city.lowercase()) {
                                        "valkeakoski" -> viewModel.setCustomLocation(61.2628, 24.0316)
                                        "kerava" -> viewModel.setCustomLocation(60.4039, 25.1015)
                                        "gps" -> viewModel.useCurrentLocation()
                                    }
                                },
                                headlineContent = {
                                    Text(
                                        text = city,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                },
                                supportingContent = {
                                    Text("Select $city")
                                },
                                leadingContent = {
                                    Icon(Icons.Rounded.LocationOn, contentDescription = null)
                                },
                            )
                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = { viewModel.toggleTemperatureUnit() },
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .gradientBackground()
                            .size(width = 54.dp, height = 48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = if (isCelsius.value) "°F" else "°C",
                            color = Color(0xFF262547),
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 20.sp),
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isDataLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = displayTemperature,
                        style = valeraRound
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Weather Image",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    DailyForecastComponent(dailyForecast)
                }
            }
        }
    }
}
