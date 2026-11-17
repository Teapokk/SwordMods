package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.InnerslothSession
import com.example.model.OptimizationConfig
import com.example.model.ServerConfig

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("sword_mods_session_prefs", Context.MODE_PRIVATE)

    fun getInnerslothSession(): InnerslothSession {
        val isLoggedIn = prefs.getBoolean("inn_is_logged_in", true)
        val loginTime = prefs.getLong("inn_login_time", System.currentTimeMillis())
        var expireTime = prefs.getLong("inn_expire_time", 0L)
        val autoRenew = prefs.getBoolean("inn_auto_renew", true)

        // Se expireTime não estiver definido ou for menor que loginTime, define para 24 horas a partir de agora
        if (expireTime <= System.currentTimeMillis()) {
            if (autoRenew && isLoggedIn) {
                // Auto-renovação de 24 horas para nunca cair no meio de uma partida
                expireTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000L)
                prefs.edit().putLong("inn_expire_time", expireTime).apply()
            }
        }

        return InnerslothSession(
            isLoggedIn = isLoggedIn,
            accountName = prefs.getString("inn_account_name", "Sword_Player") ?: "Sword_Player",
            friendCode = prefs.getString("inn_friend_code", "SWORD#7712") ?: "SWORD#7712",
            innerslothId = prefs.getString("inn_account_id", "inn_98214309aef") ?: "inn_98214309aef",
            loginTimestamp = loginTime,
            expiresTimestamp = expireTime,
            autoRenew = autoRenew,
            keepCosmetics = prefs.getBoolean("inn_keep_cosmetics", true),
            selectedColorName = prefs.getString("inn_color_name", "Vermelho") ?: "Vermelho",
            selectedColorValue = prefs.getLong("inn_color_value", 0xFFE53935),
            selectedHat = prefs.getString("inn_hat", "Elmo do Cavaleiro") ?: "Elmo do Cavaleiro",
            selectedSkin = prefs.getString("inn_skin", "Armadura de Batalha") ?: "Armadura de Batalha"
        )
    }

    fun saveInnerslothSession(session: InnerslothSession) {
        prefs.edit()
            .putBoolean("inn_is_logged_in", session.isLoggedIn)
            .putString("inn_account_name", session.accountName)
            .putString("inn_friend_code", session.friendCode)
            .putString("inn_account_id", session.innerslothId)
            .putLong("inn_login_time", session.loginTimestamp)
            .putLong("inn_expire_time", session.expiresTimestamp)
            .putBoolean("inn_auto_renew", session.autoRenew)
            .putBoolean("inn_keep_cosmetics", session.keepCosmetics)
            .putString("inn_color_name", session.selectedColorName)
            .putLong("inn_color_value", session.selectedColorValue)
            .putString("inn_hat", session.selectedHat)
            .putString("inn_skin", session.selectedSkin)
            .apply()
    }

    fun renewSessionFor24Hours() {
        val current = getInnerslothSession()
        val updated = current.copy(
            isLoggedIn = true,
            loginTimestamp = System.currentTimeMillis(),
            expiresTimestamp = System.currentTimeMillis() + (24 * 60 * 60 * 1000L) // +24 horas
        )
        saveInnerslothSession(updated)
    }

    fun logoutSession() {
        val current = getInnerslothSession()
        val updated = current.copy(
            isLoggedIn = false,
            expiresTimestamp = 0L
        )
        saveInnerslothSession(updated)
    }

    fun getOptimizationConfig(): OptimizationConfig {
        return OptimizationConfig(
            fpsLimit = prefs.getInt("fps_limit", 60),
            showFpsCounter = prefs.getBoolean("fps_counter", true),
            resolutionScale = prefs.getFloat("resolution_scale", 1.0f),
            quickStart = prefs.getBoolean("quick_start", true),
            streamerMode = prefs.getBoolean("streamer_mode", false),
            antiLagNetwork = prefs.getBoolean("anti_lag", true)
        )
    }

    fun saveOptimizationConfig(config: OptimizationConfig) {
        prefs.edit()
            .putInt("fps_limit", config.fpsLimit)
            .putBoolean("fps_counter", config.showFpsCounter)
            .putFloat("resolution_scale", config.resolutionScale)
            .putBoolean("quick_start", config.quickStart)
            .putBoolean("streamer_mode", config.streamerMode)
            .putBoolean("anti_lag", config.antiLagNetwork)
            .apply()
    }

    fun getServerConfig(): ServerConfig {
        return ServerConfig(
            selectedRegionId = prefs.getString("server_region_id", "cricri_br") ?: "cricri_br",
            proximityChatEnabled = prefs.getBoolean("proximity_chat", true),
            customLobbyOptions = prefs.getBoolean("custom_lobby", true),
            autoConnect = prefs.getBoolean("auto_connect", true)
        )
    }

    fun saveServerConfig(config: ServerConfig) {
        prefs.edit()
            .putString("server_region_id", config.selectedRegionId)
            .putBoolean("proximity_chat", config.proximityChatEnabled)
            .putBoolean("custom_lobby", config.customLobbyOptions)
            .putBoolean("auto_connect", config.autoConnect)
            .apply()
    }

    fun isModEnabled(modId: String): Boolean {
        return prefs.getBoolean("mod_enabled_$modId", true)
    }

    fun setModEnabled(modId: String, enabled: Boolean) {
        prefs.edit().putBoolean("mod_enabled_$modId", enabled).apply()
    }
}
