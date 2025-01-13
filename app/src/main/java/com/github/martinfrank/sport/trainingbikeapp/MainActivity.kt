package com.github.martinfrank.sport.trainingbikeapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.bluefalcon.*
import java.util.*

class MainActivity : BlueFalconDelegate, ComponentActivity() {

    private lateinit var consoleText: TextView
    private lateinit var devicesText: TextView
    private lateinit var peripheralText: TextView
    private lateinit var blueFalcon: BlueFalcon
    private var devices = listOf<BluetoothDevice>()
    private lateinit var trainingBike: BluetoothPeripheral


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
        consoleText = findViewById(R.id.consoleText)
        devicesText = findViewById(R.id.devicesText)
        peripheralText = findViewById(R.id.peripheralText)
        val startScanButton = findViewById<Button>(R.id.startScanButton);
        val stopScanButton = findViewById<Button>(R.id.stopScanButton);
        val connectButton = findViewById<Button>(R.id.connectButton);
        val disconnectButton = findViewById<Button>(R.id.disconnectButton);

        //-----
        handlePermissions()
        checkBluetoothStatus()
        //-----

        try {
            blueFalcon = BlueFalcon(null, this.application, true)
            blueFalcon.delegates.add(this);
        } catch (exception: Exception) {
            consoleText.append("Exception creating blue falcon " + exception.message + "\n")
        }


        startScanButton.setOnClickListener {
            try {
                if (blueFalcon.isScanning) {
                    consoleText.setText("already scanning..." + Date(System.currentTimeMillis()) + "\n")
                } else {
                    consoleText.setText("start scanning..." + Date(System.currentTimeMillis()) + "\n")
                }
                blueFalcon.scan()
            } catch (exception: Exception) {
                consoleText.append("Exception " + exception.message + "\n")
            }
        }

        stopScanButton.setOnClickListener {
            try {
                if (blueFalcon.isScanning) {
                    blueFalcon.stopScanning()
                    consoleText.setText("stop scanning..." + Date(System.currentTimeMillis()) + "\n")
                } else {
                    consoleText.setText("already stopped scanning..." + Date(System.currentTimeMillis()) + "\n")
                }
            } catch (exception: Exception) {
                consoleText.append("Exception " + exception.message + "\n")
            }
        }

        connectButton.setOnClickListener {
            blueFalcon.connect(trainingBike)
        }
        disconnectButton.setOnClickListener {
            blueFalcon.disconnect(trainingBike)
        }
    }


    // ---------------------

    @SuppressLint("MissingPermission")
    override fun didDiscoverDevice(
        bluetoothPeripheral: BluetoothPeripheral,
        advertisementData: Map<AdvertisementDataRetrievalKeys, Any>
    ) {
        super.didDiscoverDevice(bluetoothPeripheral, advertisementData)
        if(! devices.contains(bluetoothPeripheral.device)) {
            devices += bluetoothPeripheral.device
        }
        devicesText.setText("")
        devices.forEach {
            if(it.name != null && it.name != "") {
                devicesText.append("device: " + it.name + "\n")
            }
        }

        if(bluetoothPeripheral.device.name != null && bluetoothPeripheral.device.name.startsWith("JAS_C3A")){
            trainingBike = bluetoothPeripheral
            peripheralText.setText("")
        }
    }

    override fun didConnect(bluetoothPeripheral: BluetoothPeripheral) {
        super.didConnect(bluetoothPeripheral)
        peripheralText.append("didConnect: " + bluetoothPeripheral + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didDisconnect(bluetoothPeripheral: BluetoothPeripheral) {
        super.didDisconnect(bluetoothPeripheral)
        peripheralText.append("didDisconnect: " + bluetoothPeripheral  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didDiscoverServices(bluetoothPeripheral: BluetoothPeripheral) {
        super.didDiscoverServices(bluetoothPeripheral)
        peripheralText.append("didDiscoverServices: " + bluetoothPeripheral  + Date(System.currentTimeMillis()) + "\n")

    }

    override fun didDiscoverCharacteristics(bluetoothPeripheral: BluetoothPeripheral) {
        super.didDiscoverCharacteristics(bluetoothPeripheral)
        peripheralText.append("didDiscoverCharacteristics: " + bluetoothPeripheral  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didCharacteristcValueChanged(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristic: BluetoothCharacteristic
    ) {
        super.didCharacteristcValueChanged(bluetoothPeripheral, bluetoothCharacteristic)
        peripheralText.append("didCharacteristcValueChanged: " + bluetoothPeripheral + "," + bluetoothCharacteristic  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didRssiUpdate(bluetoothPeripheral: BluetoothPeripheral) {
        super.didRssiUpdate(bluetoothPeripheral)
        peripheralText.append("didRssiUpdate: " + bluetoothPeripheral  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didUpdateMTU(bluetoothPeripheral: BluetoothPeripheral) {
        super.didUpdateMTU(bluetoothPeripheral)
        peripheralText.append("didUpdateMTU: " + bluetoothPeripheral  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didReadDescriptor(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristicDescriptor: BluetoothCharacteristicDescriptor
    ) {
        super.didReadDescriptor(bluetoothPeripheral, bluetoothCharacteristicDescriptor)
        peripheralText.append("didReadDescriptor: " + bluetoothPeripheral + "," + bluetoothCharacteristicDescriptor  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didWriteDescriptor(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristicDescriptor: BluetoothCharacteristicDescriptor
    ) {
        super.didWriteDescriptor(bluetoothPeripheral, bluetoothCharacteristicDescriptor)
        consoleText.append("didWriteDescriptor: " + bluetoothPeripheral + "," + bluetoothCharacteristicDescriptor  + Date(System.currentTimeMillis()) + "\n")
    }

    override fun didWriteCharacteristic(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristic: BluetoothCharacteristic,
        success: Boolean
    ) {
        super.didWriteCharacteristic(bluetoothPeripheral, bluetoothCharacteristic, success)
        consoleText.append("didWriteCharacteristic: " + bluetoothPeripheral + "," + BluetoothCharacteristic + "," + success  + Date(System.currentTimeMillis()) + "\n")
    }

    // ----------------------------------

    private fun checkBluetoothStatus() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter?.isEnabled == false){
            Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show()
        }
    }

    // ----------------------------------
    var PERMISSION_REQUEST_CODE = 4718

    private val requiredPermissions = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.BLUETOOTH_ADVERTISE,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    private fun handlePermissions() {

        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            // All permissions are already granted
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                //startBluetoothScanning()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
