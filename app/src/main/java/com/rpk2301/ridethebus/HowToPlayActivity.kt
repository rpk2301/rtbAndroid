package com.rpk2301.ridethebus

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class HowToPlayActivity : AppCompatActivity() {

    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

        val backArrow: ImageView = findViewById(R.id.iv_back_arrow)
        val howToPlayTitle: TextView = findViewById(R.id.tv_how_to_play_title)
        adView = findViewById(R.id.adView)

        backArrow.setOnClickListener {
            finish()
        }

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) {}

        // Load an ad into the AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}
