package com.example.ridethebus

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StackDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_detail)

        val backArrow = findViewById<ImageView>(R.id.ivBackArrow)
        val stackTitle = findViewById<TextView>(R.id.tvStackTitle)
        val recyclerView = findViewById<RecyclerView>(R.id.rvStackCards)

        backArrow.setOnClickListener {
            finish() // This will finish the current activity and go back to the previous one
        }

        val stackIndex = intent.getIntExtra("STACK_INDEX", 0)
        val cardsInStack = intent.getParcelableArrayListExtra<Card>("STACK_CARDS") ?: arrayListOf()

        stackTitle.text = "Stack ${stackIndex + 1} Details"

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CardAdapter(cardsInStack)
    }
}
