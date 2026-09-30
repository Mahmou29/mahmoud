package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BriefEntity
import com.example.data.model.*
import com.example.data.repository.BriefDoctorRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class BriefDoctorUiState(
    val rawBriefText: String = "",
    val selectedTeam: TargetTeam = TargetTeam.ALL,
    val thinkingMode: ThinkingMode = ThinkingMode.CREATIVE,
    val appLanguage: AppLanguage = AppLanguage.ARABIC,
    val isAnalyzing: Boolean = false,
    val processingStepIndex: Int = 0,
    val currentProcessingStage: String = "",
    val analysisResult: CreativeAnalysisResult? = null,
    val activeAnalysisId: String? = null,
    val errorMessage: String? = null,
    val infoMessage: String? = null,
    val isHistoryVisible: Boolean = false,
    val isSettingsVisible: Boolean = false,
    val customApiKeyInput: String = "",
    val isAiActive: Boolean = false
)

class BriefDoctorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BriefDoctorRepository(application)
    private var analysisJob: Job? = null

    private val _uiState = MutableStateFlow(
        BriefDoctorUiState(
            isAiActive = repository.isAiAvailable(),
            customApiKeyInput = repository.getCustomApiKey() ?: ""
        )
    )
    val uiState: StateFlow<BriefDoctorUiState> = _uiState.asStateFlow()

    val historyBriefs: StateFlow<List<BriefEntity>> = repository.historyBriefs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    companion object {
        val PROCESSING_STAGES_AR = listOf(
            "بفهم الـBrief...",
            "بدور على اللي ناقص...",
            "بفكر في الـCreative Direction...",
            "بجهز الشغل للفريق...",
            "براجع النتيجة..."
        )

        val PROCESSING_STAGES_EN = listOf(
            "Understanding the Brief...",
            "Auditing Missing Information...",
            "Developing Creative Directions...",
            "Preparing Team Deliverables...",
            "Reviewing Final Output..."
        )
    }

    fun setAppLanguage(language: AppLanguage) {
        _uiState.update { it.copy(appLanguage = language) }
    }

    fun toggleAppLanguage() {
        _uiState.update {
            val next = if (it.appLanguage == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
            it.copy(appLanguage = next)
        }
    }

    fun onRawBriefChange(text: String) {
        _uiState.update { it.copy(rawBriefText = text) }
    }

    fun onTeamSelect(team: TargetTeam) {
        _uiState.update { it.copy(selectedTeam = team) }
    }

    fun onThinkingModeSelect(mode: ThinkingMode) {
        _uiState.update { it.copy(thinkingMode = mode) }
    }

    fun loadDemoPrompt(prompt: QuickDemoPrompt) {
        _uiState.update {
            it.copy(
                rawBriefText = prompt.rawText,
                selectedTeam = prompt.recommendedTeam,
                analysisResult = null,
                errorMessage = null,
                infoMessage = null
            )
        }
    }

    /**
     * Start a completely clean new brief workspace.
     * Clears: current brief, current analysis, temporary AI state, error/info.
     */
    fun startNewBrief() {
        analysisJob?.cancel()
        _uiState.update {
            it.copy(
                rawBriefText = "",
                analysisResult = null,
                activeAnalysisId = null,
                isAnalyzing = false,
                errorMessage = null,
                infoMessage = if (it.appLanguage == AppLanguage.ARABIC) "تم بدء مساحة عمل جديدة" else "Clean workspace started"
            )
        }
    }

    fun backToInput() {
        _uiState.update {
            it.copy(
                analysisResult = null,
                activeAnalysisId = null,
                isAnalyzing = false
            )
        }
    }

    /**
     * ONE BRIEF = ONE FRESH ANALYSIS FLOW:
     * 1. Read ONLY the current brief.
     * 2. Read ONLY the current selected team.
     * 3. Read ONLY the current settings/mode.
     * 4. Immediately clear the previous generated result.
     * 5. Create a new analysis session / analysis ID.
     * 6. Send ONLY the current data to the AI.
     * 7. Generate every result section from scratch.
     * 8. Replace the entire previous result with the new result.
     */
    fun analyzeBrief() {
        val currentState = _uiState.value
        val briefToAnalyze = currentState.rawBriefText.trim()
        val teamToAnalyze = currentState.selectedTeam
        val modeToAnalyze = currentState.thinkingMode
        val isArabic = currentState.appLanguage == AppLanguage.ARABIC

        if (briefToAnalyze.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = if (isArabic) "من فضلك اكتب كلام العميل الأول." else "Please enter the client brief first."
                )
            }
            return
        }

        // New unique analysis ID for fresh session
        val newAnalysisId = UUID.randomUUID().toString()

        // Cancel previous job if running
        analysisJob?.cancel()

        // STEP 4: Immediately clear previous result & show creative processing state
        _uiState.update {
            it.copy(
                analysisResult = null, // Old result disappears immediately!
                activeAnalysisId = newAnalysisId,
                isAnalyzing = true,
                errorMessage = null,
                infoMessage = null,
                processingStepIndex = 0,
                currentProcessingStage = if (isArabic) PROCESSING_STAGES_AR[0] else PROCESSING_STAGES_EN[0]
            )
        }

        analysisJob = viewModelScope.launch {
            try {
                // Creative Processing Sequence
                val stages = if (isArabic) PROCESSING_STAGES_AR else PROCESSING_STAGES_EN
                for (i in 0 until stages.size) {
                    _uiState.update {
                        it.copy(
                            processingStepIndex = i,
                            currentProcessingStage = stages[i]
                        )
                    }
                    delay(300)
                }

                // STEP 6: Send ONLY current brief data to AI
                val result = repository.analyzeBrief(
                    rawText = briefToAnalyze,
                    targetTeam = teamToAnalyze,
                    thinkingMode = modeToAnalyze,
                    analysisId = newAnalysisId
                )

                // Save to independent history record
                val firstLine = briefToAnalyze.lines().firstOrNull { it.isNotBlank() } ?: "بريف إبداعي"
                val displayTitle = firstLine.take(40)
                repository.saveAnalysisRecord(displayTitle, briefToAnalyze, result)

                // STEP 8: Replace the entire previous result with the new result
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        analysisResult = result,
                        activeAnalysisId = newAnalysisId
                    )
                }
            } catch (e: Exception) {
                // STEP: If analysis fails, old result must REMAIN cleared.
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        analysisResult = null, // Old result remains cleared!
                        errorMessage = if (isArabic) "حصلت مشكلة في تحليل البريف. جرّب تاني." else "An error occurred while analyzing the brief. Please try again."
                    )
                }
            }
        }
    }

    fun showHistory(show: Boolean) {
        _uiState.update { it.copy(isHistoryVisible = show) }
    }

    fun showSettings(show: Boolean) {
        _uiState.update {
            it.copy(
                isSettingsVisible = show,
                customApiKeyInput = repository.getCustomApiKey() ?: ""
            )
        }
    }

    fun onCustomApiKeyInputChange(input: String) {
        _uiState.update { it.copy(customApiKeyInput = input) }
    }

    fun saveCustomApiKey(key: String?) {
        repository.setCustomApiKey(key)
        _uiState.update {
            it.copy(
                isAiActive = repository.isAiAvailable(),
                isSettingsVisible = false,
                infoMessage = if (it.appLanguage == AppLanguage.ARABIC) "تم حفظ إعدادات المفتاح بنجاح" else "API Key configuration saved"
            )
        }
    }

    /**
     * History Isolation:
     * Opening History Item A loads ONLY A. Never merges old state.
     */
    fun loadFromHistory(entity: BriefEntity) {
        val targetTeam = try { TargetTeam.valueOf(entity.targetTeam) } catch (e: Exception) { TargetTeam.ALL }
        val mode = try { ThinkingMode.valueOf(entity.thinkingMode) } catch (e: Exception) { ThinkingMode.CREATIVE }
        val result = repository.deserializeAnalysisResult(entity.resultJson, targetTeam, mode)

        _uiState.update {
            it.copy(
                rawBriefText = entity.rawText,
                selectedTeam = targetTeam,
                thinkingMode = mode,
                analysisResult = result,
                activeAnalysisId = result?.analysisId ?: UUID.randomUUID().toString(),
                isHistoryVisible = false,
                isAnalyzing = false,
                errorMessage = null
            )
        }
    }

    fun deleteFromHistory(entity: BriefEntity) {
        viewModelScope.launch {
            repository.deleteBrief(entity)
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun dismissInfo() {
        _uiState.update { it.copy(infoMessage = null) }
    }
}
