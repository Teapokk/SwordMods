package com.example.model

data class InnerslothSession(
    val isLoggedIn: Boolean = true,
    val accountName: String = "Sword_Player",
    val friendCode: String = "SWORD#7712",
    val innerslothId: String = "inn_98214309aef",
    val loginTimestamp: Long = System.currentTimeMillis(),
    val expiresTimestamp: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000L), // Sessão de 24 horas garantida
    val autoRenew: Boolean = true,
    val keepCosmetics: Boolean = true,
    val selectedColorName: String = "Vermelho",
    val selectedColorValue: Long = 0xFFE53935,
    val selectedHat: String = "Elmo do Cavaleiro",
    val selectedSkin: String = "Armadura de Batalha"
) {
    val isSessionValid: Boolean
        get() = isLoggedIn && System.currentTimeMillis() < expiresTimestamp

    val remainingHours: Long
        get() = if (expiresTimestamp > System.currentTimeMillis()) {
            (expiresTimestamp - System.currentTimeMillis()) / (1000 * 60 * 60)
        } else 0L

    val remainingMinutes: Long
        get() = if (expiresTimestamp > System.currentTimeMillis()) {
            ((expiresTimestamp - System.currentTimeMillis()) / (1000 * 60)) % 60
        } else 0L

    val formattedRemainingTime: String
        get() = if (isSessionValid) {
            "${remainingHours}h ${remainingMinutes}m restantes"
        } else {
            "Sessão expirada"
        }
}

data class RealReplayFile(
    val fileName: String,
    val filePath: String,
    val fileSizeBytes: Long,
    val formattedSize: String,
    val lastModified: Long,
    val formattedDate: String,
    val mapName: String = "The Skeld"
)

data class ModItem(
    val id: String,
    val name: String,
    val binaryName: String,
    val author: String,
    val version: String,
    val description: String,
    val isEnabled: Boolean = true,
    val sha256: String,
    val sizeBytes: Long,
    val isEssential: Boolean = false
)

data class OptimizationConfig(
    val fpsLimit: Int = 60,
    val showFpsCounter: Boolean = true,
    val resolutionScale: Float = 1.0f,
    val quickStart: Boolean = true,
    val streamerMode: Boolean = false,
    val antiLagNetwork: Boolean = true
)

data class ServerRegion(
    val id: String,
    val name: String,
    val location: String,
    val address: String,
    val port: Int,
    val pingMs: Int,
    val isDedicated: Boolean = false,
    val status: String = "Online"
)

data class ServerConfig(
    val selectedRegionId: String = "cricri_br",
    val proximityChatEnabled: Boolean = true,
    val customLobbyOptions: Boolean = true,
    val autoConnect: Boolean = true
)

data class GameDetectionState(
    val isInstalled: Boolean = false,
    val versionName: String = "Não instalado",
    val versionCode: Long = 0L,
    val packageName: String = "com.innersloth.spacemafia",
    val installLocation: String = "",
    val isModdedReady: Boolean = true
)
