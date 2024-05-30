package com.example.ridethebus

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var cardStack1: ImageView
    private lateinit var cardStack2: ImageView
    private lateinit var cardStack3: ImageView
    private lateinit var cardStack4: ImageView

    private lateinit var btnHigher: Button
    private lateinit var btnSame: Button
    private lateinit var btnLower: Button
    private lateinit var btnRed: Button
    private lateinit var btnBlack: Button
    private lateinit var btnHearts: Button
    private lateinit var btnDiamonds: Button
    private lateinit var btnClubs: Button
    private lateinit var btnSpades: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize views
        cardStack1 = findViewById(R.id.cardStack1)
        cardStack2 = findViewById(R.id.cardStack2)
        cardStack3 = findViewById(R.id.cardStack3)
        cardStack4 = findViewById(R.id.cardStack4)

        btnHigher = findViewById(R.id.btnHigher)
        btnSame = findViewById(R.id.btnSame)
        btnLower = findViewById(R.id.btnLower)


        // Set click listeners for the buttons
        btnHigher.setOnClickListener {
            // Handle the higher button click
            Toast.makeText(this, "Higher clicked", Toast.LENGTH_SHORT).show()
        }

        btnSame.setOnClickListener {
            // Handle the same button click
            Toast.makeText(this, "Same clicked", Toast.LENGTH_SHORT).show()
        }

        btnLower.setOnClickListener {
            // Handle the lower button click
            Toast.makeText(this, "Lower clicked", Toast.LENGTH_SHORT).show()
        }
    }

}
