package com.safeword.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationHelper(context: Context) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Pair<Double, Double>? = suspendCancellableCoroutine { cont ->
        fusedClient.lastLocation
            .addOnSuccessListener { location ->
                if (!cont.isCompleted) {
                    cont.resume(location?.let { it.latitude to it.longitude })
                }
            }
            .addOnFailureListener { error ->
                if (!cont.isCompleted) cont.resume(null)
            }
    }
}
