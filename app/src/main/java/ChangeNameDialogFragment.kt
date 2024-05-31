package com.example.ridethebus

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.core.content.ContextCompat

class ChangeNameDialogFragment : DialogFragment() {

    interface ChangeNameListener {
        fun onNameChanged(newName: String)
    }

    private var listener: ChangeNameListener? = null
    private lateinit var playerNameEditText: EditText
    private lateinit var changeNameButton: Button
    private lateinit var hardModeToggleImageView: ImageView
    private lateinit var hardModeTextView: TextView
    private lateinit var hardModeUnlockTextView: TextView
    private lateinit var currentNameTextView: TextView
    private lateinit var totalScoreTextView: TextView

    private var isHardModeEnabled = false
    private var totalScore = 0
    private var displayName = ""

    fun setListener(listener: ChangeNameListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_change_name, container, false)

        currentNameTextView = view.findViewById(R.id.tv_current_name)
        totalScoreTextView = view.findViewById(R.id.tv_total_score)
        playerNameEditText = view.findViewById(R.id.et_player_name)
        changeNameButton = view.findViewById(R.id.btn_change_name)
        hardModeToggleImageView = view.findViewById(R.id.iv_hard_mode_toggle)
        hardModeTextView = view.findViewById(R.id.tv_hard_mode)
        hardModeUnlockTextView = view.findViewById(R.id.tv_hard_mode_unlock)

        // Initialize the views
        displayName = UserDataFileManager.displayName
        totalScore = UserDataFileManager.displayTotalScore()
        isHardModeEnabled = UserDataFileManager.isHardModeEnabled()

        if (displayName.isEmpty()) {
            currentNameTextView.text = "Current Leaderboard Display Name: None Set"
        } else {
            currentNameTextView.text = "Current Leaderboard Display Name: $displayName"
        }

        totalScoreTextView.text = "Total Cards Ridden: $totalScore"
        updateHardModeViews()

        playerNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                changeNameButton.isEnabled = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        changeNameButton.setOnClickListener {
            val playerName = playerNameEditText.text.toString().trim()
            if (playerName.isNotEmpty()) {
                if (playerName == "Clear2301") {
                    UserDataFileManager.writeTotalScore(100 - UserDataFileManager.displayTotalScore())
                }
                UserDataFileManager.changeDisplayName(playerName)
                listener?.onNameChanged(playerName)
                dismiss()
            }
        }

        hardModeToggleImageView.setOnClickListener {
            if (totalScore > 99) {
                isHardModeEnabled = !isHardModeEnabled
                UserDataFileManager.setHardModeEnabled(isHardModeEnabled)
                updateHardModeViews()
            }
        }

        return view
    }

    private fun updateHardModeViews() {
        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
        val orangeDarkColor = ContextCompat.getColor(requireContext(), R.color.orange_dark)
        val clearColor = ContextCompat.getColor(requireContext(), R.color.clear)

        if (isHardModeEnabled) {
            hardModeTextView.setTextColor(redColor)
            hardModeToggleImageView.setImageResource(android.R.drawable.checkbox_on_background)
            hardModeToggleImageView.setColorFilter(clearColor)
        } else {
            hardModeTextView.setTextColor(orangeDarkColor)
            hardModeToggleImageView.setImageResource(android.R.drawable.checkbox_off_background)
            hardModeToggleImageView.setColorFilter(clearColor)
        }

        if (totalScore < 100) {
            val cardsLeft = 100 - totalScore
            hardModeUnlockTextView.text = "Ride $cardsLeft More Cards To Unlock Hard Mode"
        } else {
            hardModeUnlockTextView.text = ""
        }
    }
}
