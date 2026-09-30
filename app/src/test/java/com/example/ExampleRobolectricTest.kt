package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.AgencyDiagnosticEngine
import com.example.data.local.AppDatabase
import com.example.data.local.BriefEntity
import com.example.data.model.TargetTeam
import com.example.data.model.ThinkingMode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("طبيب الـ Brief", appName)
  }

  @Test
  fun `database schema and operations work cleanly`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = AppDatabase.getInstance(context)
    val dao = db.briefDao()
    val entity = BriefEntity(
      title = "Test Brief",
      rawText = "محتاجين حملة إعلانية لمشروع جديد",
      targetTeam = "ALL",
      thinkingMode = "CREATIVE",
      resultJson = "{}"
    )
    val id = dao.insertBrief(entity)
    assertTrue(id > 0)
    val loaded = dao.getBriefById(id)
    assertNotNull(loaded)
    assertEquals("Test Brief", loaded?.title)
  }

  @Test
  fun `sequential analyses have strict client data isolation`() {
    val briefA = "عايزين حملة لمطعم برجر جديد في رمضان."
    val briefB = "حملة إطلاق لمشروع عقاري فاخر وفلل في الساحل."
    val briefC = "حملة تسويق لتطبيق تكنولوجي جديد للمهام اليومية."

    // 1. Analyze Brief A (Food)
    val resultA1 = AgencyDiagnosticEngine.analyzeBrief(briefA, TargetTeam.ALL, ThinkingMode.CREATIVE)
    assertNotNull(resultA1)
    assertTrue(resultA1.realCommunicationChallenge.contains("المطاعم") || resultA1.realCommunicationChallenge.contains("أكل"))
    assertTrue(resultA1.creativeOpportunity.contains("شهية") || resultA1.creativeOpportunity.contains("الطعم") || resultA1.creativeOpportunity.contains("التجربة"))

    // 2. Analyze Brief B (Real Estate)
    val resultB = AgencyDiagnosticEngine.analyzeBrief(briefB, TargetTeam.ALL, ThinkingMode.CREATIVE)
    assertNotNull(resultB)
    assertNotEquals(resultA1.analysisId, resultB.analysisId)
    // CRITICAL: Brief B must NOT contain food/burger content
    assertFalse(resultB.understanding.summary.contains("برجر"))
    assertFalse(resultB.realCommunicationChallenge.contains("برجر"))
    assertTrue(resultB.realCommunicationChallenge.contains("عقاري") || resultB.realCommunicationChallenge.contains("المدينة"))
    assertTrue(resultB.creativeOpportunity.contains("ساكنيها") || resultB.creativeOpportunity.contains("المعيشة") || resultB.creativeOpportunity.contains("الهدوء"))

    // Check creative ideas of Brief B
    resultB.creativeIdeas.forEach { idea ->
      assertFalse(idea.title.contains("برجر"))
      assertFalse(idea.coreIdea.contains("برجر"))
      assertFalse(idea.visualExecution.contains("برجر"))
      assertFalse(idea.strategicInsight.contains("برجر"))
    }

    // 3. Analyze Brief C (Tech App)
    val resultC = AgencyDiagnosticEngine.analyzeBrief(briefC, TargetTeam.ALL, ThinkingMode.CREATIVE)
    assertNotNull(resultC)
    assertNotEquals(resultB.analysisId, resultC.analysisId)
    // CRITICAL: Brief C must NOT contain real estate or burger
    assertFalse(resultC.understanding.summary.contains("عقاري"))
    assertFalse(resultC.understanding.summary.contains("برجر"))
    assertTrue(resultC.realCommunicationChallenge.contains("تطبيق") || resultC.realCommunicationChallenge.contains("تحميل"))

    // 4. Test A -> B -> A again
    val resultA2 = AgencyDiagnosticEngine.analyzeBrief(briefA, TargetTeam.ALL, ThinkingMode.CREATIVE)
    assertNotNull(resultA2)
    assertNotEquals(resultA1.analysisId, resultA2.analysisId)
    assertFalse(resultA2.understanding.summary.contains("عقاري"))
    assertFalse(resultA2.understanding.summary.contains("تطبيق"))
  }

  @Test
  fun `three unrelated briefs have unique conceptual titles and evidence-based ideas`() {
    val briefFood = "مطعم برجر جديد عايز حملة شبابية سريعة على تيك توك."
    val briefRealEstate = "مشروع فلل ساحلية فاخرة للبيع في الساحل الشمالي."
    val briefApp = "ابلكيشن جديد لإدارة وتنظيم مصاريف وميزانية البيت."

    val resFood = AgencyDiagnosticEngine.analyzeBrief(briefFood, TargetTeam.ALL, ThinkingMode.CREATIVE)
    val resEstate = AgencyDiagnosticEngine.analyzeBrief(briefRealEstate, TargetTeam.ALL, ThinkingMode.CREATIVE)
    val resApp = AgencyDiagnosticEngine.analyzeBrief(briefApp, TargetTeam.ALL, ThinkingMode.CREATIVE)

    // Verification 1: No generic cliché titles like "Freshness Unveiled"
    listOf(resFood, resEstate, resApp).forEach { res ->
      res.creativeIdeas.forEach { idea ->
        assertFalse(idea.title.contains("Freshness Unveiled", ignoreCase = true))
        assertFalse(idea.title.contains("Beyond Expectations", ignoreCase = true))
        assertFalse(idea.title.contains("Pure Experience", ignoreCase = true))
        assertTrue(idea.title.isNotBlank())
        assertTrue(idea.coreIdea.isNotBlank())
        assertTrue(idea.strategicInsight.isNotBlank())
        assertTrue(idea.visualExecution.isNotBlank())
      }
    }

    // Verification 2: Execution guidance is actionable and distinct per industry
    assertTrue(resFood.executionGuidance!!.contains("وجبة") || resFood.executionGuidance!!.contains("Macro") || resFood.executionGuidance!!.contains("أول ثانيتين"))
    assertTrue(resEstate.executionGuidance!!.contains("مشهد") || resEstate.executionGuidance!!.contains("فخامة") || resEstate.executionGuidance!!.contains("أسعار"))
    assertTrue(resApp.executionGuidance!!.contains("المشكلة") || resApp.executionGuidance!!.contains("شاشة") || resApp.executionGuidance!!.contains("تطبيق"))
  }

  @Test
  fun `missing fields show neutral empty state and never previous client data`() {
    val sparseBrief = "حملة إعلانية سريعة."
    val result = AgencyDiagnosticEngine.analyzeBrief(sparseBrief, TargetTeam.ALL, ThinkingMode.PRACTICAL)

    assertNotNull(result)
    assertTrue(result.missingEssentials.isNotEmpty())
    assertFalse(result.understanding.summary.contains("كافيه"))
    assertFalse(result.understanding.summary.contains("مخبوزات"))
    assertFalse(result.understanding.summary.contains("bottle"))
    assertFalse(result.understanding.summary.contains("سيروم"))
  }
}
