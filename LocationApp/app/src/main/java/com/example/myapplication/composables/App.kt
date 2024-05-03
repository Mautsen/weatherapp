package com.example.myapplication.composables

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.MainViewModel
import androidx.compose.runtime.livedata.observeAsState

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

    val locationState = viewModel.location.observeAsState()
    val weatherState = viewModel.weather.observeAsState()

    val location = locationState.value
    val weather = weatherState.value

    val latitude = location?.latitude ?: "Not available"
    val longitude = location?.longitude ?: "Not available"


    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Location: $latitude, $longitude")
        Text(text = "Weather: ${weather?.current ?: "Not available"}°C")    }
}


