package com.rpk2301.ridethebus

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.io.File

@SuppressLint("StaticFieldLeak")
object UserDataFileManager {

    private lateinit var context: Context

    private const val PREFERENCES_FILE_KEY = "com.example.ridethebus.PREFERENCES_FILE_KEY"
    private const val IS_HARD_MODE_ENABLED_KEY = "isHardModeEnabled"
    private const val DISPLAY_NAME_FILE = "DisplayName.txt"
    private const val TOTAL_SCORE_FILE = "TotalScore.txt"

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
    }

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    fun setHardModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(IS_HARD_MODE_ENABLED_KEY, enabled).apply()
    }

    fun isHardModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(IS_HARD_MODE_ENABLED_KEY, false)
    }

    fun resetHardMode() {
        sharedPreferences.edit().remove(IS_HARD_MODE_ENABLED_KEY).apply()
    }

    var displayName: String
        get() {
            val file = File(context.filesDir, DISPLAY_NAME_FILE)
            return if (file.exists()) {
                file.readText().trim()
            } else {
                ""
            }
        }
        set(value) {
            val file = File(context.filesDir, DISPLAY_NAME_FILE)
            file.writeText(value.trim())
        }

    fun changeDisplayName(newName: String) {
        displayName = newName
    }

    fun retrieveDisplayName(): String {
        return displayName
    }

    private fun filePath(forPlayer: String): File {
        return File(context.filesDir, "$forPlayer.txt")
    }

    fun saveScore(forPlayer: String, score: Int) {
        val file = filePath(forPlayer)
        file.writeText(score.toString())
    }

    fun loadScore(forPlayer: String): Int? {
        val file = filePath(forPlayer)
        return if (file.exists()) {
            file.readText().toIntOrNull()
        } else {
            null
        }
    }

    fun listPlayerFiles(): List<File> {
        return context.filesDir.listFiles()?.filter { it.extension == "txt" } ?: emptyList()
    }

    fun listPlayersAndScores() {
        listPlayerFiles().forEach { file ->
            val playerId = file.nameWithoutExtension
            if (playerId != "TotalScore") {
                loadScore(playerId)?.let { score ->
                    println("Player ID: $playerId, Score: $score")
                }
            }
        }
    }

    fun deletePlayer(playerId: String) {
        val file = filePath(playerId)
        if (file.exists()) {
            file.delete()
        }
    }

    fun writeTotalScore(score: Int) {
        val file = File(context.filesDir, TOTAL_SCORE_FILE)
        val currentScore = if (file.exists()) {
            file.readText().trim().toIntOrNull() ?: 0
        } else {
            0
        }
        file.writeText((currentScore + score).toString())
    }

    fun displayTotalScore(): Int {
        val file = File(context.filesDir, TOTAL_SCORE_FILE)
        return if (file.exists()) {
            file.readText().trim().toIntOrNull() ?: 0
        } else {
            0
        }
    }
}
