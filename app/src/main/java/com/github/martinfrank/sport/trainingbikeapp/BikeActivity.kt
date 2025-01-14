package com.github.martinfrank.sport.trainingbikeapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.bluefalcon.BlueFalcon
import dev.bluefalcon.BlueFalconDelegate
import dev.bluefalcon.BluetoothCharacteristic
import dev.bluefalcon.BluetoothPeripheral
import dev.bluefalcon.BluetoothService
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BikeActivity : BlueFalconDelegate, AppCompatActivity() {

    private lateinit var bikeText: TextView
    private lateinit var characteristicText: TextView
    private lateinit var blueFalcon: BlueFalcon
    private lateinit var peripheral: BluetoothPeripheral
    private lateinit var ftmsService: BluetoothService
    private var sensorText = "not measured"


    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable


    @OptIn(ExperimentalUuidApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bike)
        bikeText = findViewById(R.id.bikeText)
        characteristicText = findViewById(R.id.characteristicText)

        startRepeatingTask(100)

        val bundle = intent.extras
        if (bundle != null) {
            peripheral = bundle.getParcelable(MainActivity.PERIPHERAL_EXTRA)!!
            bikeText.text = peripheral.let { String.format("peripheral: %s", it.name) }
            blueFalcon = BlueFalcon(null, this.application, true)
            blueFalcon.delegates.add(this);
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
        Log.d(MainActivity.LOG_TAG, "didCharacteristcValueChange value=${bluetoothCharacteristic.value?.toHexString()}")
        var charText = bluetoothCharacteristic.value?.toHexString()
        parseData(charText.toString())
    }

    private fun parseData(dataString: String) {
        Log.d(MainActivity.LOG_TAG, String.format("parseData : %s", dataString))
        //d40900000000000000000000000000ffffff1200
        //d409c9054e0047000026000a000100ffffff2c00
        //d409a3054c002d000025000b000000ffffff1b00
        //0123456789012345678901234567890123456789
        //0000000000111111111122222222223333333333
//        sensorText = string
        if (dataString.length == 40) {
            Log.d(MainActivity.LOG_TAG, "data string (36, 38) " + dataString.substring(36, 38))
            val distance = dataString.substring(36, 38).toInt(16)
            Log.d(MainActivity.LOG_TAG, "distance " + distance)

            Log.d(MainActivity.LOG_TAG, "data string (16, 20) " + dataString.substring(16, 20))
            val cadence = dataString.substring(16, 20).toInt(16)
            Log.d(MainActivity.LOG_TAG, "cadence " + cadence)

            Log.d(MainActivity.LOG_TAG, "data string (20, 24) " + dataString.substring(20, 24))
            val power = dataString.substring(20, 24).toInt(16)
            Log.d(MainActivity.LOG_TAG, "power " + power)

            sensorText = "distance : ${distance} [m] \n" +
                    "cadence : ${cadence} [RPM] \n" +
                    "power : ${power} [W] \n"
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private val uuid = Uuid.parse("00001826-0000-1000-8000-00805f9b34fb");

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

    fun startRepeatingTask(interval: Long) {
        runnable = object : Runnable {
            override fun run() {
                // Your repeating task code here
                if (bikeText.text != sensorText) {
                    bikeText.text = sensorText
                }
                // Schedule the next execution
                handler.postDelayed(this, interval)
            }
        }
        handler.post(runnable)
    }

    fun stopRepeatingTask() {
        handler.removeCallbacks(runnable)
    }
}