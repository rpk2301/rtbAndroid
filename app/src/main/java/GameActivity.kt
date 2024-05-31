package com.example.ridethebus

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var cardStacks: Array<FrameLayout>
    private lateinit var btnHigher: Button
    private lateinit var btnSame: Button
    private lateinit var btnLower: Button
    private lateinit var btnHearts: Button
    private lateinit var btnBack: ImageButton
    private lateinit var tvTitle: TextView

    private val deck = mutableListOf<Card>()
    private var currentIndex = 0
    private val topCards = mutableListOf<Card>()
    private val allStacks = mutableListOf<MutableList<Card>>()
    private var score = 0
    private var playerName: String? = null

    private var glowAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Initialize views
        cardStacks = arrayOf(
            findViewById(R.id.frameLayout1),
            findViewById(R.id.frameLayout2),
            findViewById(R.id.frameLayout3),
            findViewById(R.id.frameLayout4)
        )

        btnHigher = findViewById(R.id.btnHigher)
        btnSame = findViewById(R.id.btnSame)
        btnLower = findViewById(R.id.btnLower)
        btnHearts = findViewById(R.id.btnHearts)
        btnBack = findViewById(R.id.btnBack)
        tvTitle = findViewById(R.id.tvTitle)

        // Retrieve the player name from UserDataFileManager
        playerName = UserDataFileManager.retrieveDisplayName()

        // Initialize deck and shuffle
        initializeDeck()
        shuffleDeck()

        // Initialize allStacks with empty lists for each stack
        for (i in cardStacks.indices) {
            allStacks.add(mutableListOf())
        }

        // Draw the initial cards for each stack
        drawInitialCards()

        // Apply glow animation to the first stack
        applyGlowAnimation(currentIndex)

        btnHigher.setOnClickListener { guess("higher") }
        btnSame.setOnClickListener { guess("same") }
        btnLower.setOnClickListener { guess("lower") }
        btnHearts.setOnClickListener { guess("hearts") }

        // Handle back button click
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set onClickListeners for each stack to navigate to StackDetailActivity
        cardStacks.forEachIndexed { index, frameLayout ->
            frameLayout.setOnClickListener {
                val intent = Intent(this, StackDetailActivity::class.java)
                intent.putExtra("STACK_INDEX", index)
                intent.putParcelableArrayListExtra("STACK_CARDS", ArrayList(allStacks[index]))
                startActivity(intent)
            }
        }
    }

    private fun initializeDeck() {
        val suits = listOf("hearts", "diamonds", "clubs", "spades")
        for (suit in suits) {
            for (rank in 2..14) {
                deck.add(Card(suit, rank))
            }
        }
    }

    private fun shuffleDeck() {
        deck.shuffle(Random(System.currentTimeMillis()))
        for (card in deck) {
            Log.d("Deck", "Card: ${card.rank} of ${card.suit}")
        }
    }

    private fun drawInitialCards() {
        for (i in cardStacks.indices) {
            val card = deck.removeAt(0)
            topCards.add(card)
            allStacks[i].add(card)
            updateCardImage(cardStacks[i].getChildAt(0) as ImageView, card)
        }
    }

    private fun updateCardImage(imageView: ImageView, card: Card) {
        val resourceId = resources.getIdentifier(card.getImageResourceName(), "drawable", packageName)
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            Toast.makeText(this, "Image not found: ${card.getImageResourceName()}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guess(guessType: String) {
        if (deck.isEmpty()) {
            initializeDeck()
            shuffleDeck()
            Toast.makeText(this, "The Deck Has Depleted. Dealing New Deck", Toast.LENGTH_SHORT).show()
            return
        }

        val nextCard = deck.removeAt(0)
        val currentTopCard = topCards[currentIndex]

        val correct = when (currentIndex) {
            2 -> {
                // Guess color on the third stack
                val nextColor = if (nextCard.suit == "hearts" || nextCard.suit == "diamonds") "red" else "black"
                nextColor == guessType
            }
            3 -> {
                // Guess suit on the fourth stack
                nextCard.suit == guessType
            }
            else -> when (guessType) {
                "higher" -> nextCard.getCardValue() > currentTopCard.getCardValue()
                "same" -> nextCard.getCardValue() == currentTopCard.getCardValue()
                "lower" -> nextCard.getCardValue() < currentTopCard.getCardValue()
                else -> false
            }
        }

        // Update the current card before moving to the next stack
        topCards[currentIndex] = nextCard
        allStacks[currentIndex].add(nextCard)
        updateCardImage(cardStacks[currentIndex].getChildAt(0) as ImageView, nextCard)

        // Increment the score and update the TextView
        score++
        tvTitle.text = "Score: $score"

        if (correct) {
            if (currentIndex < cardStacks.size - 1) {
                currentIndex++
                updateButtonLabels()
            } else {
                val intent = Intent(this, FinishActivity::class.java).apply {
                    putExtra("PLAYER_NAME", UserDataFileManager.retrieveDisplayName())
                    putExtra("SCORE", score)
                }
                startActivity(intent)
                finish()
            }
        } else {
            if (UserDataFileManager.isHardModeEnabled()) {
                currentIndex = 0
            } else {
                if (currentIndex > 0) {
                    currentIndex--
                }
            }
            updateButtonLabels()
        }

        // Apply glow animation to the new current stack
        applyGlowAnimation(currentIndex)
    }

    private fun applyGlowAnimation(currentIndex: Int) {
        // Clear previous glow animations and backgrounds
        glowAnimator?.cancel()
        for (i in cardStacks.indices) {
            val cardView = cardStacks[i].getChildAt(0) as ImageView
            cardView.background = null
            cardView.alpha = 1f // Reset alpha to fully opaque
        }

        // Apply the new glow background and start animation
        val currentCard = cardStacks[currentIndex].getChildAt(0) as ImageView
        currentCard.setBackgroundResource(R.drawable.glow_background)

        glowAnimator = ObjectAnimator.ofFloat(currentCard, "alpha", 0f, 1f).apply {
            duration = 1000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }

    private fun updateButtonLabels() {
        when (currentIndex) {
            1, 0 -> {
                // Default stack: Higher, Same, Lower
                btnHigher.text = "Higher"
                btnHigher.setBackgroundResource(R.drawable.btn_higher) // Custom higher button background
                btnHigher.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnHigher.setOnClickListener { guess("higher") }
                btnSame.text = "Same"
                btnSame.setBackgroundResource(R.drawable.btn_same) // Custom same button background
                btnSame.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnSame.setOnClickListener { guess("same") }
                btnLower.text = "Lower"
                btnLower.setBackgroundResource(R.drawable.btn_lower) // Custom lower button background
                btnLower.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnLower.setOnClickListener { guess("lower") }
                btnLower.visibility = View.VISIBLE // Show the lower button
                btnHearts.visibility = View.GONE // Hide the hearts button
            }
            2 -> {
                // Third stack: Guess color
                btnHigher.text = "Red"
                btnHigher.setBackgroundResource(R.drawable.btn_red)
                btnHigher.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnHigher.setOnClickListener { guess("red") }
                btnSame.text = "Black"
                btnSame.setBackgroundResource(R.drawable.btn_black)
                btnSame.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnSame.setOnClickListener { guess("black") }
                btnLower.visibility = View.GONE // Hide the lower button
                btnHearts.visibility = View.GONE // Hide the hearts button
            }
            3 -> {
                // Fourth stack: Guess suit
                btnHigher.text = "Clubs"
                btnHigher.setBackgroundResource(R.drawable.btn_black) // Reset to default background
                btnHigher.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnHigher.setOnClickListener { guess("clubs") }
                btnSame.text = "Spades"
                btnSame.setBackgroundResource(R.drawable.btn_black) // Reset to default background
                btnSame.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnSame.setOnClickListener { guess("spades") }
                btnLower.text = "Diamonds"
                btnLower.setBackgroundResource(R.drawable.btn_red) // Reset to default background
                btnLower.setTextColor(ContextCompat.getColor(this, android.R.color.white))
                btnLower.setOnClickListener { guess("diamonds") }
                btnLower.visibility = View.VISIBLE // Show the lower button
                btnHearts.visibility = View.VISIBLE // Show the hearts button
            }
        }
    }
}
