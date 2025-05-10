package com.example.workoutApp

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.workoutApp.ui.WorkoutApp
import com.example.workoutApp.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    //create an instance of BroadcastReceiver
    private val br: BroadcastReceiver = MyPowerReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkoutApp()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        //create an instance of IntentFilter
        val filter = IntentFilter("android.intent.action.ACTION_POWER_CONNECTED")
        //set the receiver exported flag if your app is listening to system broadcast
        val listenToBroadcastsFromOtherApps = true
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }
        //register the receiver in the Activity context
        ContextCompat.registerReceiver(this, br, filter, receiverFlags)

    }
    override fun onPause() {
        super.onPause()
        //unregister the broadcast receiver to avoid leaking the receiver outside the Activity context
        unregisterReceiver(br)
    }
}