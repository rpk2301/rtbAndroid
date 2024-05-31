// com.example.ridethebus.LeaderboardActivity.kt
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

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private val playerList = mutableListOf<PlayerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)


        val backArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val leaderboardTitle: TextView = findViewById(R.id.tv_leaderboard_title)
        recyclerView = findViewById(R.id.rv_leaderboard)

        recyclerView.layoutManager = LinearLayoutManager(this)
        leaderboardAdapter = LeaderboardAdapter(playerList)
        recyclerView.adapter = leaderboardAdapter


        val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
        leaderboardTitle.text = "$currentMonth Leaderboard"


        databaseReference = FirebaseDatabase.getInstance().reference


        fetchPlayerData()


        backArrow.setOnClickListener {
            finish()
        }
    }

    private fun fetchPlayerData() {
        databaseReference.child("playerData").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                playerList.clear()
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentYear = calendar.get(Calendar.YEAR)

                for (dataSnapshot in snapshot.children) {
                    val playerData = dataSnapshot.getValue(PlayerData::class.java)
                    if (playerData != null) {

                        val timestampInMilliseconds = playerData.timestamp * 1000L
                        val entryDate = Date(timestampInMilliseconds)
                        calendar.time = entryDate

                        val entryMonth = calendar.get(Calendar.MONTH)
                        val entryYear = calendar.get(Calendar.YEAR)

                        if (entryMonth == currentMonth && entryYear == currentYear) {
                            playerList.add(playerData)
                        }
                    }
                }
                playerList.sortByDescending { it.score }
                leaderboardAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("LeaderboardActivity", "Failed to load player data", error.toException())
            }
        })
    }
}
