package com.github.martinfrank.sport.trainingbikeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.bluefalcon.BluetoothPeripheral

class BluetoothPeripheralsAdapter(private val items: MutableList<BluetoothPeripheral>) : RecyclerView.Adapter<BluetoothPeripheralsAdapter.ViewHolder>() {

    private var selectedPosition = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].name
        holder.radioButton.isChecked = position == selectedPosition

        holder.radioButton.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
        holder.textView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size

    // Method to update the list
    fun updateList(newItems: List<BluetoothPeripheral>) {
        if (newItems != items) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }

    fun getSelectedItem(): BluetoothPeripheral? {
        if (selectedPosition == -1 || items.isEmpty() ) {
            return null
        }
        return items[selectedPosition]
    }
}