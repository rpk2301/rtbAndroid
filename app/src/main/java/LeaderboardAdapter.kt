// com.example.ridethebus.LeaderboardAdapter.kt
package com.example.ridethebus

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class LeaderboardAdapter(private val playerList: List<PlayerData>) :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankTextView: TextView = itemView.findViewById(R.id.tv_rank)
        val playerNameTextView: TextView = itemView.findViewById(R.id.tv_player_name)
        val timestampTextView: TextView = itemView.findViewById(R.id.tv_timestamp)
        val scoreTextView: TextView = itemView.findViewById(R.id.tv_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val player = playerList[position]
        holder.rankTextView.text = "${position + 1}."
        holder.playerNameTextView.text = player.playerName

        // Log the raw timestamp value
        Log.d("LeaderboardAdapter", "Raw Timestamp: ${player.timestamp}")

        // Convert the timestamp from seconds to milliseconds
        val timestampInMilliseconds = player.timestamp * 1000L

        // Format the timestamp correctly
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = Date(timestampInMilliseconds)
        holder.timestampTextView.text = dateFormat.format(date)

        // Log the formatted date
        Log.d("LeaderboardAdapter", "Formatted Date: ${dateFormat.format(date)}")

        holder.scoreTextView.text = player.score.toString()
    }

    override fun getItemCount() = playerList.size
}
