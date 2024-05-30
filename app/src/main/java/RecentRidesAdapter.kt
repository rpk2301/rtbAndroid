// com.example.ridethebus.RecentRidesAdapter.kt
package com.example.ridethebus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class RecentRidesAdapter(private val recentRidesList: List<PlayerData>) :
    RecyclerView.Adapter<RecentRidesAdapter.RecentRidesViewHolder>() {

    class RecentRidesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameTextView: TextView = itemView.findViewById(R.id.tv_player_name)
        val timestampTextView: TextView = itemView.findViewById(R.id.tv_timestamp)
        val scoreTextView: TextView = itemView.findViewById(R.id.tv_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentRidesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_rides, parent, false)
        return RecentRidesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecentRidesViewHolder, position: Int) {
        val player = recentRidesList[position]
        holder.playerNameTextView.text = player.playerName

        // Convert the timestamp from seconds to milliseconds
        val timestampInMilliseconds = player.timestamp * 1000L
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = Date(timestampInMilliseconds)
        holder.timestampTextView.text = dateFormat.format(date)

        holder.scoreTextView.text = player.score.toString()
    }

    override fun getItemCount() = recentRidesList.size
}
