package com.masoudjafari.batterylife

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver : BroadcastReceiver() {
    private lateinit var functions: Functions

    override fun onReceive(context: Context, intent: Intent) {
        functions = Functions(context)
        when (intent.action.toString()) {
            Intent.ACTION_POWER_CONNECTED -> {
                if (functions.isChargeCountEnable()) {
                    functions.saveNewChargeCount()
                    functions.startService()
                }
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                if (functions.isChargeCountEnable())
                    functions.enableCountCharge()
            }
        }
    }
}