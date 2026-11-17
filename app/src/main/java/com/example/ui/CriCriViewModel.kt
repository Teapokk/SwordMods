package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameLauncherManager
import com.example.data.ModsRepository
import com.example.data.PreferencesManager
import com.example.model.GameDetectionState
import com.example.model.InnerslothSession
import com.example.model.ModItem
import com.example.model.OptimizationConfig
import com.example.model.RealReplayFile
import com.example.model.ServerConfig
import com.example.model.ServerRegion
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppTab {
    HOME,
    ACCOUNT,
    REPLAYS
}

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
}

class CriCriViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val repo = ModsRepository(application, prefs)
    val launcher = GameLauncherManager(application)

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _gameState = MutableStateFlow(GameDetectionState())
    val gameState: StateFlow<GameDetectionState> = _gameState.asStateFlow()

    private val _session = MutableStateFlow(InnerslothSession())
    val session: StateFlow<InnerslothSession> = _session.asStateFlow()

    private val _mods = MutableStateFlow<List<ModItem>>(emptyList())
    val mods: StateFlow<List<ModItem>> = _mods.asStateFlow()

    private val _optimization = MutableStateFlow(OptimizationConfig())
    val optimization: StateFlow<OptimizationConfig> = _optimization.asStateFlow()

    private val _serverConfig = MutableStateFlow(ServerConfig())
    val serverConfig: StateFlow<ServerConfig> = _serverConfig.asStateFlow()

    private val _serverRegions = MutableStateFlow<List<ServerRegion>>(emptyList())
    val serverRegions: StateFlow<List<ServerRegion>> = _serverRegions.asStateFlow()

    private val _realReplays = MutableStateFlow<List<RealReplayFile>>(emptyList())
    val realReplays: StateFlow<List<RealReplayFile>> = _realReplays.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents: SharedFlow<UiEvent> = _uiEvents.asSharedFlow()

    init {
        refreshAll()
    }

    fun refreshAll() {
        _gameState.value = launcher.detectAmongUs()
        _session.value = prefs.getInnerslothSession()
        _mods.value = repo.getInitialMods()
        _optimization.value = prefs.getOptimizationConfig()
        _serverConfig.value = prefs.getServerConfig()
        _serverRegions.value = repo.getServerRegions()
        refreshReplays()
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun toggleMod(modId: String) {
        val updated = _mods.value.map { mod ->
            if (mod.id == modId) {
                val newStatus = !mod.isEnabled
                prefs.setModEnabled(modId, newStatus)
                mod.copy(isEnabled = newStatus)
            } else {
                mod
            }
        }
        _mods.value = updated
    }

    fun updateSession(updatedSession: InnerslothSession) {
        _session.value = updatedSession
        prefs.saveInnerslothSession(updatedSession)
    }

    fun renewSession24Hours() {
        prefs.renewSessionFor24Hours()
        _session.value = prefs.getInnerslothSession()
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.ShowToast("Sessão Innersloth renovada por +24 horas!"))
        }
    }

    fun logoutSession() {
        prefs.logoutSession()
        _session.value = prefs.getInnerslothSession()
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.ShowToast("Desconectado da conta Innersloth."))
        }
    }

    fun updateOptimization(newConfig: OptimizationConfig) {
        _optimization.value = newConfig
        prefs.saveOptimizationConfig(newConfig)
    }

    fun updateServerConfig(newConfig: ServerConfig) {
        _serverConfig.value = newConfig
        prefs.saveServerConfig(newConfig)
    }

    fun selectServerRegion(regionId: String) {
        val updated = _serverConfig.value.copy(selectedRegionId = regionId)
        updateServerConfig(updated)
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.ShowToast("Região: ${_serverRegions.value.find { it.id == regionId }?.name ?: regionId}"))
        }
    }

    fun launchGame(): Boolean {
        val opt = _optimization.value
        val ses = _session.value
        val srv = _serverConfig.value
        val launched = launcher.launchGameWithMods(
            fpsLimit = opt.fpsLimit,
            streamerMode = opt.streamerMode,
            regionId = srv.selectedRegionId,
            playerName = ses.accountName,
            sessionToken = ses.innerslothId,
            expiresTimestamp = ses.expiresTimestamp
        )
        if (!launched) {
            viewModelScope.launch {
                _uiEvents.emit(UiEvent.ShowToast("Among Us não encontrado. Abrindo Google Play Store..."))
            }
        }
        return launched
    }

    fun openCriCriSite() {
        launcher.openCriCriSite()
    }

    fun openInnerslothLoginPortal() {
        launcher.openInnerslothLoginPortal()
    }

    fun createShortcut() {
        val ok = launcher.createHomeScreenShortcut()
        viewModelScope.launch {
            if (ok) {
                _uiEvents.emit(UiEvent.ShowToast("Atalho criado na tela inicial!"))
            }
        }
    }

    fun refreshReplays() {
        _realReplays.value = repo.getRealReplays()
    }

    fun deleteReplay(filePath: String) {
        val success = repo.deleteReplayFile(filePath)
        if (success) {
            refreshReplays()
            viewModelScope.launch {
                _uiEvents.emit(UiEvent.ShowToast("Arquivo de replay excluído do disco."))
            }
        }
    }

    fun createSampleReplay(mapName: String = "The Skeld") {
        repo.createSampleRealReplay(mapName)
        refreshReplays()
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.ShowToast("Replay gerado no armazenamento."))
        }
    }

    fun getReplaysDirectoryPath(): String {
        return repo.getReplaysDirectory().absolutePath
    }
}
