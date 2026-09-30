package com.example.data.model

import java.util.UUID

enum class AppLanguage(val code: String, val label: String) {
    ARABIC("ar", "العربية"),
    ENGLISH("en", "English")
}

enum class TargetTeam(val labelAr: String, val labelEn: String) {
    ALL("الكل", "All"),
    CONTENT("Content", "Content"),
    DESIGN("Design", "Design"),
    VIDEO("Video", "Video"),
    ACCOUNT("Account", "Account")
}

enum class ThinkingMode(val labelAr: String, val descAr: String, val labelEn: String, val descEn: String) {
    PRACTICAL("عملي", "أفكار واقعية وسريعة وقابلة للتنفيذ المباشر", "Practical", "Feasible, fast, ready-to-execute ideas"),
    CREATIVE("كريتيف", "أفكار ذكية ومميزة ومبنية على فكرة استراتيجية", "Creative", "Smart, distinctive, strategy-backed ideas"),
    BOLD("جريء", "أفكار تجريبية وغير تقليدية تكسر المألوف", "Bold", "Edgy, out-of-the-box concepts that break norms")
}

data class ClientUnderstanding(
    val summary: String,
    val clientStated: List<String>,
    val creativeSuggestion: List<String>
)

data class ActionableBrief(
    val objective: String? = null,
    val audience: String? = null,
    val productOrService: String? = null,
    val offer: String? = null,
    val platform: String? = null,
    val deliverables: String? = null,
    val tone: String? = null,
    val constraints: String? = null
)

data class DesignOutput(
    val creativeConcept: String,
    val visualDirection: String,
    val composition: String? = null,
    val heroElement: String? = null,
    val lighting: String? = null,
    val colorDirection: String? = null,
    val typographyDirection: String? = null,
    val negativeSpace: String? = null,
    val requiredAssets: String? = null,
    val executionNotes: String? = null
)

data class VideoOutput(
    val coreIdea: String,
    val openingHook: String,
    val storyStructure: String? = null,
    val sceneSequence: String? = null,
    val shotSuggestions: String? = null,
    val cameraLanguage: String? = null,
    val motionDirection: String? = null,
    val transitionDirection: String? = null,
    val soundDesign: String? = null,
    val duration: String? = null
)

data class ContentOutput(
    val contentAngle: String,
    val mainIdea: String,
    val hook: String,
    val storyDirection: String? = null,
    val captionDirection: String? = null,
    val cta: String? = null,
    val reelConcept: String? = null
)

data class AccountOutput(
    val whatClientWants: String,
    val missingInfo: List<String>,
    val questionsForClient: List<String>,
    val deliverablesList: String,
    val risksAndAmbiguities: List<String>
)

data class CreativeIdea(
    val title: String, // اسم الاتجاه الإبداعي المفهومي
    val coreIdea: String, // 1. الفكرة
    val strategicInsight: String, // 2. ليه الفكرة دي؟
    val intendedMessage: String, // 3. الرسالة
    val visualExecution: String, // 4. التنفيذ البصري
    val composition: String? = null, // 5. التكوين
    val heroElement: String? = null, // 6. العنصر البطل
    val keyDetails: String? = null, // 7. التفاصيل المهمة
    val whatToAvoid: String? = null, // 8. تجنب إيه
    val executionNotes: String? = null // ملاحظات التنفيذ
)

data class CreativeAnalysisResult(
    val analysisId: String = UUID.randomUUID().toString(),
    val briefText: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val targetTeam: TargetTeam,
    val thinkingMode: ThinkingMode,
    val realCommunicationChallenge: String = "",
    val creativeOpportunity: String = "",
    val understanding: ClientUnderstanding,
    val actionableBrief: ActionableBrief,
    val missingEssentials: List<String>,
    val designOutput: DesignOutput? = null,
    val videoOutput: VideoOutput? = null,
    val contentOutput: ContentOutput? = null,
    val accountOutput: AccountOutput? = null,
    val creativeIdeas: List<CreativeIdea> = emptyList(),
    val executionGuidance: String? = null
)

data class QuickDemoPrompt(
    val id: String,
    val label: String,
    val rawText: String,
    val recommendedTeam: TargetTeam
)
