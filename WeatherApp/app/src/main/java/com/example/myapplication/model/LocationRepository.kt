package com.example.myapplication.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

/**
 * A repository class for handling location updates.
 *
 * @property context The context used to access system services.
 */
class LocationRepository(private val context: Context) {

    /**
     * Starts location updates if the required permissions are granted.
     *
     * @param callback A callback function to handle the location updates.
     */
    fun startLocationUpdates(callback: (Location?) -> Unit) {
        // Check if location permissions are granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are granted, proceed with requesting location updates
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = LocationRequest.Builder(10000L)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setIntervalMillis(5000L)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.i("LocationRepository", "Received location result: $locationResult")
                    callback(locationResult.lastLocation)
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper() // callback is done in the main thread
            )
        } else {
            Log.d("Location", "Not granted!")
        }
    }
}
