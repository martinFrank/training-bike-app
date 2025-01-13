package com.github.martinfrank.sport.trainingbikeapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.bluefalcon.BluetoothPeripheral

class BikeActivity : AppCompatActivity() {

    private lateinit var bikeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bike)
        bikeText = findViewById(R.id.bikeText)
        val bikeButton = findViewById<Button>(R.id.bikeButton)

        val bundle = intent.extras
        if (bundle != null){
            var peripheral: BluetoothPeripheral? = bundle.getParcelable(MainActivity.PERIPHERAL_EXTRA)

            bikeText.text = peripheral?.let { String.format("peripheral: %s", it.name) }
        }


    }

}