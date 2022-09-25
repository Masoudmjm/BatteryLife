package com.masoudjafari.batterylife

import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

class Functions(private val context: Context) {
    private val editor = context.getSharedPreferences(ConstValues.prefsName, Service.MODE_PRIVATE).edit()
    private val prefs: SharedPreferences = context.getSharedPreferences(ConstValues.prefsName, AppCompatActivity.MODE_PRIVATE)
    private var broadcastReceiver = MyBroadcastReceiver()

    fun enableCountCharge() {
        enableBroadcastReceiver()
        startService()
        setChargeCountStatus(true)
    }

    fun disableCountCharge() {
        disableBroadcastReceiver()
        stopService()
        resetChargeCount()
        //in case of service is running
        stopService()
        setChargeCountStatus(false)
    }

    private fun setChargeCountStatus(enable: Boolean) {
        editor.putBoolean(ConstValues.countPermissionName, enable)
        editor.apply()
    }

    fun isChargeCountEnable(): Boolean {
        return prefs.getBoolean(ConstValues.countPermissionName, false)
    }

    private fun resetChargeCount() {
        editor.putInt(ConstValues.chargeCountName, 0)
        editor.apply()
    }

    fun getSavedChargeCount(): Int {
        return prefs.getInt(ConstValues.chargeCountName, 0)
    }

    fun saveNewChargeCount() {
        var count = getSavedChargeCount()
        count += 1
        editor.putInt(ConstValues.chargeCountName, count)
        editor.apply()
    }

    private fun enableBroadcastReceiver() {
        val receiver = ComponentName(context, MyBroadcastReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun disableBroadcastReceiver() {
        val receiver = ComponentName(context, MyBroadcastReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun registerPowerReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        context.registerReceiver(broadcastReceiver, filter)
    }

    fun startService() {
        val intent = Intent(context, ChargeCountService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intent)
        else
            context.startService(intent)
    }

    fun stopService() {
        val intent = Intent(context, ChargeCountService::class.java)
        context.stopService(intent)
    }
}