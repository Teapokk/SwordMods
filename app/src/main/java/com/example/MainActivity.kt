package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.CriCriViewModel
import com.example.ui.UiEvent
import com.example.ui.components.GamingHeader
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ReplaysScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SwordGold
import com.example.ui.theme.SwordRed
import com.example.ui.theme.TextMutedSlate
import com.example.ui.theme.TextWhite
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: CriCriViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle direct launch from home-screen shortcut
        if (intent?.getBooleanExtra("LAUNCH_DIRECT_GAME", false) == true) {
            viewModel.launchGame()
        }

        setContent {
            MyApplicationTheme {
                SwordModsApp(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAll()
    }
}

@Composable
fun SwordModsApp(viewModel: CriCriViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val session by viewModel.session.collectAsStateWithLifecycle()
    val mods by viewModel.mods.collectAsStateWithLifecycle()
    val optimization by viewModel.optimization.collectAsStateWithLifecycle()
    val serverConfig by viewModel.serverConfig.collectAsStateWithLifecycle()
    val serverRegions by viewModel.serverRegions.collectAsStateWithLifecycle()
    val realReplays by viewModel.realReplays.collectAsStateWithLifecycle()

    val context = androidx.compose.ui.platform.LocalContext.current

    // Listen to UI events
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // BackHandler: Return to Home if in another tab
    if (currentTab != AppTab.HOME) {
        BackHandler {
            viewModel.setTab(AppTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding(),
        topBar = {
            GamingHeader(
                regionName = serverRegions.find { it.id == serverConfig.selectedRegionId }?.name ?: "CriCri BR",
                pingMs = serverRegions.find { it.id == serverConfig.selectedRegionId }?.pingMs ?: 21,
                onRefresh = { viewModel.refreshAll() }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SlateSurface,
                contentColor = TextWhite,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("main_bottom_nav")
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.HOME,
                    onClick = { viewModel.setTab(AppTab.HOME) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Início"
                        )
                    },
                    label = {
                        Text(
                            text = "Início",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.HOME) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = SwordRed,
                        indicatorColor = SwordRed,
                        unselectedIconColor = TextMutedSlate,
                        unselectedTextColor = TextMutedSlate
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.ACCOUNT,
                    onClick = { viewModel.setTab(AppTab.ACCOUNT) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Conta Innersloth"
                        )
                    },
                    label = {
                        Text(
                            text = "Conta (24h)",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.ACCOUNT) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = SwordGold,
                        indicatorColor = SwordRed,
                        unselectedIconColor = TextMutedSlate,
                        unselectedTextColor = TextMutedSlate
                    ),
                    modifier = Modifier.testTag("nav_tab_account")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.REPLAYS,
                    onClick = { viewModel.setTab(AppTab.REPLAYS) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Replays"
                        )
                    },
                    label = {
                        Text(
                            text = "Replays",
                            fontSize = 11.sp,
                            fontWeight = if (currentTab == AppTab.REPLAYS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TextWhite,
                        selectedTextColor = SwordRed,
                        indicatorColor = SwordRed,
                        unselectedIconColor = TextMutedSlate,
                        unselectedTextColor = TextMutedSlate
                    ),
                    modifier = Modifier.testTag("nav_tab_replays")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SlateBackground)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "TabContentTransition"
            ) { tab ->
                when (tab) {
                    AppTab.HOME -> HomeScreen(
                        gameState = gameState,
                        session = session,
                        mods = mods,
                        optimization = optimization,
                        serverConfig = serverConfig,
                        onLaunchGame = { viewModel.launchGame() },
                        onOpenCriCriSite = { viewModel.openCriCriSite() },
                        onCreateShortcut = { viewModel.createShortcut() },
                        onRenewSession = { viewModel.renewSession24Hours() },
                        onNavigateToAccount = { viewModel.setTab(AppTab.ACCOUNT) },
                        onToggleMod = { viewModel.toggleMod(it) },
                        onRefreshState = { viewModel.refreshAll() }
                    )
                    AppTab.ACCOUNT -> AccountScreen(
                        session = session,
                        onUpdateSession = { viewModel.updateSession(it) },
                        onRenewSession = { viewModel.renewSession24Hours() },
                        onLogout = { viewModel.logoutSession() },
                        onOpenInnerslothPortal = { viewModel.openInnerslothLoginPortal() }
                    )
                    AppTab.REPLAYS -> ReplaysScreen(
                        replays = realReplays,
                        storagePath = viewModel.getReplaysDirectoryPath(),
                        onRefresh = { viewModel.refreshReplays() },
                        onDeleteReplay = { viewModel.deleteReplay(it) },
                        onCreateTestReplay = { viewModel.createSampleReplay(it) }
                    )
                }
            }
        }
    }
}
