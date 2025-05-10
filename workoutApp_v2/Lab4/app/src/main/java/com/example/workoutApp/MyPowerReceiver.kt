package com.example.workoutApp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyPowerReceiver:BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0, "Power Connected!", Toast.LENGTH_SHORT).show()

        //log success
        Log.d("broadcast","Power Connected")
    }
}