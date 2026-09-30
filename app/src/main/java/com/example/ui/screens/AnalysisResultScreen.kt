package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

/**
 * Desktop-First Strategic Agency Document Screen.
 * Dual-column editorial layout on desktop, providing a wide canvas for in-depth strategic analysis,
 * evidence-based creative directions, and actionable execution guidance.
 */
@Composable
fun AnalysisResultScreen(
    result: CreativeAnalysisResult,
    appLanguage: AppLanguage = AppLanguage.ARABIC,
    onBackToInput: () -> Unit,
    onStartNewBrief: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val isArabic = appLanguage == AppLanguage.ARABIC

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDark950)
            .testTag("analysis_result_screen")
    ) {
        val isDesktop = maxWidth >= 920.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = if (isDesktop) 44.dp else 20.dp,
                    vertical = if (isDesktop) 28.dp else 16.dp
                )
        ) {
            // Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = onBackToInput,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("back_to_input_button")
                    ) {
                        Icon(
                            imageVector = if (isArabic) Icons.AutoMirrored.Filled.ArrowForward else Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isArabic) "رجوع" else "Back",
                            tint = SoftCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "تعديل البريف" else "Edit Brief",
                            color = SoftCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onStartNewBrief,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, StudioDark700),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = StudioDark900),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("result_new_brief_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = ElectricPurpleLight, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isArabic) "بريف جديد" else "New Brief",
                            color = StudioDark100,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                FilledTonalButton(
                    onClick = {
                        val fullDoc = formatCompleteStrategyDoc(result, isArabic)
                        clipboard.setText(AnnotatedString(fullDoc))
                        Toast.makeText(
                            context,
                            if (isArabic) "تم نسخ وثيقة الاستراتيجية الإبداعية بالكامل!" else "Full strategy document copied!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = StudioDark850,
                        contentColor = StudioDark100
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("copy_all_button")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "نسخ الاستراتيجية" else "Copy Strategy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Strategic Document Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                StudioDark900,
                                StudioDark850
                            )
                        )
                    )
                    .border(1.dp, StudioDark800, RoundedCornerShape(16.dp))
                    .padding(if (isDesktop) 28.dp else 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricPurple.copy(alpha = 0.2f))
                                .border(1.dp, ElectricPurple.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isArabic) "تشخيص استراتيجي إبداعي" else "STRATEGIC DIAGNOSIS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricPurpleLight,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StudioDark800)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${if (isArabic) "الفريق: " else "Team: "}${if (isArabic) result.targetTeam.labelAr else result.targetTeam.labelEn}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftCyan
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StudioDark800)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isArabic) result.thinkingMode.labelAr else result.thinkingMode.labelEn,
                                    fontSize = 12.sp,
                                    color = StudioDark300
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isArabic) "التشخيص الإبداعي وخارطة الطريق" else "Brief Diagnosis & Creative Strategy",
                        fontSize = if (isDesktop) 32.sp else 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudioDark50
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isArabic) {
                            "تم تفكيك التحدي الاتصالي الحقيقي، وتحديد الفرصة الإبداعية، وصياغة اتجاهات قائمة على الأدلة بدون قوالب جاهزة."
                        } else {
                            "The communication challenge was diagnosed, the creative territory identified, and evidence-based directions generated."
                        },
                        fontSize = 14.sp,
                        color = StudioDark400,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main Content Area: Desktop Two-Column Layout vs Mobile Stacked Layout
            if (isDesktop) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Column: Strategic Understanding & Gaps (42% width)
                    Column(
                        modifier = Modifier
                            .weight(0.42f)
                            .fillMaxWidth()
                    ) {
                        // Challenge & Opportunity Card
                        StrategicChallengeCard(
                            challenge = result.realCommunicationChallenge,
                            opportunity = result.creativeOpportunity,
                            isArabic = isArabic
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Client Understanding (Fact vs Recommendation)
                        ClientUnderstandingSection(result = result, isArabic = isArabic)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Actionable Brief
                        ActionableBriefSection(result = result, isArabic = isArabic)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Missing Essentials Audit
                        MissingEssentialsSection(result = result, isArabic = isArabic)
                    }

                    // Right Column: Creative Directions & Team Deliverables (58% width)
                    Column(
                        modifier = Modifier
                            .weight(0.58f)
                            .fillMaxWidth()
                    ) {
                        // Creative Directions
                        CreativeDirectionsSection(result = result, isArabic = isArabic)

                        Spacer(modifier = Modifier.height(28.dp))

                        // Team-Specific Deliverables
                        TeamDeliverablesSection(result = result, isArabic = isArabic)

                        Spacer(modifier = Modifier.height(28.dp))

                        // Execution Guidance
                        ExecutionGuidanceSection(result = result, isArabic = isArabic)
                    }
                }
            } else {
                // Mobile Sequential Stack
                Column(modifier = Modifier.fillMaxWidth()) {
                    StrategicChallengeCard(
                        challenge = result.realCommunicationChallenge,
                        opportunity = result.creativeOpportunity,
                        isArabic = isArabic
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    ClientUnderstandingSection(result = result, isArabic = isArabic)

                    Spacer(modifier = Modifier.height(24.dp))
                    ActionableBriefSection(result = result, isArabic = isArabic)

                    Spacer(modifier = Modifier.height(24.dp))
                    MissingEssentialsSection(result = result, isArabic = isArabic)

                    Spacer(modifier = Modifier.height(28.dp))
                    CreativeDirectionsSection(result = result, isArabic = isArabic)

                    Spacer(modifier = Modifier.height(28.dp))
                    TeamDeliverablesSection(result = result, isArabic = isArabic)

                    Spacer(modifier = Modifier.height(28.dp))
                    ExecutionGuidanceSection(result = result, isArabic = isArabic)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onStartNewBrief,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioDark850),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    modifier = Modifier.testTag("bottom_new_brief_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isArabic) "بدء بريف عميل جديد" else "Start New Client Brief",
                        fontSize = 13.sp,
                        color = StudioDark50,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// -------------------------------------------------------------
// Strategic Modular Sections
// -------------------------------------------------------------

@Composable
private fun StrategicChallengeCard(
    challenge: String,
    opportunity: String,
    isArabic: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SoftCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDark900)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isArabic) "التحدي الإبداعي والفرصة الاستراتيجية" else "Creative Challenge & Opportunity",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCyan
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real Communication Challenge
            Text(
                text = if (isArabic) "التحدي الاتصالي الفعلي:" else "Real Communication Challenge:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = StudioDark400
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = challenge.ifBlank { if (isArabic) "البريف يحتاج زاوية اتصالية واضحة تجعل الجمهور يتوقف ويلتفت للرسالة." else "A distinct communication angle is needed." },
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = StudioDark100,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = StudioDark800, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Creative Opportunity
            Text(
                text = if (isArabic) "الفرصة الإبداعية:" else "Creative Opportunity:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricPurpleLight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = opportunity.ifBlank { if (isArabic) "ربط المنتج بحاجة يومية ملموسة للجمهور." else "Connect the product with an immediate audience need." },
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = StudioDark100,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ClientUnderstandingSection(
    result: CreativeAnalysisResult,
    isArabic: Boolean
) {
    Column {
        EditorialSectionHeader(
            sectionNumber = "01",
            titleAr = "فهم كلام العميل والملخص",
            titleEn = "Client Understanding & Facts",
            isArabic = isArabic,
            accentColor = SoftCyan
        )

        Spacer(modifier = Modifier.height(12.dp))

        EditorialCard(accentColor = SoftCyan) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = if (isArabic) "ملخص ما يطلبه العميل:" else "Client Request Summary:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = result.understanding.summary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = StudioDark100,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = StudioDark800, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Client Stated Facts vs Agency Suggestions
                Text(
                    text = if (isArabic) "ما قاله العميل صراحة (FACT):" else "Explicitly Stated Facts:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioDark300
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (result.understanding.clientStated.isEmpty()) {
                    Text(
                        text = if (isArabic) "مفيش معلومات كفاية في البريف." else "No explicit points stated.",
                        fontSize = 12.sp,
                        color = StudioDark500
                    )
                } else {
                    result.understanding.clientStated.forEach { point ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("• ", color = SoftCyan, fontSize = 13.sp)
                            Text(point, fontSize = 13.sp, color = StudioDark200, lineHeight = 19.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "استنتاجات وتوصيات الوكالة:" else "Agency Strategic Recommendations:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricPurpleLight
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (result.understanding.creativeSuggestion.isEmpty()) {
                    Text(
                        text = if (isArabic) "مفيش معلومات كفاية في البريف." else "No recommendations.",
                        fontSize = 12.sp,
                        color = StudioDark500
                    )
                } else {
                    result.understanding.creativeSuggestion.forEach { point ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("→ ", color = ElectricPurple, fontSize = 13.sp)
                            Text(point, fontSize = 13.sp, color = StudioDark200, lineHeight = 19.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionableBriefSection(
    result: CreativeAnalysisResult,
    isArabic: Boolean
) {
    Column {
        EditorialSectionHeader(
            sectionNumber = "02",
            titleAr = "الـ Brief الاستراتيجي الجاهز",
            titleEn = "Actionable Strategic Brief",
            isArabic = isArabic,
            accentColor = ElectricPurple
        )

        Spacer(modifier = Modifier.height(12.dp))

        EditorialCard(accentColor = ElectricPurple) {
            Column(modifier = Modifier.padding(18.dp)) {
                val items = listOf(
                    Pair(if (isArabic) "الهدف" else "Objective", result.actionableBrief.objective),
                    Pair(if (isArabic) "الجمهور المستهدف" else "Audience", result.actionableBrief.audience),
                    Pair(if (isArabic) "المنتج / الخدمة" else "Product/Service", result.actionableBrief.productOrService),
                    Pair(if (isArabic) "العرض الترويجي" else "Offer", result.actionableBrief.offer),
                    Pair(if (isArabic) "المنصات" else "Platform", result.actionableBrief.platform),
                    Pair(if (isArabic) "المخرجات" else "Deliverables", result.actionableBrief.deliverables),
                    Pair(if (isArabic) "النبرة" else "Tone", result.actionableBrief.tone),
                    Pair(if (isArabic) "القيود" else "Constraints", result.actionableBrief.constraints)
                )

                items.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowItems.forEach { (label, value) ->
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioDark400
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = value ?: (if (isArabic) "مفيش معلومات كفاية في البريف." else "Not specified."),
                                    fontSize = 13.sp,
                                    color = if (value != null && !value.contains("مفيش معلومات كفاية")) StudioDark100 else StudioDark500,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MissingEssentialsSection(
    result: CreativeAnalysisResult,
    isArabic: Boolean
) {
    Column {
        EditorialSectionHeader(
            sectionNumber = "03",
            titleAr = "النواقص الجوهرية ومخاطر التنفيذ",
            titleEn = "Missing Essentials & Risk Audit",
            isArabic = isArabic,
            accentColor = StudioWarning
        )

        Spacer(modifier = Modifier.height(12.dp))

        EditorialCard(accentColor = StudioWarning) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = if (isArabic) "إيه اللي ناقص قبل ما نبدأ شغل؟" else "What is missing before production?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioWarning
                )
                Spacer(modifier = Modifier.height(6.dp))

                if (result.missingEssentials.isEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StudioSuccess, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "البريف مكتمل ولا توجد نواقص تعطل العمل." else "Brief has no operational blockers.",
                            fontSize = 13.sp,
                            color = StudioSuccess
                        )
                    }
                } else {
                    result.missingEssentials.forEach { item ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("! ", color = StudioWarning, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(item, fontSize = 13.sp, color = StudioDark200, lineHeight = 19.sp)
                        }
                    }
                }

                // Account questions if available
                result.accountOutput?.let { account ->
                    if (account.questionsForClient.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = StudioDark800, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isArabic) "أسئلة حاسمة يجب توجيهها للعميل الآن:" else "Questions to confirm with client:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        account.questionsForClient.forEach { q ->
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                Text("? ", color = SoftCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(q, fontSize = 12.sp, color = StudioDark300)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreativeDirectionsSection(
    result: CreativeAnalysisResult,
    isArabic: Boolean
) {
    Column {
        EditorialSectionHeader(
            sectionNumber = "04",
            titleAr = "الاتجاهات الإبداعية للحملة (قائمة على الأدلة)",
            titleEn = "Evidence-Based Creative Directions",
            isArabic = isArabic,
            accentColor = ElectricPurple
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (result.creativeIdeas.isEmpty()) {
            EditorialCard(accentColor = StudioDark700) {
                Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isArabic) "مفيش معلومات كفاية في البريف لتوليد اتجاهات إبداعية قوية." else "Insufficient details to generate creative directions.",
                        fontSize = 13.sp,
                        color = StudioDark500
                    )
                }
            }
        } else {
            result.creativeIdeas.forEachIndexed { index, idea ->
                EvidenceBasedCreativeDirectionCard(
                    idea = idea,
                    index = index,
                    isArabic = isArabic
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun EvidenceBasedCreativeDirectionCard(
    idea: CreativeIdea,
    index: Int,
    isArabic: Boolean
) {
    val accentColor = when (index % 3) {
        0 -> ElectricPurple
        1 -> SoftCyan
        else -> StudioIndigo
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDark900)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Direction Badge & Index
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${if (isArabic) "الاتجاه الإبداعي" else "DIRECTION"} 0${index + 1}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Meaningful Conceptual Title
            Text(
                text = idea.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = StudioDark50
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = StudioDark800, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // 1. الفكرة
            DirectionPointBlock(
                label = if (isArabic) "١. الفكرة الإبداعية:" else "1. Creative Idea:",
                content = idea.coreIdea,
                accentColor = accentColor
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. ليه الفكرة دي؟
            DirectionPointBlock(
                label = if (isArabic) "٢. ليه الفكرة دي؟ (الاستنتاج الاستراتيجي):" else "2. Why this idea? (Strategic Insight):",
                content = idea.strategicInsight,
                accentColor = SoftCyan
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. الرسالة
            DirectionPointBlock(
                label = if (isArabic) "٣. الرسالة للجمهور:" else "3. Intended Message:",
                content = idea.intendedMessage,
                accentColor = StudioDark300
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. التنفيذ البصري
            DirectionPointBlock(
                label = if (isArabic) "٤. التنفيذ البصري:" else "4. Visual Execution:",
                content = idea.visualExecution,
                accentColor = StudioDark200
            )

            // 5 & 6. التكوين والعنصر البطل
            if (idea.composition != null || idea.heroElement != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    idea.composition?.let { comp ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "٥. التكوين وترتيب العناصر:" else "5. Composition:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioDark400
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = comp, fontSize = 13.sp, color = StudioDark200, lineHeight = 18.sp)
                        }
                    }
                    idea.heroElement?.let { hero ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "٦. العنصر البطل:" else "6. Visual Hero:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioDark400
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = hero, fontSize = 13.sp, color = StudioDark200, lineHeight = 18.sp)
                        }
                    }
                }
            }

            // 7. التفاصيل المهمة
            idea.keyDetails?.let { details ->
                Spacer(modifier = Modifier.height(10.dp))
                DirectionPointBlock(
                    label = if (isArabic) "٧. التفاصيل المميزة:" else "7. Distinctive Details:",
                    content = details,
                    accentColor = StudioDark300
                )
            }

            // 8. تجنب إيه
            idea.whatToAvoid?.let { avoid ->
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioWarning.copy(alpha = 0.1f))
                        .border(1.dp, StudioWarning.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = if (isArabic) "٨. تجنب إيه (ما يضعف الفكرة):" else "8. What to Avoid:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioWarning
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = avoid, fontSize = 12.sp, color = StudioDark200, lineHeight = 18.sp)
                    }
                }
            }

            // ملاحظات التنفيذ
            idea.executionNotes?.let { notes ->
                Spacer(modifier = Modifier.height(10.dp))
                DirectionPointBlock(
                    label = if (isArabic) "ملاحظات التنفيذ:" else "Execution Notes:",
                    content = notes,
                    accentColor = SoftCyan
                )
            }
        }
    }
}

@Composable
private fun DirectionPointBlock(
    label: String,
    content: String,
    accentColor: Color
) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = content,
            fontSize = 13.sp,
            color = StudioDark100,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun TeamDeliverablesSection(
    result: CreativeAnalysisResult,
    isArabic: Boolean
) {
    Column {
        EditorialSectionHeader(
            sectionNumber = "05",
            titleAr = "مخرجات الفريق المختار (${if (isArabic) result.targetTeam.labelAr else result.targetTeam.labelEn})",
            titleEn = "Team Deliverables (${result.targetTeam.name})",
            isArabic = isArabic,
            accentColor = StudioIndigo
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Design Output (NO FORCED FIELDS: Renders only non-null strategic fields)
        result.designOutput?.let { design ->
            TeamOutputCard(
                title = if (isArabic) "فريق التصميم والإخراج الفني" else "Design & Art Direction",
                icon = Icons.Default.Palette,
                accentColor = SoftCyan
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeliverableField(if (isArabic) "المفهوم البصري" else "Creative Concept", design.creativeConcept)
                    DeliverableField(if (isArabic) "الاتجاه البصري" else "Visual Direction", design.visualDirection)
                    design.composition?.let { DeliverableField(if (isArabic) "التكوين وترتيب العناصر" else "Composition", it) }
                    design.heroElement?.let { DeliverableField(if (isArabic) "العنصر الأساسي البطل" else "Hero Element", it) }
                    design.lighting?.let { DeliverableField(if (isArabic) "الإضاءة المدروسة" else "Lighting", it) }
                    design.colorDirection?.let { DeliverableField(if (isArabic) "توجيه الألوان" else "Color Palette", it) }
                    design.typographyDirection?.let { DeliverableField(if (isArabic) "التايبوجرافي" else "Typography", it) }
                    design.negativeSpace?.let { DeliverableField(if (isArabic) "المساحة السلبية" else "Negative Space", it) }
                    design.requiredAssets?.let { DeliverableField(if (isArabic) "الملحقات المطلوبة" else "Required Assets", it) }
                    design.executionNotes?.let { DeliverableField(if (isArabic) "ملاحظات التنفيذ" else "Execution Notes", it) }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Video Output
        result.videoOutput?.let { video ->
            TeamOutputCard(
                title = if (isArabic) "فريق الفيديو والموشن" else "Video & Motion",
                icon = Icons.Default.Videocam,
                accentColor = Color(0xFFFF5555)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeliverableField(if (isArabic) "فكرة الفيديو الأساسية" else "Core Idea", video.coreIdea)
                    DeliverableField(if (isArabic) "افتتاحية الـ Hook" else "Opening Hook", video.openingHook)
                    video.storyStructure?.let { DeliverableField(if (isArabic) "هيكل السرد" else "Story Structure", it) }
                    video.sceneSequence?.let { DeliverableField(if (isArabic) "تسلسل المشاهد" else "Scene Sequence", it) }
                    video.shotSuggestions?.let { DeliverableField(if (isArabic) "اقتراحات اللقطات" else "Shots", it) }
                    video.cameraLanguage?.let { DeliverableField(if (isArabic) "لغة الكاميرا" else "Camera Language", it) }
                    video.motionDirection?.let { DeliverableField(if (isArabic) "حركة الكاميرا والانتقالات" else "Motion Direction", it) }
                    video.soundDesign?.let { DeliverableField(if (isArabic) "تصميم الصوت والمؤثرات" else "Sound Design", it) }
                    video.duration?.let { DeliverableField(if (isArabic) "المدة المقترحة" else "Duration", it) }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Content Output
        result.contentOutput?.let { content ->
            TeamOutputCard(
                title = if (isArabic) "فريق المحتوى وصياغة الرسائل" else "Content & Copywriting",
                icon = Icons.Default.Description,
                accentColor = ElectricPurple
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeliverableField(if (isArabic) "زاوية المحتوى" else "Content Angle", content.contentAngle)
                    DeliverableField(if (isArabic) "الفكرة الرئيسية" else "Main Idea", content.mainIdea)
                    DeliverableField(if (isArabic) "جملة الـ Hook" else "Hook", content.hook)
                    content.storyDirection?.let { DeliverableField(if (isArabic) "اتجاه السرد" else "Story Direction", it) }
                    content.captionDirection?.let { DeliverableField(if (isArabic) "توجيه الكابشن" else "Caption Direction", it) }
                    content.cta?.let { DeliverableField(if (isArabic) "الدعوة للتفاعل (CTA)" else "CTA", it) }
                    content.reelConcept?.let { DeliverableField(if (isArabic) "فكرة الريل" else "Reel Concept", it) }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Account Output
        result.accountOutput?.let { account ->
            TeamOutputCard(
                title = if (isArabic) "فريق إدارة الحسابات" else "Account Management",
                icon = Icons.Default.BusinessCenter,
                accentColor = StudioWarning
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeliverableField(if (isArabic) "ملخص متطلبات العميل" else "Client Summary", account.whatClientWants)
                    DeliverableField(if (isArabic) "قائمة المخرجات للتسليم" else "Deliverables List", account.deliverablesList)
                    if (account.risksAndAmbiguities.isNotEmpty()) {
                        DeliverableField(
                            if (isArabic) "مخاطر محتملة يجب الانتباه لها" else "Risks & Ambiguities",
                            account.risksAndAmbiguities.joinToString("\n• ", prefix = "• ")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExecutionGuidanceSection(
    result: CreativeAnalysisResult,
    isArabic: Boolean
) {
    Column {
        EditorialSectionHeader(
            sectionNumber = "06",
            titleAr = "خارطة طريق التنفيذ العملي",
            titleEn = "Actionable Execution Guidance",
            isArabic = isArabic,
            accentColor = StudioSuccess
        )

        Spacer(modifier = Modifier.height(12.dp))

        EditorialCard(accentColor = StudioSuccess) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = result.executionGuidance ?: (if (isArabic)
                        "خلّي المنتج يحتل الجزء الأكبر والأوضح من الكادر، وضع الرسالة الأساسية في المساحة المقابلة له بتوازن بصري نظيف يقود العين من ميزة المنتج إلى زر الدعوة لاتخاذ إجراء مباشرة."
                    else
                        "Make the product prominent in the frame, positioning the primary message in the complementary space with balanced visual hierarchy pointing to the CTA."),
                    fontSize = 14.sp,
                    color = StudioDark100,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Subcomponents & Helpers
// -------------------------------------------------------------

@Composable
private fun EditorialSectionHeader(
    sectionNumber: String,
    titleAr: String,
    titleEn: String,
    isArabic: Boolean,
    accentColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f))
                .border(1.dp, accentColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = sectionNumber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = if (isArabic) titleAr else titleEn,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = StudioDark100
        )
    }
}

@Composable
private fun EditorialCard(
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDark900.copy(alpha = 0.75f))
    ) {
        content()
    }
}

@Composable
private fun TeamOutputCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDark900)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StudioDark50)
            }
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = StudioDark800, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun DeliverableField(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StudioDark400
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            color = StudioDark200,
            lineHeight = 19.sp
        )
    }
}

private fun formatCompleteStrategyDoc(result: CreativeAnalysisResult, isArabic: Boolean): String {
    return buildString {
        appendLine(if (isArabic) "# وثيقة الاستراتيجية الإبداعية — طبيب الـ Brief" else "# Executive Creative Strategy — AI Brief Doctor")
        appendLine("--------------------------------------------------")
        appendLine(if (isArabic) "الفريق المستهدف: ${result.targetTeam.labelAr} | النمط: ${result.thinkingMode.labelAr}" else "Team: ${result.targetTeam.name} | Mode: ${result.thinkingMode.name}")
        appendLine()
        appendLine(if (isArabic) "## التحدي الاتصالي والفرصة الإبداعية" else "## Challenge & Opportunity")
        appendLine(if (isArabic) "- التحدي الحقيقي: ${result.realCommunicationChallenge}" else "- Challenge: ${result.realCommunicationChallenge}")
        appendLine(if (isArabic) "- الفرصة الإبداعية: ${result.creativeOpportunity}" else "- Opportunity: ${result.creativeOpportunity}")
        appendLine()
        appendLine(if (isArabic) "## 01. فهم كلام العميل" else "## 01. Client Understanding")
        appendLine(result.understanding.summary)
        if (result.understanding.clientStated.isNotEmpty()) {
            appendLine()
            appendLine(if (isArabic) "ما قاله العميل صراحة (FACT):" else "Explicitly Stated Facts:")
            result.understanding.clientStated.forEach { appendLine("• $it") }
        }
        if (result.understanding.creativeSuggestion.isNotEmpty()) {
            appendLine()
            appendLine(if (isArabic) "استنتاجات وتوصيات الوكالة:" else "Agency Strategic Recommendations:")
            result.understanding.creativeSuggestion.forEach { appendLine("→ $it") }
        }
        appendLine()
        appendLine(if (isArabic) "## 02. الـ Brief الاستراتيجي الجاهز" else "## 02. Actionable Brief")
        result.actionableBrief.objective?.let { appendLine("- الهدف: $it") }
        result.actionableBrief.audience?.let { appendLine("- الجمهور: $it") }
        result.actionableBrief.productOrService?.let { appendLine("- المنتج: $it") }
        result.actionableBrief.offer?.let { appendLine("- العرض: $it") }
        result.actionableBrief.platform?.let { appendLine("- المنصات: $it") }
        result.actionableBrief.deliverables?.let { appendLine("- المخرجات: $it") }
        result.actionableBrief.tone?.let { appendLine("- النبرة: $it") }
        result.actionableBrief.constraints?.let { appendLine("- القيود: $it") }
        appendLine()
        appendLine(if (isArabic) "## 03. النواقص ومخاطر التنفيذ" else "## 03. Missing Essentials & Risks")
        if (result.missingEssentials.isEmpty()) {
            appendLine(if (isArabic) "لا توجد نواقص جوهرية تعطل العمل." else "No critical blockers.")
        } else {
            result.missingEssentials.forEach { appendLine("! $it") }
        }
        appendLine()
        appendLine(if (isArabic) "## 04. الاتجاهات الإبداعية للحملة" else "## 04. Creative Directions")
        result.creativeIdeas.forEachIndexed { i, idea ->
            appendLine("### ${idea.title}")
            appendLine("١. الفكرة: ${idea.coreIdea}")
            appendLine("٢. ليه الفكرة دي؟: ${idea.strategicInsight}")
            appendLine("٣. الرسالة: ${idea.intendedMessage}")
            appendLine("٤. التنفيذ البصري: ${idea.visualExecution}")
            idea.composition?.let { appendLine("٥. التكوين: $it") }
            idea.heroElement?.let { appendLine("٦. العنصر البطل: $it") }
            idea.keyDetails?.let { appendLine("٧. التفاصيل المميزة: $it") }
            idea.whatToAvoid?.let { appendLine("٨. تجنب إيه: $it") }
            idea.executionNotes?.let { appendLine("ملاحظات التنفيذ: $it") }
            appendLine()
        }
        appendLine(if (isArabic) "## 05. خارطة طريق التنفيذ" else "## 05. Actionable Execution Guidance")
        appendLine(result.executionGuidance ?: "")
    }
}
