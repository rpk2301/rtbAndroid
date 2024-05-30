package com.example.ridethebus

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var rideCounter: TextView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize TextView
        rideCounter = findViewById(R.id.rideCounter)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // Fetch totalScore from Firebase
        fetchTotalScore()
    }

    private fun fetchTotalScore() {
        databaseReference.child("totalScore").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the totalScore value
                    val totalScore = dataSnapshot.getValue(Long::class.java)
                    // Update the TextView
                    rideCounter.text = "$totalScore Cards Ridden"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}
