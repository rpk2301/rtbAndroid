package com.example.ridethebus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), SetDisplayNameDialogFragment.SetDisplayNameListener, ChangeNameDialogFragment.ChangeNameListener {

    private lateinit var rideCounterCombined: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var leaderboardButton: Button
    private lateinit var recentRidesButton: Button
    private lateinit var playButton: Button
    private lateinit var profileIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserDataFileManager.initialize(this)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        rideCounterCombined = findViewById(R.id.rideCounterCombined)
        leaderboardButton = findViewById(R.id.leaderboardButton)
        recentRidesButton = findViewById(R.id.recentRidesButton)
        playButton = findViewById(R.id.playButton)
        profileIcon = findViewById(R.id.profileIcon)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        fetchTotalScore()

        // Set up the button click listeners
        leaderboardButton.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        recentRidesButton.setOnClickListener {
            val intent = Intent(this, RecentRidesActivity::class.java)
            startActivity(intent)
        }

        playButton.setOnClickListener {
            if (UserDataFileManager.displayName == "") {
                showSetDisplayNameDialog()
            } else {
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)
            }
        }

        profileIcon.setOnClickListener {
            showChangeNameDialog()
        }
    }

    private fun showSetDisplayNameDialog() {
        val dialog = SetDisplayNameDialogFragment()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "SetDisplayNameDialogFragment")
    }

    private fun showChangeNameDialog() {
        val dialog = ChangeNameDialogFragment()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "ChangeNameDialogFragment")
    }

    override fun onDisplayNameSet(displayName: String) {
        // Handle the display name set by the user
        UserDataFileManager.displayName = displayName
    }

    override fun onNameChanged(newName: String) {
        // Handle the name change
        UserDataFileManager.displayName = newName
    }

    private fun fetchTotalScore() {
        databaseReference.child("totalScore").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the totalScore value
                    val totalScore = dataSnapshot.getValue(Long::class.java) ?: 0
                    val combinedText = "$totalScore\nCards Ridden"
                    rideCounterCombined.text = combinedText
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}
