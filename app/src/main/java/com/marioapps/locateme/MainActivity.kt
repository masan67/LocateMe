package com.marioapps.locateme

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.marioapps.locateme.MainActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.buttonStartLocationUpdates).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                            applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE_LOCATION_PERMISSION
                )
            } else {
                startLocationService()
            }
        }

        findViewById<View>(R.id.buttonStopLocationUpdates).setOnClickListener { stopLocationService() }

        findViewById<View>(R.id.buttonFSTest).setOnClickListener {

            Toast.makeText(applicationContext, "chido", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.isNotEmpty()) {
            startLocationService()
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    private val isLocationServiceRunning: Boolean
        get() {
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (activityManager != null) {
                for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                    if (LocationService::class.java.name == service.service.className) {
                        if (service.foreground) {
                            return true
                        }
                    }
                }
                return false
            }
            return false
        }

    private fun startLocationService() {
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.action = Constants.ACTION_START_LOCATION_SERVICE
        startService(intent)
        Toast.makeText(this, "Location service started...", Toast.LENGTH_SHORT).show()
    }

    private fun stopLocationService() {
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.action = Constants.ACTION_STOP_LOCATION_SERVICE
        startService(intent)
        Toast.makeText(this, "Location service stopped...", Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val REQUEST_CODE_LOCATION_PERMISSION = 1
    }
}