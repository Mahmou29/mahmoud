package com.example

import com.example.data.api.AgencyDiagnosticEngine
import com.example.data.model.SampleData
import com.example.data.model.TargetTeam
import com.example.data.model.ThinkingMode
import org.junit.Assert.*
import org.junit.Test

class BriefDoctorDiagnosticTest {

    @Test
    fun testAllTeamsAnalysisWithEgyptianSlang() {
        val demo = SampleData.demoPrompts.first() // Coffee demo
        val result = AgencyDiagnosticEngine.analyzeBrief(
            rawText = demo.rawText,
            targetTeam = TargetTeam.ALL,
            thinkingMode = ThinkingMode.CREATIVE
        )

        // Verify Understanding
        assertTrue("Understanding summary must not be blank", result.understanding.summary.isNotBlank())
        assertTrue("Should extract client stated items", result.understanding.clientStated.isNotEmpty())
        assertTrue("Should produce agency creative suggestions", result.understanding.creativeSuggestion.isNotEmpty())

        // Verify Actionable Brief
        assertNotNull("Objective must be extracted", result.actionableBrief.objective)
        assertNotNull("Audience must be extracted", result.actionableBrief.audience)
        assertNotNull("Deliverables must be extracted", result.actionableBrief.deliverables)

        // For TargetTeam.ALL, all specialized team outputs should be populated
        assertNotNull("Design output must exist for ALL", result.designOutput)
        assertNotNull("Video output must exist for ALL", result.videoOutput)
        assertNotNull("Content output must exist for ALL", result.contentOutput)
        assertNotNull("Account output must exist for ALL", result.accountOutput)

        // Verify 3 Creative Ideas
        assertEquals("Must generate 3 strategic creative ideas", 3, result.creativeIdeas.size)
        result.creativeIdeas.forEach { idea ->
            assertTrue("Idea must have title", idea.title.isNotBlank())
            assertTrue("Idea must have oneLiner", idea.oneLiner.isNotBlank())
            assertTrue("Idea must have creativeThinking", idea.creativeThinking.isNotBlank())
            assertTrue("Idea must have visualDirection", idea.visualDirection.isNotBlank())
            assertTrue("Idea must have contentDirection", idea.contentDirection?.isNotBlank() == true)
        }
    }

    @Test
    fun testDesignSpecificTeamOutput() {
        val demo = SampleData.demoPrompts[1] // Skincare demo
        val result = AgencyDiagnosticEngine.analyzeBrief(
            rawText = demo.rawText,
            targetTeam = TargetTeam.DESIGN,
            thinkingMode = ThinkingMode.PRACTICAL
        )

        assertEquals(TargetTeam.DESIGN, result.targetTeam)
        assertNotNull("Design output must exist", result.designOutput)
        assertTrue("Creative concept must be defined", result.designOutput!!.creativeConcept.isNotBlank())
        assertTrue("Visual direction must be defined", result.designOutput!!.visualDirection.isNotBlank())
        assertTrue("Hero element must be defined", result.designOutput!!.heroElement.isNotBlank())
        // Video and Content outputs are omitted when targetTeam is DESIGN to avoid forcing unnecessary fields
        assertNull("Video output should be omitted for DESIGN team", result.videoOutput)
        assertNull("Content output should be omitted for DESIGN team", result.contentOutput)
    }

    @Test
    fun testVideoSpecificTeamOutput() {
        val demo = SampleData.demoPrompts[1] // Skincare demo
        val result = AgencyDiagnosticEngine.analyzeBrief(
            rawText = demo.rawText,
            targetTeam = TargetTeam.VIDEO,
            thinkingMode = ThinkingMode.BOLD
        )

        assertEquals(TargetTeam.VIDEO, result.targetTeam)
        assertNotNull("Video output must exist", result.videoOutput)
        assertTrue("Opening hook must be defined", result.videoOutput!!.openingHook.isNotBlank())
        assertTrue("Core idea must be defined", result.videoOutput!!.coreIdea.isNotBlank())
        assertTrue("Sound design must be defined", result.videoOutput!!.soundDesign.isNotBlank())
    }

    @Test
    fun testEnglishBriefUnderstanding() {
        val englishDemo = SampleData.demoPrompts.last() // Smart water bottle
        val result = AgencyDiagnosticEngine.analyzeBrief(
            rawText = englishDemo.rawText,
            targetTeam = TargetTeam.ALL,
            thinkingMode = ThinkingMode.CREATIVE
        )

        assertTrue(result.understanding.summary.isNotBlank())
        assertTrue(result.actionableBrief.deliverables?.contains("كاروسيل") == true ||
                   result.actionableBrief.deliverables?.contains("Carousel") == true ||
                   result.actionableBrief.deliverables?.contains("تصاميم") == true)
        assertTrue(result.missingEssentials.isNotEmpty())
    }
}
