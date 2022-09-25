package com.masoudjafari.batterylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SwitchCompat
import com.masoudjafari.batterylife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var switchCounter: SwitchCompat
    private lateinit var binding: ActivityMainBinding
    private lateinit var functions: Functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initiate()
    }

    private fun initiate() {
        switchCounter = binding.switchCounter
        functions = Functions(this)

        binding.tvLastCount.text = buildString {
            append(" آخرین شمارش : ")
            append(functions.getSavedChargeCount())
        }

        if (functions.isChargeCountEnable())
            switchCounter.isChecked = true

        switchCounter.setOnCheckedChangeListener { switch, isChecked ->
            if (isChecked)
                functions.enableCountCharge()
            else
                functions.disableCountCharge()
        }
    }
}