package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.api.AgencyDiagnosticEngine
import com.example.data.api.GeminiClient
import com.example.data.local.AppDatabase
import com.example.data.local.BriefEntity
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class BriefDoctorRepository(private val context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val briefDao = db.briefDao()
    private val prefs = context.getSharedPreferences("brief_doctor_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "BriefDoctorRepo"
        private const val PREF_CUSTOM_API_KEY = "custom_gemini_api_key"
    }

    fun getCustomApiKey(): String? {
        return prefs.getString(PREF_CUSTOM_API_KEY, null)
    }

    fun setCustomApiKey(key: String?) {
        prefs.edit().putString(PREF_CUSTOM_API_KEY, key?.trim()).apply()
    }

    fun getEffectiveApiKey(): String {
        return GeminiClient.getApiKey(getCustomApiKey())
    }

    fun isAiAvailable(): Boolean {
        return getEffectiveApiKey().isNotBlank()
    }

    val historyBriefs: Flow<List<BriefEntity>> = briefDao.getAllBriefs()

    suspend fun analyzeBrief(
        rawText: String,
        targetTeam: TargetTeam = TargetTeam.ALL,
        thinkingMode: ThinkingMode = ThinkingMode.CREATIVE,
        analysisId: String = UUID.randomUUID().toString()
    ): CreativeAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = getEffectiveApiKey()
        if (apiKey.isBlank()) {
            Log.i(TAG, "No API key provided. Executing internal Strategic Agency Diagnostic Engine.")
            return@withContext AgencyDiagnosticEngine.analyzeBrief(rawText, targetTeam, thinkingMode, analysisId)
        }

        val prompt = """
أنت مدير إبداعي استراتيجي (Executive Creative Director) في وكالة إعلانية مرموقة.
مهمتك: تحليل وفحص بريف العميل التالي وتقديم تفكير استراتيجي إبداعي حقيقي يضيف قيمة ولا يعيد صياغة كلام العميل.

بريف العميل الحالي:
\"\"\"
$rawText
\"\"\"

الفريق المستهدف: [${targetTeam.name}]
أسلوب التفكير المطلوب: [${thinkingMode.labelAr}]

قواعد التفكير الاستراتيجي الإلزامية:
1. التفكير قبل الكتابة (Strategic Thinking First):
   - حدد "realCommunicationChallenge": ما هو العائق الحقيقي أمام استجابة الجمهور؟ (لماذا قد يتجاهل الجمهور الإعلان؟ ما التحدي الاتصالي الفعلي؟).
   - حدد "creativeOpportunity": ما هي الزاوية الذكية أو الفرصة الإبداعية التي يمكن امتلاكها بدلاً من الحل الإعلاني المكرر؟
   - تحدي البريف (Challenge the brief): إذا طلب العميل مجرد "بوست أو إعلان عن عرض"، لا تقف عند المظهر؛ بل حوّل العرض إلى فكرة تهم الجمهور.
   - افصل بدقة بين:
     * FACT (ما ذكره العميل صراحة في clientStated)
     * INFERENCE & RECOMMENDATION (ما تقترحه أنت كمدير إبداعي في creativeSuggestion)

2. الاتجاهات الإبداعية قائمة على الأدلة (Evidence-Based Creative Directions):
   - الجودة فوق العدد (QUALITY > COUNT): قدم من 1 إلى 3 اتجاهات إبداعية فقط حسب قوة الأفكار الحقيقية لهذا البريف. لا تقدم اتجاهاً ضعيفاً لمجرد ملء خانات.
   - عناوين مفاهيمية عربية محددة: ممنوع تماماً العناوين الإعلانية الإنجليزية المبتذلة (مثل "Freshness Unveiled" أو "Beyond Expectations" أو "Modern Journey"). اكتب عنواناً مفهومياً يصف الفكرة مثل: "خلي المنتج هو اللي يكشف الفرق" أو "بيع الصبح الهادي مش مساحة الشقة".
   - لكل اتجاه إبداعي أجب عن العناصر الثمانية:
     * title: عنوان الاتجاه المفهومي
     * coreIdea: 1. الفكرة الإبداعية المحددة
     * strategicInsight: 2. ليه الفكرة دي؟ (الاستنتاج من هذا البريف بالتحديد)
     * intendedMessage: 3. الرسالة (ما يجب أن يفهمه أو يشعر به الجمهور)
     * visualExecution: 4. التنفيذ البصري المحدد
     * composition: 5. التكوين وترتيب العناصر ولماذا
     * heroElement: 6. العنصر البطل ولماذا
     * keyDetails: 7. التفاصيل المهمة التي تجعل الاتجاه مميزاً
     * whatToAvoid: 8. تجنب إيه (ما يضعف الفكرة البصرية)
     * executionNotes: ملاحظات للمصمم أو المخرج

3. توجيهات التصميم بدون قوالب إجبارية (NO FORCED FIELDS):
   - في designOutput: اشرح "لماذا" وماذا تحقق كل توصية لهذا العميل بالذات.
   - إذا كانت الإضاءة أو المساحة السلبية غير محورية، اترك الحقل فارغاً/null ولا تخترع جملاً عامة مثل "Use cinematic lighting" أو "Use bold typography".

4. إرشاد تنفيذي عملي (Actionable Execution Guidance):
   - اكتب توجيهاً واضحاً ومباشراً يستطيع المصمم أو المونتير أو كاتب المحتوى البدء بالعمل بناءً عليه فوراً (مثل: "خلّي المنتج يحتل 60% من الكادر مع إضاءة طبيعية دافئة، وضع العنوان في المساحة المقابلة...").

أرجع فقط كائن JSON صالح يطابق البنية التالية:
{
  "realCommunicationChallenge": "...",
  "creativeOpportunity": "...",
  "understanding": {
    "summary": "...",
    "clientStated": ["..."],
    "creativeSuggestion": ["..."]
  },
  "actionableBrief": {
    "objective": "...",
    "audience": "...",
    "productOrService": "...",
    "offer": "...",
    "platform": "...",
    "deliverables": "...",
    "tone": "...",
    "constraints": "..."
  },
  "missingEssentials": ["..."],
  "designOutput": {
    "creativeConcept": "...",
    "visualDirection": "...",
    "composition": "...",
    "heroElement": "...",
    "lighting": "...",
    "colorDirection": "...",
    "typographyDirection": "...",
    "negativeSpace": "...",
    "requiredAssets": "...",
    "executionNotes": "..."
  },
  "videoOutput": {
    "coreIdea": "...",
    "openingHook": "...",
    "storyStructure": "...",
    "sceneSequence": "...",
    "shotSuggestions": "...",
    "cameraLanguage": "...",
    "motionDirection": "...",
    "transitionDirection": "...",
    "soundDesign": "...",
    "duration": "..."
  },
  "contentOutput": {
    "contentAngle": "...",
    "mainIdea": "...",
    "hook": "...",
    "storyDirection": "...",
    "captionDirection": "...",
    "cta": "...",
    "reelConcept": "..."
  },
  "accountOutput": {
    "whatClientWants": "...",
    "missingInfo": ["..."],
    "questionsForClient": ["..."],
    "deliverablesList": "...",
    "risksAndAmbiguities": ["..."]
  },
  "creativeIdeas": [
    {
      "title": "...",
      "coreIdea": "...",
      "strategicInsight": "...",
      "intendedMessage": "...",
      "visualExecution": "...",
      "composition": "...",
      "heroElement": "...",
      "keyDetails": "...",
      "whatToAvoid": "...",
      "executionNotes": "..."
    }
  ],
  "executionGuidance": "..."
}
""".trimIndent()

        val systemInstruction = "أنت مدير إبداعي تنفيذي (ECD) استراتيجي في وكالة إعلانية. لغتك مهنية، ذكية، مصرية/عربية راقية، تركز على الفكرة والفرصة الاستراتيجية الحقيقية وتنفيها عن القوالب المكررة. أرجع فقط JSON صالح."

        try {
            val result = GeminiClient.generateContent(prompt, apiKey, systemInstruction)
            result.fold(
                onSuccess = { jsonText ->
                    try {
                        val cleaned = cleanJsonString(jsonText)
                        val json = JSONObject(cleaned)
                        parseAnalysisJson(json, targetTeam, thinkingMode, rawText, analysisId)
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to parse Gemini JSON, falling back to dynamic engine: ${e.message}")
                        AgencyDiagnosticEngine.analyzeBrief(rawText, targetTeam, thinkingMode, analysisId)
                    }
                },
                onFailure = { err ->
                    Log.w(TAG, "Gemini call failed (${err.javaClass.simpleName}: ${err.message}), seamlessly running strategic diagnostic engine.")
                    AgencyDiagnosticEngine.analyzeBrief(rawText, targetTeam, thinkingMode, analysisId)
                }
            )
        } catch (e: Throwable) {
            Log.w(TAG, "Unexpected error in AI call, utilizing strategic diagnostic engine: ${e.message}")
            AgencyDiagnosticEngine.analyzeBrief(rawText, targetTeam, thinkingMode, analysisId)
        }
    }

    suspend fun saveAnalysisRecord(
        title: String,
        rawText: String,
        result: CreativeAnalysisResult
    ): Long = withContext(Dispatchers.IO) {
        val entity = BriefEntity(
            title = title.ifBlank { "بريف - ${result.analysisId.take(8)}" },
            rawText = rawText,
            targetTeam = result.targetTeam.name,
            thinkingMode = result.thinkingMode.name,
            resultJson = serializeAnalysisResult(result)
        )
        briefDao.insertBrief(entity)
    }

    suspend fun deleteBrief(brief: BriefEntity) = withContext(Dispatchers.IO) {
        briefDao.deleteBrief(brief)
    }

    private fun cleanJsonString(raw: String): String {
        var str = raw.trim()
        if (str.startsWith("```json")) {
            str = str.removePrefix("```json")
        } else if (str.startsWith("```")) {
            str = str.removePrefix("```")
        }
        if (str.endsWith("```")) {
            str = str.removeSuffix("```")
        }
        return str.trim()
    }

    private fun parseAnalysisJson(
        json: JSONObject,
        targetTeam: TargetTeam,
        thinkingMode: ThinkingMode,
        rawText: String,
        analysisId: String
    ): CreativeAnalysisResult {
        val emptyState = "مفيش معلومات كفاية في البريف."

        val realChallenge = json.optString("realCommunicationChallenge").takeIf { it.isNotBlank() }
            ?: "تحديد التحدي الاتصالي الحقيقي لجذب اهتمام الجمهور المستهدف دون الاعتماد على رسائل ترويجية مباشرة مكررة."

        val creativeOpportunity = json.optString("creativeOpportunity").takeIf { it.isNotBlank() }
            ?: "خلق زاوية فريدة تربط حل البريف بموقف أو قيمة يومية يشعر بها الجمهور."

        val underObj = json.optJSONObject("understanding")
        val summary = underObj?.optString("summary")?.takeIf { it.isNotBlank() } ?: "تم استيعاب طلب العميل استراتيجياً."
        val stated = mutableListOf<String>()
        underObj?.optJSONArray("clientStated")?.let { arr ->
            for (i in 0 until arr.length()) {
                val s = arr.getString(i)
                if (s.isNotBlank()) stated.add(s)
            }
        }
        val suggestions = mutableListOf<String>()
        underObj?.optJSONArray("creativeSuggestion")?.let { arr ->
            for (i in 0 until arr.length()) {
                val s = arr.getString(i)
                if (s.isNotBlank()) suggestions.add(s)
            }
        }

        val actObj = json.optJSONObject("actionableBrief")
        val brief = ActionableBrief(
            objective = actObj?.optString("objective")?.takeIf { it.isNotBlank() } ?: emptyState,
            audience = actObj?.optString("audience")?.takeIf { it.isNotBlank() } ?: emptyState,
            productOrService = actObj?.optString("productOrService")?.takeIf { it.isNotBlank() } ?: emptyState,
            offer = actObj?.optString("offer")?.takeIf { it.isNotBlank() } ?: emptyState,
            platform = actObj?.optString("platform")?.takeIf { it.isNotBlank() } ?: emptyState,
            deliverables = actObj?.optString("deliverables")?.takeIf { it.isNotBlank() } ?: emptyState,
            tone = actObj?.optString("tone")?.takeIf { it.isNotBlank() } ?: emptyState,
            constraints = actObj?.optString("constraints")?.takeIf { it.isNotBlank() } ?: emptyState
        )

        val missing = mutableListOf<String>()
        json.optJSONArray("missingEssentials")?.let { arr ->
            for (i in 0 until arr.length()) {
                val m = arr.getString(i)
                if (m.isNotBlank()) missing.add(m)
            }
        }

        val designOutput = json.optJSONObject("designOutput")?.let { d ->
            DesignOutput(
                creativeConcept = d.optString("creativeConcept", emptyState),
                visualDirection = d.optString("visualDirection", emptyState),
                composition = d.optString("composition").takeIf { it.isNotBlank() },
                heroElement = d.optString("heroElement").takeIf { it.isNotBlank() },
                lighting = d.optString("lighting").takeIf { it.isNotBlank() },
                colorDirection = d.optString("colorDirection").takeIf { it.isNotBlank() },
                typographyDirection = d.optString("typographyDirection").takeIf { it.isNotBlank() },
                negativeSpace = d.optString("negativeSpace").takeIf { it.isNotBlank() },
                requiredAssets = d.optString("requiredAssets").takeIf { it.isNotBlank() },
                executionNotes = d.optString("executionNotes").takeIf { it.isNotBlank() }
            )
        }

        val videoOutput = json.optJSONObject("videoOutput")?.let { v ->
            VideoOutput(
                coreIdea = v.optString("coreIdea", emptyState),
                openingHook = v.optString("openingHook", emptyState),
                storyStructure = v.optString("storyStructure").takeIf { it.isNotBlank() },
                sceneSequence = v.optString("sceneSequence").takeIf { it.isNotBlank() },
                shotSuggestions = v.optString("shotSuggestions").takeIf { it.isNotBlank() },
                cameraLanguage = v.optString("cameraLanguage").takeIf { it.isNotBlank() },
                motionDirection = v.optString("motionDirection").takeIf { it.isNotBlank() },
                transitionDirection = v.optString("transitionDirection").takeIf { it.isNotBlank() },
                soundDesign = v.optString("soundDesign").takeIf { it.isNotBlank() },
                duration = v.optString("duration").takeIf { it.isNotBlank() }
            )
        }

        val contentOutput = json.optJSONObject("contentOutput")?.let { c ->
            ContentOutput(
                contentAngle = c.optString("contentAngle", emptyState),
                mainIdea = c.optString("mainIdea", emptyState),
                hook = c.optString("hook", emptyState),
                storyDirection = c.optString("storyDirection").takeIf { it.isNotBlank() },
                captionDirection = c.optString("captionDirection").takeIf { it.isNotBlank() },
                cta = c.optString("cta").takeIf { it.isNotBlank() },
                reelConcept = c.optString("reelConcept").takeIf { it.isNotBlank() }
            )
        }

        val accountOutput = json.optJSONObject("accountOutput")?.let { a ->
            val mList = mutableListOf<String>()
            a.optJSONArray("missingInfo")?.let { arr ->
                for (i in 0 until arr.length()) mList.add(arr.getString(i))
            }
            val qList = mutableListOf<String>()
            a.optJSONArray("questionsForClient")?.let { arr ->
                for (i in 0 until arr.length()) qList.add(arr.getString(i))
            }
            val rList = mutableListOf<String>()
            a.optJSONArray("risksAndAmbiguities")?.let { arr ->
                for (i in 0 until arr.length()) rList.add(arr.getString(i))
            }
            AccountOutput(
                whatClientWants = a.optString("whatClientWants", emptyState),
                missingInfo = mList,
                questionsForClient = qList,
                deliverablesList = a.optString("deliverablesList", emptyState),
                risksAndAmbiguities = rList
            )
        }

        val creativeIdeas = mutableListOf<CreativeIdea>()
        json.optJSONArray("creativeIdeas")?.let { arr ->
            for (i in 0 until arr.length()) {
                val item = arr.getJSONObject(i)
                val title = item.optString("title").takeIf { it.isNotBlank() } ?: "اتجاه إبداعي ${i + 1}"
                val coreIdea = item.optString("coreIdea").ifBlank { item.optString("oneLiner", emptyState) }
                val insight = item.optString("strategicInsight").ifBlank { item.optString("creativeThinking", emptyState) }
                val intendedMessage = item.optString("intendedMessage").takeIf { it.isNotBlank() } ?: "إيصال قيمة المنتج الحقيقية بوضوح."
                val visualExec = item.optString("visualExecution").ifBlank { item.optString("visualDirection", emptyState) }

                creativeIdeas.add(
                    CreativeIdea(
                        title = title,
                        coreIdea = coreIdea,
                        strategicInsight = insight,
                        intendedMessage = intendedMessage,
                        visualExecution = visualExec,
                        composition = item.optString("composition").takeIf { it.isNotBlank() },
                        heroElement = item.optString("heroElement").takeIf { it.isNotBlank() },
                        keyDetails = item.optString("keyDetails").takeIf { it.isNotBlank() },
                        whatToAvoid = item.optString("whatToAvoid").takeIf { it.isNotBlank() },
                        executionNotes = item.optString("executionNotes").takeIf { it.isNotBlank() }
                    )
                )
            }
        }

        val execGuidance = json.optString("executionGuidance").takeIf { it.isNotBlank() }
            ?: "خلّي المنتج يحتل الجزء الأكبر والأوضح من الكادر، وضع الرسالة الأساسية في المساحة المقابلة له بتوازن بصري نظيف يقود العين من ميزة المنتج إلى زر الدعوة لاتخاذ إجراء مباشرة."

        return CreativeAnalysisResult(
            analysisId = analysisId,
            briefText = rawText,
            timestamp = System.currentTimeMillis(),
            targetTeam = targetTeam,
            thinkingMode = thinkingMode,
            realCommunicationChallenge = realChallenge,
            creativeOpportunity = creativeOpportunity,
            understanding = ClientUnderstanding(summary, stated, suggestions),
            actionableBrief = brief,
            missingEssentials = missing,
            designOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.DESIGN) designOutput else null,
            videoOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.VIDEO) videoOutput else null,
            contentOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.CONTENT) contentOutput else null,
            accountOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.ACCOUNT) accountOutput else null,
            creativeIdeas = creativeIdeas,
            executionGuidance = execGuidance
        )
    }

    private fun serializeAnalysisResult(result: CreativeAnalysisResult): String {
        val root = JSONObject()
        root.put("analysisId", result.analysisId)
        root.put("briefText", result.briefText)
        root.put("timestamp", result.timestamp)
        root.put("targetTeam", result.targetTeam.name)
        root.put("thinkingMode", result.thinkingMode.name)
        root.put("realCommunicationChallenge", result.realCommunicationChallenge)
        root.put("creativeOpportunity", result.creativeOpportunity)

        val under = JSONObject()
        under.put("summary", result.understanding.summary)
        val stArr = JSONArray()
        result.understanding.clientStated.forEach { stArr.put(it) }
        under.put("clientStated", stArr)
        val suArr = JSONArray()
        result.understanding.creativeSuggestion.forEach { suArr.put(it) }
        under.put("creativeSuggestion", suArr)
        root.put("understanding", under)

        val briefObj = JSONObject()
        result.actionableBrief.objective?.let { briefObj.put("objective", it) }
        result.actionableBrief.audience?.let { briefObj.put("audience", it) }
        result.actionableBrief.productOrService?.let { briefObj.put("productOrService", it) }
        result.actionableBrief.offer?.let { briefObj.put("offer", it) }
        result.actionableBrief.platform?.let { briefObj.put("platform", it) }
        result.actionableBrief.deliverables?.let { briefObj.put("deliverables", it) }
        result.actionableBrief.tone?.let { briefObj.put("tone", it) }
        result.actionableBrief.constraints?.let { briefObj.put("constraints", it) }
        root.put("actionableBrief", briefObj)

        val missArr = JSONArray()
        result.missingEssentials.forEach { missArr.put(it) }
        root.put("missingEssentials", missArr)

        result.designOutput?.let { d ->
            val obj = JSONObject()
            obj.put("creativeConcept", d.creativeConcept)
            obj.put("visualDirection", d.visualDirection)
            d.composition?.let { obj.put("composition", it) }
            d.heroElement?.let { obj.put("heroElement", it) }
            d.lighting?.let { obj.put("lighting", it) }
            d.colorDirection?.let { obj.put("colorDirection", it) }
            d.typographyDirection?.let { obj.put("typographyDirection", it) }
            d.negativeSpace?.let { obj.put("negativeSpace", it) }
            d.requiredAssets?.let { obj.put("requiredAssets", it) }
            d.executionNotes?.let { obj.put("executionNotes", it) }
            root.put("designOutput", obj)
        }

        result.videoOutput?.let { v ->
            val obj = JSONObject()
            obj.put("coreIdea", v.coreIdea)
            obj.put("openingHook", v.openingHook)
            v.storyStructure?.let { obj.put("storyStructure", it) }
            v.sceneSequence?.let { obj.put("sceneSequence", it) }
            v.shotSuggestions?.let { obj.put("shotSuggestions", it) }
            v.cameraLanguage?.let { obj.put("cameraLanguage", it) }
            v.motionDirection?.let { obj.put("motionDirection", it) }
            v.transitionDirection?.let { obj.put("transitionDirection", it) }
            v.soundDesign?.let { obj.put("soundDesign", it) }
            v.duration?.let { obj.put("duration", it) }
            root.put("videoOutput", obj)
        }

        result.contentOutput?.let { c ->
            val obj = JSONObject()
            obj.put("contentAngle", c.contentAngle)
            obj.put("mainIdea", c.mainIdea)
            obj.put("hook", c.hook)
            c.storyDirection?.let { obj.put("storyDirection", it) }
            c.captionDirection?.let { obj.put("captionDirection", it) }
            c.cta?.let { obj.put("cta", it) }
            c.reelConcept?.let { obj.put("reelConcept", it) }
            root.put("contentOutput", obj)
        }

        result.accountOutput?.let { a ->
            val obj = JSONObject()
            obj.put("whatClientWants", a.whatClientWants)
            val mArr = JSONArray()
            a.missingInfo.forEach { mArr.put(it) }
            obj.put("missingInfo", mArr)
            val qArr = JSONArray()
            a.questionsForClient.forEach { qArr.put(it) }
            obj.put("questionsForClient", qArr)
            obj.put("deliverablesList", a.deliverablesList)
            val rArr = JSONArray()
            a.risksAndAmbiguities.forEach { rArr.put(it) }
            obj.put("risksAndAmbiguities", rArr)
            root.put("accountOutput", obj)
        }

        val ideasArr = JSONArray()
        result.creativeIdeas.forEach { idea ->
            val iObj = JSONObject()
            iObj.put("title", idea.title)
            iObj.put("coreIdea", idea.coreIdea)
            iObj.put("strategicInsight", idea.strategicInsight)
            iObj.put("intendedMessage", idea.intendedMessage)
            iObj.put("visualExecution", idea.visualExecution)
            idea.composition?.let { iObj.put("composition", it) }
            idea.heroElement?.let { iObj.put("heroElement", it) }
            idea.keyDetails?.let { iObj.put("keyDetails", it) }
            idea.whatToAvoid?.let { iObj.put("whatToAvoid", it) }
            idea.executionNotes?.let { iObj.put("executionNotes", it) }
            ideasArr.put(iObj)
        }
        root.put("creativeIdeas", ideasArr)
        result.executionGuidance?.let { root.put("executionGuidance", it) }

        return root.toString()
    }

    fun deserializeAnalysisResult(jsonStr: String, targetTeam: TargetTeam, thinkingMode: ThinkingMode): CreativeAnalysisResult? {
        return try {
            val json = JSONObject(jsonStr)
            val analysisId = json.optString("analysisId", UUID.randomUUID().toString())
            val briefText = json.optString("briefText", "")
            val parsedTeam = try {
                TargetTeam.valueOf(json.optString("targetTeam", targetTeam.name))
            } catch (e: Exception) {
                targetTeam
            }
            val parsedMode = try {
                ThinkingMode.valueOf(json.optString("thinkingMode", thinkingMode.name))
            } catch (e: Exception) {
                thinkingMode
            }
            parseAnalysisJson(json, parsedTeam, parsedMode, briefText, analysisId)
        } catch (e: Exception) {
            Log.e(TAG, "Failed deserializing analysis result", e)
            null
        }
    }
}
