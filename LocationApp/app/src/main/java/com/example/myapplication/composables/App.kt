package com.example.myapplication.composables

import DailyForecastComponent
import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.theme.valeraRound


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel: MainViewModel = viewModel()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // Check if all requested permissions have been granted
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                viewModel.startLocationUpdates()
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    var isDataLoading by remember { mutableStateOf(true) }
    val locationState = viewModel.location.observeAsState()
    val weatherState = viewModel.weather.observeAsState()
    val dailyForecastState = viewModel.dailyForecast.observeAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val location = locationState.value
    val weather = weatherState.value
    val dailyForecast = dailyForecastState.value

    val latitude = location?.latitude ?: "Not available"
    val longitude = location?.longitude ?: "Not available"
    val temperature = weather?.current?.temperature ?: "Not available"
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

    // isDataLoading is true if location or weather are null
    // It's false if they are not null so the data has loaded
    LaunchedEffect(location, weather) {
        isDataLoading = location == null || weather == null || dailyForecast == null
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Location: $latitude, $longitude")

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            DockedSearchBar(
                modifier = Modifier
                    .padding(top = 8.dp),
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
                    items(2) { idx ->
                        val resultText = "City $idx"
                        ListItem(
                            modifier = Modifier.clickable {
                                text = resultText
                                active = false
                            },
                            headlineContent = {
                                Text(
                                    text = resultText,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            supportingContent = {
                                Text("")
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Rounded.LocationOn,
                                    contentDescription = null
                                )
                            },
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isDataLoading) {
                CircularProgressIndicator() // This gets called if isDataLoading is true
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = "${temperature}°C",
                        style = valeraRound
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "My Image",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    DailyForecastComponent(dailyForecast)
                }

            }
        }
    }
}



