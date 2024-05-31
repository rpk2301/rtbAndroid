data class PlayerData(
    val playerName: String = "",
    val timestamp: Long = 0,
    val score: Int = 0,
    val isHardModeEnabled: Boolean = false,
    val platform: String = "" // Ensure this field is included
)
