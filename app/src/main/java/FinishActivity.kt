package com.example.ridethebus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private lateinit var btnLeaderboard: Button
    private lateinit var btnPlayAgain: Button
    private lateinit var btnExit: Button
    private lateinit var tvScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        // Initialize views
        btnLeaderboard = findViewById(R.id.btnLeaderboard)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        btnExit = findViewById(R.id.btnExit)
        tvScore = findViewById(R.id.tvScore)

        // Get the intent data
        val playerName = intent.getStringExtra("PLAYER_NAME")
        val score = intent.getIntExtra("SCORE", 0)
        val timestamp = System.currentTimeMillis() / 1000 // Current timestamp in seconds

        // Update the score text
        tvScore.text = "Score: $score"

        // Upload player data to Firebase
        uploadPlayerDataToFirebase(playerName, score, timestamp)

        // Set button click listeners
        btnLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        btnPlayAgain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun uploadPlayerDataToFirebase(playerName: String?, score: Int, timestamp: Long) {
        if (playerName == null) return

        val database = FirebaseDatabase.getInstance()
        val playerRef = database.getReference("players").push()

        val playerData = hashMapOf(
            "playerName" to playerName,
            "score" to score,
            "timestamp" to timestamp,
            "platform" to "Android"
        )

        playerRef.setValue(playerData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Data uploaded successfully
            } else {
                // Handle the error
            }
        }
    }
}
