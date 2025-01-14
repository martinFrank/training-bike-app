package com.github.martinfrank.sport.trainingbikeapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.bluefalcon.BlueFalcon
import dev.bluefalcon.BlueFalconDelegate
import dev.bluefalcon.BluetoothCharacteristic
import dev.bluefalcon.BluetoothPeripheral
import dev.bluefalcon.BluetoothService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class JaSportC3Activity : BlueFalconDelegate, AppCompatActivity() {

    private lateinit var bikeText: TextView
    private lateinit var blueFalcon: BlueFalcon
    private lateinit var peripheral: BluetoothPeripheral
    private lateinit var ftmsService: BluetoothService
    private var sensorText = "initializing"

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var uiRefreshRunnable: Runnable


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    @OptIn(ExperimentalUuidApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_jasportc3)
        bikeText = findViewById(R.id.bikeText)

        // Prevent the screen from timing out
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        startUiRefreshTread(100)

        val bundle = intent.extras
        if (bundle != null) {
            peripheral = bundle.getParcelable(MainActivity.PERIPHERAL_EXTRA)!!
            blueFalcon = BlueFalcon(null, this.application, true)
            blueFalcon.delegates.add(this)
            blueFalcon.connect(peripheral)
            blueFalcon.discoverServices(peripheral, mutableListOf())
        }

    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun didCharacteristcValueChanged(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristic: BluetoothCharacteristic
    ) {
        super.didCharacteristcValueChanged(bluetoothPeripheral, bluetoothCharacteristic)
        var charText = bluetoothCharacteristic.value?.toHexString()
        extractData(charText.toString())
    }

    private fun extractData(dataString: String) {
        //d40900000000000000000000000000ffffff1200
        //d409c9054e0047000026000a000100ffffff2c00
        //d409a3054c002d000025000b000000ffffff1b00
        //0123456789012345678901234567890123456789
        //0000000000111111111122222222223333333333
//        sensorText = string
        if (dataString.length == 40) {
            val distance = extractSpecificData(dataString, 36, 38)
            Log.d(MainActivity.LOG_TAG, "distance: $distance")

            val cadence = extractSpecificData(dataString, 16, 20)
            Log.d(MainActivity.LOG_TAG, "cadence: $cadence")

            val power = extractSpecificData(dataString, 20, 24)
            Log.d(MainActivity.LOG_TAG, "power: $power")

            //???
            val speed = extractSpecificData(dataString, 10, 14)
            Log.d(MainActivity.LOG_TAG, "speed: $speed")

            //???
            val calories = extractSpecificData(dataString, 6, 10)
            Log.d(MainActivity.LOG_TAG, "calories: $calories")

            //???
            val strange = extractSpecificData(dataString, 4, 6)
            Log.d(MainActivity.LOG_TAG, "strange: $strange")

            sensorText = "distance : $distance [m] \n" +
                    "cadence : $cadence [RPM] \n" +
                    "power : $power [W] \n" +
                    "speed : $speed [m/s] \n" +
                    "calories : $calories [kcal] \n" +
                    "strange : $strange [???] \n"
        }
    }

    private fun extractSpecificData(dataString: String, from: Int, to: Int) : Int {
        Log.d(MainActivity.LOG_TAG, "data string ("+from+", "+to+") " + dataString.substring(from, to))
        return dataString.substring(from, to).toInt(16)
    }

    @OptIn(ExperimentalUuidApi::class)
    private val uuid = Uuid.parse("00001826-0000-1000-8000-00805f9b34fb")

    @OptIn(ExperimentalUuidApi::class, ExperimentalStdlibApi::class)
    override fun didDiscoverServices(bluetoothPeripheral: BluetoothPeripheral) {
        super.didDiscoverServices(bluetoothPeripheral)
        bluetoothPeripheral.services.forEach { it ->
            Log.d(MainActivity.LOG_TAG, "Discovered ${it.key}, ${it.value.name} services")
            if (it.key == uuid) {
                ftmsService = it.value
                ftmsService.characteristics.forEach { characteristic ->
                    blueFalcon.notifyCharacteristic(peripheral, characteristic, true)
                }
            }
        }
    }

    override fun didDisconnect(bluetoothPeripheral: BluetoothPeripheral) {
        super.didDisconnect(bluetoothPeripheral)
    }

    fun startUiRefreshTread(interval: Long) {
        uiRefreshRunnable = object : Runnable {
            override fun run() {
                // Your repeating task code here
                if (bikeText.text != sensorText) {
                    bikeText.text = sensorText
                }
                // Schedule the next execution
                handler.postDelayed(this, interval)
            }
        }
        handler.post(uiRefreshRunnable)
    }

    fun stopRepeatingTask() {
        handler.removeCallbacks(uiRefreshRunnable)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onResume() {
        super.onResume()
        blueFalcon.connect(peripheral)
        blueFalcon.discoverServices(peripheral, mutableListOf())
    }

    override fun onPause() {
        super.onPause()
        stopRepeatingTask()
        blueFalcon.disconnect(peripheral)

    }
}