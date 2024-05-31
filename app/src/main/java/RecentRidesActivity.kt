package com.example.ridethebus

import PlayerData
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class RecentRidesActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var recentRidesAdapter: RecentRidesAdapter
    private val recentRidesList = mutableListOf<PlayerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_rides)

        val backArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val recentRidesTitle: TextView = findViewById(R.id.tv_recent_rides_title)
        recyclerView = findViewById(R.id.rv_recent_rides)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recentRidesAdapter = RecentRidesAdapter(recentRidesList)
        recyclerView.adapter = recentRidesAdapter

        recentRidesTitle.text = "Recent Rides"

        databaseReference = FirebaseDatabase.getInstance().reference

        fetchRecentRides()

        backArrow.setOnClickListener {
            finish()
        }
    }

    private fun fetchRecentRides() {
        databaseReference.child("playerData").orderByChild("timestamp").limitToLast(100)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    recentRidesList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val playerData = dataSnapshot.getValue(PlayerData::class.java)
                        if (playerData != null) {
                            // Check if isHardModeEnabled is present, default to false if not
                            val isHardModeEnabled = dataSnapshot.child("isHardModeEnabled").getValue(Boolean::class.java) ?: false
                            // Check if platform is present, default to "iOS" if not
                            val platform = dataSnapshot.child("platform").getValue(String::class.java) ?: "iOS"
                            val playerDataWithPlatform = playerData.copy(isHardModeEnabled = isHardModeEnabled, platform = platform)
                            Log.d("RecentRidesActivity", "Fetched player data: $playerDataWithPlatform")
                            recentRidesList.add(playerDataWithPlatform)
                        }
                    }
                    recentRidesList.sortByDescending { it.timestamp }
                    recentRidesAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RecentRidesActivity", "Failed to load recent rides data", error.toException())
                }
            })
    }
}
