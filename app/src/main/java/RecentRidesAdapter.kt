package com.rpk2301.ridethebus

import PlayerData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class RecentRidesAdapter(private val recentRidesList: List<PlayerData>) :
    RecyclerView.Adapter<RecentRidesAdapter.RecentRidesViewHolder>() {

    class RecentRidesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val platformLogoImageView: ImageView = itemView.findViewById(R.id.iv_platform_logo)
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

        val timestampInMilliseconds = player.timestamp * 1000L
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = Date(timestampInMilliseconds)
        holder.timestampTextView.text = dateFormat.format(date)

        holder.scoreTextView.text = player.score.toString()

        // Change text color based on isHardModeEnabled
        val textColor = if (player.isHardModeEnabled) {
            holder.itemView.context.resources.getColor(R.color.red)
        } else {
            holder.itemView.context.resources.getColor(R.color.yellow)
        }
        holder.playerNameTextView.setTextColor(textColor)
        holder.timestampTextView.setTextColor(textColor)
        holder.scoreTextView.setTextColor(textColor)

        // Set platform logo based on platform
        val platformLogoResId = if (player.platform == "Android") {
            R.drawable.ic_android_logo // Replace with your actual Android logo drawable
        } else {
            R.drawable.applelogo // Replace with your actual Apple logo drawable
        }
        holder.platformLogoImageView.setImageResource(platformLogoResId)
    }

    override fun getItemCount() = recentRidesList.size
}
