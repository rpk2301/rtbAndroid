// com.example.ridethebus.SetDisplayNameDialogFragment.kt
package com.rpk2301.ridethebus

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class SetDisplayNameDialogFragment : DialogFragment() {

    interface SetDisplayNameListener {
        fun onDisplayNameSet(displayName: String)
    }

    private var listener: SetDisplayNameListener? = null
    private lateinit var playerNameEditText: EditText
    private lateinit var createNameButton: Button

    fun setListener(listener: SetDisplayNameListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_set_display_name, container, false)

        val titleTextView: TextView = view.findViewById(R.id.tv_title)
        val noteTextView: TextView = view.findViewById(R.id.tv_note)
        playerNameEditText = view.findViewById(R.id.et_player_name)
        createNameButton = view.findViewById(R.id.btn_create_name)

        playerNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createNameButton.isEnabled = !s.isNullOrEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        createNameButton.setOnClickListener {
            val playerName = playerNameEditText.text.toString().trim()
            if (playerName.isNotEmpty()) {
                UserDataFileManager.changeDisplayName(playerName)
                dismiss()
            }
        }

        return view
    }
}
