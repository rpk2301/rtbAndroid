// com.example.ridethebus.RecentRidesActivity.kt
package com.example.ridethebus

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

        // Initialize UI elements
        val backArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val recentRidesTitle: TextView = findViewById(R.id.tv_recent_rides_title)
        recyclerView = findViewById(R.id.rv_recent_rides)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recentRidesAdapter = RecentRidesAdapter(recentRidesList)
        recyclerView.adapter = recentRidesAdapter

        // Set the title
        recentRidesTitle.text = "Recent Rides"

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // Fetch recent rides data from Firebase
        fetchRecentRides()

        // Set the click listener for the back arrow
        backArrow.setOnClickListener {
            finish() // Close this activity and go back to the previous one
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
                            recentRidesList.add(playerData)
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
