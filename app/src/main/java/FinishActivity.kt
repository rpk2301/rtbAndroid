package com.rpk2301.ridethebus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FinishActivity : AppCompatActivity() {

    private lateinit var btnLeaderboard: Button
    private lateinit var btnPlayAgain: Button
    private lateinit var btnExit: Button
    private lateinit var tvScore: TextView
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        // Initialize views
        btnLeaderboard = findViewById(R.id.btnLeaderboard)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        btnExit = findViewById(R.id.btnExit)
        tvScore = findViewById(R.id.tvScore)
        adView = findViewById(R.id.adView)

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) {}

        // Load an ad into the AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        // Get the intent data
        val playerName = intent.getStringExtra("PLAYER_NAME") ?: "Unknown"
        val score = intent.getIntExtra("SCORE", 0)
        val timestamp = System.currentTimeMillis() / 1000 // Current timestamp in seconds

        // Update the score text
        tvScore.text = "Score: $score"

        // Upload player data to Firebase
        uploadPlayerDataToFirebase(playerName, score, timestamp)
        UserDataFileManager.writeTotalScore(score)

        // Set button click listeners
        btnLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        btnPlayAgain.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun uploadPlayerDataToFirebase(playerName: String, score: Int, timestamp: Long) {
        val database = FirebaseDatabase.getInstance()
        val playerRef = database.getReference("playerData").push()
        val totalScoreRef = database.getReference("totalScore")

        val isHardModeEnabled = UserDataFileManager.isHardModeEnabled()
        val playerData = hashMapOf(
            "playerName" to playerName,
            "score" to score,
            "timestamp" to timestamp,
            "platform" to "Android",
            "isHardModeEnabled" to isHardModeEnabled
        )

        playerRef.setValue(playerData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Data uploaded successfully
                totalScoreRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tempScore = snapshot.getValue(Int::class.java) ?: 0
                        totalScoreRef.setValue(tempScore + score)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
            } else {
                // Handle failure
            }
        }
    }
}
