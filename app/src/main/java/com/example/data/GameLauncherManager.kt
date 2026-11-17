package com.example.data

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.MainActivity
import com.example.R
import com.example.model.GameDetectionState

class GameLauncherManager(private val context: Context) {

    val targetPackage = "com.innersloth.spacemafia"

    fun detectAmongUs(): GameDetectionState {
        val pm = context.packageManager
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(targetPackage, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(targetPackage, 0)
            }

            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }

            GameDetectionState(
                isInstalled = true,
                versionName = packageInfo.versionName ?: "Detectado",
                versionCode = versionCode,
                packageName = targetPackage,
                installLocation = packageInfo.applicationInfo?.sourceDir ?: "Armazenamento Interno",
                isModdedReady = true
            )
        } catch (e: Exception) {
            GameDetectionState(
                isInstalled = false,
                versionName = "Não instalado",
                versionCode = 0L,
                packageName = targetPackage,
                installLocation = "",
                isModdedReady = false
            )
        }
    }

    fun launchGameWithMods(
        fpsLimit: Int,
        streamerMode: Boolean,
        regionId: String,
        playerName: String,
        sessionToken: String,
        expiresTimestamp: Long
    ): Boolean {
        val pm = context.packageManager
        val launchIntent = pm.getLaunchIntentForPackage(targetPackage)
        return if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            launchIntent.putExtra("CRICRI_MODS_ACTIVE", true)
            launchIntent.putExtra("CRICRI_FPS_LIMIT", fpsLimit)
            launchIntent.putExtra("CRICRI_STREAMER_MODE", streamerMode)
            launchIntent.putExtra("CRICRI_REGION", regionId)
            launchIntent.putExtra("CRICRI_PLAYER_NAME", playerName)
            launchIntent.putExtra("CRICRI_SESSION_TOKEN", sessionToken)
            launchIntent.putExtra("CRICRI_SESSION_EXPIRES", expiresTimestamp)
            context.startActivity(launchIntent)
            true
        } else {
            openPlayStoreForAmongUs()
            false
        }
    }

    fun openPlayStoreForAmongUs() {
        try {
            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$targetPackage")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(marketIntent)
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$targetPackage")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }

    fun openCriCriSite() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cricribr.com.br")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Não foi possível abrir o navegador: https://cricribr.com.br", Toast.LENGTH_SHORT).show()
        }
    }

    fun openInnerslothLoginPortal() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.innersloth.com")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Não foi possível abrir https://accounts.innersloth.com", Toast.LENGTH_SHORT).show()
        }
    }

    fun createHomeScreenShortcut(): Boolean {
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            Toast.makeText(context, "Seu launcher não suporta atalhos fixos na tela inicial.", Toast.LENGTH_SHORT).show()
            return false
        }

        val shortcutIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            putExtra("LAUNCH_DIRECT_GAME", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val shortcut = ShortcutInfoCompat.Builder(context, "cricri_amongus_mod")
            .setShortLabel("Among Us (mod)")
            .setLongLabel("Among Us com CriCri Mods")
            .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_launcher))
            .setIntent(shortcutIntent)
            .build()

        val pinnedShortcutCallbackIntent = ShortcutManagerCompat.createShortcutResultIntent(context, shortcut)
        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            pinnedShortcutCallbackIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val pinned = ShortcutManagerCompat.requestPinShortcut(context, shortcut, successCallback.intentSender)
        if (pinned) {
            Toast.makeText(context, "Atalho criado na tela inicial!", Toast.LENGTH_SHORT).show()
        }
        return pinned
    }
}
