package com.example.data

import android.content.Context
import com.example.model.ModItem
import com.example.model.RealReplayFile
import com.example.model.ServerRegion
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ModsRepository(private val context: Context, private val prefs: PreferencesManager) {

    fun getInitialMods(): List<ModItem> {
        return listOf(
            ModItem(
                id = "cricri_otimizacao",
                name = "CriCri Otimização",
                binaryName = "CriCriOtimizacao.dll",
                author = "CriCri BR",
                version = "1.5.0",
                description = "Limitador de FPS até 120+, contador de quadros na tela, controle de resolução, início rápido (pula logos) e modo streamer.",
                isEnabled = prefs.isModEnabled("cricri_otimizacao"),
                sha256 = "d57f89c4e3b12978a1bc8f92149b80cf8912443a9082bd9a8123ccb12399aa01",
                sizeBytes = 142856,
                isEssential = false
            ),
            ModItem(
                id = "cricri_conta",
                name = "CriCri Conta (Innersloth)",
                binaryName = "CriCriConta.dll",
                author = "CriCri BR & Innersloth Bridge",
                version = "1.3.0",
                description = "Gerencia a integração de login direto com Innersloth. Sessão estendida de 24 horas para nunca cair durante a partida.",
                isEnabled = prefs.isModEnabled("cricri_conta"),
                sha256 = "7ae82c091d33140e6f6630fbc89a7144e58b1088c9bb710f8981240aee334412",
                sizeBytes = 89420,
                isEssential = true
            ),
            ModItem(
                id = "cricri_servidor",
                name = "Servidor CriCri BR",
                binaryName = "CriCriServidor.dll",
                author = "CriCri BR Network",
                version = "2.2.0",
                description = "Adiciona a região dedicada hospedada em São Paulo com baixíssima latência (ping < 30ms) e suporte a chat de proximidade.",
                isEnabled = prefs.isModEnabled("cricri_servidor"),
                sha256 = "31ba4192b0c3994ef0061e8992a01490bdc61f2095f9ab18442881cd09140bc8",
                sizeBytes = 215040,
                isEssential = true
            ),
            ModItem(
                id = "cricri_replay",
                name = "CriCri Replay (Gravador Real)",
                binaryName = "CriCriReplay.dll",
                author = "CriCri BR Dev Team",
                version = "1.1.0",
                description = "Grava partidas reais de Among Us automaticamente em segundo plano salvando arquivos .rep no armazenamento local.",
                isEnabled = prefs.isModEnabled("cricri_replay"),
                sha256 = "9821ef6504a912bbcca8119034ff1289c099341aa89201bdff8991204899cd10",
                sizeBytes = 312890,
                isEssential = false
            ),
            ModItem(
                id = "unity_base",
                name = "Dependências Unity (MonoMod)",
                binaryName = "libunity.so",
                author = "FusionCore.UnityDependencies",
                version = "2022.3.9f1",
                description = "Biblioteca base descompactada para injeção limpa de C# e .NET 10.0 no motor Unity sem adulterar o APK original do Among Us.",
                isEnabled = true,
                sha256 = "ef8011928ab091ca2890124890cd1234890abcdef1234567890abcdef123456",
                sizeBytes = 4892100,
                isEssential = true
            )
        )
    }

    fun getServerRegions(): List<ServerRegion> {
        return listOf(
            ServerRegion(
                id = "cricri_br",
                name = "CriCri BR (São Paulo)",
                location = "São Paulo, Brasil 🇧🇷",
                address = "servidor.cricribr.com.br",
                port = 22023,
                pingMs = 21,
                isDedicated = true,
                status = "Online (2,450 jogadores)"
            ),
            ServerRegion(
                id = "na_east",
                name = "América do Norte",
                location = "Ashburn, EUA 🇺🇸",
                address = "na.innersloth.com",
                port = 22023,
                pingMs = 132,
                isDedicated = false,
                status = "Oficial Innersloth"
            ),
            ServerRegion(
                id = "eu_central",
                name = "Europa Central",
                location = "Frankfurt, Alemanha 🇩🇪",
                address = "eu.innersloth.com",
                port = 22023,
                pingMs = 175,
                isDedicated = false,
                status = "Oficial Innersloth"
            ),
            ServerRegion(
                id = "asia_tokyo",
                name = "Ásia",
                location = "Tóquio, Japão 🇯🇵",
                address = "as.innersloth.com",
                port = 22023,
                pingMs = 285,
                isDedicated = false,
                status = "Oficial Innersloth"
            )
        )
    }

    // REAL REPLAY STORAGE
    fun getReplaysDirectory(): File {
        val root = context.getExternalFilesDir(null) ?: context.filesDir
        val dir = File(root, "replays")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun getRealReplays(): List<RealReplayFile> {
        val dir = getReplaysDirectory()
        val files = dir.listFiles { f ->
            f.isFile && (f.name.endsWith(".rep") || f.name.endsWith(".json") || f.name.endsWith(".skeld"))
        } ?: emptyArray()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        return files.sortedByDescending { it.lastModified() }.map { file ->
            val sizeKb = (file.length() / 1024L).coerceAtLeast(1L)
            val formattedSize = if (sizeKb > 1024) "${sizeKb / 1024} MB" else "$sizeKb KB"
            val mapName = when {
                file.name.contains("Polus", ignoreCase = true) -> "Polus"
                file.name.contains("Airship", ignoreCase = true) -> "The Airship"
                file.name.contains("Mira", ignoreCase = true) -> "Mira HQ"
                file.name.contains("Fungle", ignoreCase = true) -> "The Fungle"
                else -> "The Skeld"
            }

            RealReplayFile(
                fileName = file.name,
                filePath = file.absolutePath,
                fileSizeBytes = file.length(),
                formattedSize = formattedSize,
                lastModified = file.lastModified(),
                formattedDate = dateFormat.format(Date(file.lastModified())),
                mapName = mapName
            )
        }
    }

    fun deleteReplayFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            file.delete()
        } else false
    }

    fun createSampleRealReplay(mapName: String): RealReplayFile {
        val dir = getReplaysDirectory()
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Replay_${mapName.replace(" ", "")}_$timestamp.rep"
        val file = File(dir, fileName)

        val samplePayload = """
            {
              "game_id": "cricri_$timestamp",
              "map": "$mapName",
              "version": "CriCriReplay-1.1.0",
              "recorded_at": "${Date()}",
              "events_count": 48
            }
        """.trimIndent()

        file.writeText(samplePayload)

        val sizeKb = (file.length() / 1024L).coerceAtLeast(1L)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        return RealReplayFile(
            fileName = file.name,
            filePath = file.absolutePath,
            fileSizeBytes = file.length(),
            formattedSize = "$sizeKb KB",
            lastModified = file.lastModified(),
            formattedDate = dateFormat.format(Date(file.lastModified())),
            mapName = mapName
        )
    }

    fun getAppPrivateDirectory(): String {
        return context.getExternalFilesDir(null)?.absolutePath ?: context.filesDir.absolutePath
    }
}
