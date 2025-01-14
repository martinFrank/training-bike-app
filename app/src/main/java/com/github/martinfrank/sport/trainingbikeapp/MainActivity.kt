package com.github.martinfrank.sport.trainingbikeapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.bluefalcon.*

class MainActivity : BlueFalconDelegate, ComponentActivity() {

    private lateinit var blueFalcon: BlueFalcon
    private var peripherals = listOf<BluetoothPeripheral>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: BluetoothPeripheralsAdapter
    private var uuidPattern =
        "[A-F0-9][A-F0-9]:[A-F0-9][A-F0-9]:[A-F0-9][A-F0-9]:[A-F0-9][A-F0-9]:[A-F0-9][A-F0-9]:[A-F0-9][A-F0-9]".toRegex()

    companion object {
        var PERIPHERAL_EXTRA: String = "com.github.martinfrank.sport.trainingbikeapp.MainActivity.peripheral"
        var LOG_TAG: String = "com.github.martinfrank.sport.trainingbikeapp.logtag"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = BluetoothPeripheralsAdapter(mutableListOf())
        recyclerView.adapter = recyclerViewAdapter

        val connectButton = findViewById<Button>(R.id.connectButton)

        //-----
        handlePermissions()
        checkBluetoothStatus()
        //-----
        blueFalcon = BlueFalcon(null, this.application, true)
        blueFalcon.delegates.add(this)
        blueFalcon.scan()


        connectButton.setOnClickListener {

            val selected = recyclerViewAdapter.getSelectedItem()
            if (selected == null){
                Toast.makeText(this, "no device selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (blueFalcon.isScanning) {
                blueFalcon.stopScanning()
            }
            blueFalcon.delegates.remove(this)
            val bundle = Bundle()
            bundle.putParcelable(PERIPHERAL_EXTRA, selected)
            val intent = Intent(this, JaSportC3Activity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    // ---------------------

    @SuppressLint("MissingPermission")
    override fun didDiscoverDevice(
        bluetoothPeripheral: BluetoothPeripheral,
        advertisementData: Map<AdvertisementDataRetrievalKeys, Any>
    ) {
        super.didDiscoverDevice(bluetoothPeripheral, advertisementData)
        if (!peripherals.contains(bluetoothPeripheral) && !uuidPattern.matches(bluetoothPeripheral.name!!)) {
            peripherals += bluetoothPeripheral
        }
        recyclerViewAdapter.updateList(peripherals)
    }


    // ----------------------------------

    private fun checkBluetoothStatus() {
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show()
        }
    }

    // ----------------------------------
    private var permissionRequestCode = 4718

    @SuppressLint("InlinedApi")
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
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), permissionRequestCode)
        } else {
            // All permissions are already granted
        }
    }


    override fun onResume() {
        super.onResume()
        blueFalcon.scan()
    }

    override fun onPause() {
        super.onPause()
        recyclerViewAdapter.updateList(mutableListOf())
        blueFalcon.stopScanning()

    }


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                //startBluetoothScanning()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
