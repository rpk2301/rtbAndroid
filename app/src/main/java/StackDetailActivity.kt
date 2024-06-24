package com.rpk2301.ridethebus

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class StackDetailActivity : AppCompatActivity() {

    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_detail)

        val backArrow = findViewById<ImageView>(R.id.ivBackArrow)
        val stackTitle = findViewById<TextView>(R.id.tvStackTitle)
        val cardCounts = findViewById<TextView>(R.id.tvCardCounts)
        val recyclerView = findViewById<RecyclerView>(R.id.rvStackCards)
        adView = findViewById(R.id.adView)

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) {}

        // Load an ad into the AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        backArrow.setOnClickListener {
            finish() // This will finish the current activity and go back to the previous one
        }

        val stackIndex = intent.getIntExtra("STACK_INDEX", 0)
        val cardsInStack = intent.getParcelableArrayListExtra<Card>("STACK_CARDS") ?: arrayListOf()

        stackTitle.text = "Stack ${stackIndex + 1} Details"

        val redCardsCount = cardsInStack.count { it.suit == "hearts" || it.suit == "diamonds" }
        val blackCardsCount = cardsInStack.count { it.suit == "clubs" || it.suit == "spades" }

        cardCounts.text = "Red Cards: $redCardsCount Black Cards: $blackCardsCount"

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CardAdapter(cardsInStack)
    }
}
