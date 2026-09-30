package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppLanguage
import com.example.ui.BriefDoctorViewModel
import com.example.ui.screens.*
import com.example.ui.theme.BriefDoctorTheme
import com.example.ui.theme.StudioDark950

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BriefDoctorTheme {
                val viewModel: BriefDoctorViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val historyList by viewModel.historyBriefs.collectAsStateWithLifecycle()

                val layoutDirection = if (uiState.appLanguage == AppLanguage.ARABIC) LayoutDirection.Rtl else LayoutDirection.Ltr
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    val snackbarHostState = remember { SnackbarHostState() }

                    // Back navigation: return to home screen if results are shown
                    BackHandler(enabled = uiState.analysisResult != null) {
                        viewModel.backToInput()
                    }

                    // Snackbars
                    LaunchedEffect(uiState.errorMessage) {
                        uiState.errorMessage?.let { msg ->
                            snackbarHostState.showSnackbar(
                                message = msg,
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                            viewModel.dismissError()
                        }
                    }

                    LaunchedEffect(uiState.infoMessage) {
                        uiState.infoMessage?.let { msg ->
                            snackbarHostState.showSnackbar(
                                message = msg,
                                duration = SnackbarDuration.Short
                            )
                            viewModel.dismissInfo()
                        }
                    }

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(StudioDark950),
                        containerColor = StudioDark950,
                        contentWindowInsets = WindowInsets.safeDrawing,
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            // Desktop-first wide canvas up to 1600dp
                            val containerModifier = Modifier
                                .fillMaxHeight()
                                .widthIn(max = 1600.dp)

                            AnimatedContent(
                                targetState = uiState.analysisResult,
                                transitionSpec = {
                                    if (targetState != null) {
                                        (fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 10 })
                                            .togetherWith(fadeOut(tween(200)))
                                    } else {
                                        fadeIn(tween(300))
                                            .togetherWith(fadeOut(tween(200)) + slideOutVertically(tween(200)) { it / 10 })
                                    }
                                },
                                label = "home_result_switch"
                            ) { result ->
                                if (result == null) {
                                    SimpleHomeScreen(
                                        rawText = uiState.rawBriefText,
                                        selectedTeam = uiState.selectedTeam,
                                        thinkingMode = uiState.thinkingMode,
                                        appLanguage = uiState.appLanguage,
                                        isAnalyzing = uiState.isAnalyzing,
                                        processingStepIndex = uiState.processingStepIndex,
                                        currentProcessingStage = uiState.currentProcessingStage,
                                        onRawTextChange = { viewModel.onRawBriefChange(it) },
                                        onTeamSelect = { viewModel.onTeamSelect(it) },
                                        onThinkingModeSelect = { viewModel.onThinkingModeSelect(it) },
                                        onToggleLanguage = { viewModel.toggleAppLanguage() },
                                        onAnalyzeClick = { viewModel.analyzeBrief() },
                                        onStartNewBrief = { viewModel.startNewBrief() },
                                        onLoadDemo = { viewModel.loadDemoPrompt(it) },
                                        onOpenHistory = { viewModel.showHistory(true) },
                                        onOpenSettings = { viewModel.showSettings(true) },
                                        isAiActive = uiState.isAiActive,
                                        modifier = containerModifier
                                    )
                                } else {
                                    AnalysisResultScreen(
                                        result = result,
                                        appLanguage = uiState.appLanguage,
                                        onBackToInput = { viewModel.backToInput() },
                                        onStartNewBrief = { viewModel.startNewBrief() },
                                        modifier = containerModifier
                                    )
                                }
                            }
                        }
                    }

                    // Archive / History Dialog (Isolated records)
                    if (uiState.isHistoryVisible) {
                        HistoryDialog(
                            historyList = historyList,
                            appLanguage = uiState.appLanguage,
                            onSelectBrief = { viewModel.loadFromHistory(it) },
                            onDeleteBrief = { viewModel.deleteFromHistory(it) },
                            onDismiss = { viewModel.showHistory(false) }
                        )
                    }

                    // Settings / API Key Dialog
                    if (uiState.isSettingsVisible) {
                        ApiKeyDialog(
                            currentCustomKey = uiState.customApiKeyInput,
                            isAiActive = uiState.isAiActive,
                            appLanguage = uiState.appLanguage,
                            onSaveKey = { viewModel.saveCustomApiKey(it) },
                            onDismiss = { viewModel.showSettings(false) }
                        )
                    }
                }
            }
        }
    }
}
