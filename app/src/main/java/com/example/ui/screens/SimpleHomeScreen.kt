package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

/**
 * Desktop-First AI Creative Strategy Studio — Homepage & Workbench.
 * Fluid adaptive layout that leverages full desktop width with two-column editorial hierarchy,
 * expanding up to 1600dp on large monitors while providing a responsive mobile stack.
 */
@Composable
fun SimpleHomeScreen(
    rawText: String,
    selectedTeam: TargetTeam,
    thinkingMode: ThinkingMode,
    appLanguage: AppLanguage,
    isAnalyzing: Boolean,
    processingStepIndex: Int = 0,
    currentProcessingStage: String = "",
    onRawTextChange: (String) -> Unit,
    onTeamSelect: (TargetTeam) -> Unit,
    onThinkingModeSelect: (ThinkingMode) -> Unit,
    onToggleLanguage: () -> Unit,
    onAnalyzeClick: () -> Unit,
    onStartNewBrief: () -> Unit,
    onLoadDemo: (QuickDemoPrompt) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    isAiActive: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isArabic = appLanguage == AppLanguage.ARABIC

    // Subtle atmospheric ambient glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_glow")
    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_pulse"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDark950)
            .testTag("simple_home_screen")
    ) {
        val isDesktop = maxWidth >= 860.dp

        // Dynamic Atmospheric Ambient Light Areas
        Box(
            modifier = Modifier
                .size(500.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .blur(130.dp)
                .background(ElectricPurple.copy(alpha = ambientPulse * 0.4f), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(450.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 120.dp, y = 120.dp)
                .blur(140.dp)
                .background(SoftCyan.copy(alpha = ambientPulse * 0.3f), shape = CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = if (isDesktop) 44.dp else 20.dp,
                    vertical = if (isDesktop) 28.dp else 16.dp
                )
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(listOf(ElectricPurple.copy(alpha = 0.35f), SoftCyan.copy(alpha = 0.2f)))
                            )
                            .border(1.dp, ElectricPurple.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = "طبيب الـ Brief",
                            tint = SoftCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isArabic) "طبيب الـ Brief" else "AI Brief Doctor",
                                fontSize = if (isDesktop) 18.sp else 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioDark50
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StudioDark850)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isArabic) "استوديو استراتيجي" else "STRATEGY STUDIO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricPurpleLight,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }
                        Text(
                            text = if (isArabic) "منظومة التفكير الإبداعي الاستراتيجي للوكالات" else "Agency Creative Intelligence System",
                            fontSize = 12.sp,
                            color = StudioDark400
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // New Brief Button
                    OutlinedButton(
                        onClick = onStartNewBrief,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioDark700),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = StudioDark900.copy(alpha = 0.7f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("home_new_brief_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isArabic) "بريف جديد" else "New Brief",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark100
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Language Switcher Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StudioDark900)
                            .border(1.dp, StudioDark700, RoundedCornerShape(8.dp))
                            .clickable { onToggleLanguage() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("language_toggle_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isArabic) "EN" else "عربي",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark200
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onOpenHistory,
                        modifier = Modifier.size(38.dp).testTag("home_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = if (isArabic) "سجل البريفات" else "Brief History",
                            tint = StudioDark400,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.size(38.dp).testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = if (isArabic) "الإعدادات" else "Settings",
                            tint = StudioDark400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isDesktop) 40.dp else 24.dp))

            // Desktop-First Main Workbench
            if (isDesktop) {
                // Wide Desktop Layout (2 Columns)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Left Column: Headline + Brief Textarea + Quick Prompts (62% width)
                    Column(
                        modifier = Modifier
                            .weight(0.62f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = if (isArabic) "العميل محتاج إيه؟" else "What does the client need?",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = StudioDark50,
                            lineHeight = 48.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isArabic) {
                                "اكتب كلام العميل غير المرتب زي ما هو — المحرك هيحدد التحدي الاتصالي الحقيقي ويصيغ اتجاهات إبداعية قائمة على الأدلة."
                            } else {
                                "Paste the raw client brief — we'll uncover the real communication challenge and formulate evidence-based creative directions."
                            },
                            fontSize = 15.sp,
                            color = StudioDark300,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Large Brief Textarea
                        OutlinedTextField(
                            value = rawText,
                            onValueChange = onRawTextChange,
                            enabled = !isAnalyzing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 340.dp, max = 460.dp)
                                .testTag("client_message_input"),
                            placeholder = {
                                Text(
                                    text = if (isArabic) {
                                        "الصق كلام العميل هنا...\n\n(يدعم العامية المصرية، الفصحى، الإنجليزي، أو مزيج بينهم:\n«عايزين ريل انستجرام لحملة الصيف لمطعم برجر جديد، نركز على الخفة والشياكة والـ vibe الهادي بدون ألوان فاقعة، ومحتاجين نبدأ الأسبوع الجاي...»)"
                                    } else {
                                        "Paste client message here...\n\n(Supports English, Arabic, or mixed slang:\n'We want an Instagram campaign for our new burger launch, sleek minimal aesthetic, need 3 carousels and a reel by next week...')"
                                    },
                                    fontSize = 15.sp,
                                    color = StudioDark500,
                                    lineHeight = 24.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricPurple,
                                unfocusedBorderColor = StudioDark700,
                                focusedContainerColor = StudioDark900.copy(alpha = 0.8f),
                                unfocusedContainerColor = StudioDark900.copy(alpha = 0.65f),
                                focusedTextColor = StudioDark50,
                                unfocusedTextColor = StudioDark100
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Quick Demo Presets
                        Text(
                            text = if (isArabic) "نماذج بريفات جاهزة للتجربة:" else "Sample briefs to test:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark400
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SampleData.demoPrompts.forEach { demo ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(StudioDark900)
                                        .border(1.dp, StudioDark800, RoundedCornerShape(8.dp))
                                        .clickable { onLoadDemo(demo) }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                        .testTag("demo_chip_${demo.id}")
                                ) {
                                    Text(
                                        text = demo.label,
                                        fontSize = 12.sp,
                                        color = StudioDark300
                                    )
                                }
                            }
                        }
                    }

                    // Right Column: Controls Panel & Strategy Action (38% width)
                    Column(
                        modifier = Modifier
                            .weight(0.38f)
                            .fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, StudioDark700, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = StudioDark900.copy(alpha = 0.75f))
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                // Team Selector
                                Text(
                                    text = if (isArabic) "البريف ده رايح لمين؟" else "Target Team Deliverables",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioDark100
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TargetTeam.values().forEach { team ->
                                        val isSelected = selectedTeam == team
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) {
                                                        Brush.horizontalGradient(listOf(ElectricPurple, StudioIndigo))
                                                    } else {
                                                        Brush.horizontalGradient(listOf(StudioDark850, StudioDark800))
                                                    }
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isSelected) ElectricPurpleLight else StudioDark700,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .clickable { onTeamSelect(team) }
                                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                                .testTag("team_selector_${team.name.lowercase()}"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (isArabic) team.labelAr else team.labelEn,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color.White else StudioDark300
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Thinking Mode
                                Text(
                                    text = if (isArabic) "طريقة التفكير الإبداعي:" else "Strategic Thinking Mode",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioDark100
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    ThinkingMode.values().forEach { mode ->
                                        val isSelected = thinkingMode == mode
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) StudioDark800 else StudioDark850.copy(alpha = 0.5f))
                                                .border(
                                                    1.dp,
                                                    if (isSelected) SoftCyan.copy(alpha = 0.8f) else StudioDark700,
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .clickable { onThinkingModeSelect(mode) }
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                                .testTag("thinking_mode_${mode.name.lowercase()}"),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { onThinkingModeSelect(mode) },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = SoftCyan,
                                                    unselectedColor = StudioDark500
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = if (isArabic) mode.labelAr else mode.labelEn,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) SoftCyan else StudioDark200
                                                )
                                                Text(
                                                    text = if (isArabic) mode.descAr else mode.descEn,
                                                    fontSize = 11.sp,
                                                    color = StudioDark400,
                                                    lineHeight = 16.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Strategic Note
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ElectricPurple.copy(alpha = 0.12f))
                                        .border(1.dp, ElectricPurple.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(Icons.Default.Psychology, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isArabic) {
                                                "التشخيص الاستراتيجي يفصل الحقيقة عما يقترحه العميل، ويستخرج التحدي الاتصالي والفرصة قبل صياغة الحل."
                                            } else {
                                                "Strategic diagnosis challenges the brief, uncovering the communication barrier and creative territory."
                                            },
                                            fontSize = 11.sp,
                                            color = StudioDark200,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Analyze Action Button or Progressive Processing State
                                if (isAnalyzing) {
                                    ProcessingStateCard(
                                        currentProcessingStage = currentProcessingStage,
                                        processingStepIndex = processingStepIndex,
                                        isArabic = isArabic
                                    )
                                } else {
                                    PrimaryAnalyzeButton(
                                        enabled = rawText.isNotBlank(),
                                        isArabic = isArabic,
                                        onClick = onAnalyzeClick
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Responsive Mobile / Stacked Layout
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isArabic) "العميل محتاج إيه؟" else "What does the client need?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudioDark50,
                        lineHeight = 40.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isArabic) {
                            "الصق كلام العميل زي ما هو — المحرك هيحلل المطلوب ويستخرج التحدي الاتصالي والاتجاهات الإبداعية."
                        } else {
                            "Paste the client message here to uncover the real strategic challenge and creative angles."
                        },
                        fontSize = 14.sp,
                        color = StudioDark300,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = rawText,
                        onValueChange = onRawTextChange,
                        enabled = !isAnalyzing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 220.dp, max = 340.dp)
                            .testTag("client_message_input"),
                        placeholder = {
                            Text(
                                text = if (isArabic) "الصق كلام العميل هنا..." else "Paste client brief here...",
                                fontSize = 14.sp,
                                color = StudioDark500
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricPurple,
                            unfocusedBorderColor = StudioDark700,
                            focusedContainerColor = StudioDark900.copy(alpha = 0.75f),
                            unfocusedContainerColor = StudioDark900.copy(alpha = 0.6f),
                            focusedTextColor = StudioDark50,
                            unfocusedTextColor = StudioDark100
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Team Selector
                    Text(
                        text = if (isArabic) "البريف ده رايح لمين؟" else "Who is this brief for?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioDark200
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TargetTeam.values().forEach { team ->
                            val isSelected = selectedTeam == team
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) ElectricPurple else StudioDark850)
                                    .clickable { onTeamSelect(team) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .testTag("team_selector_${team.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isArabic) team.labelAr else team.labelEn,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else StudioDark300
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Thinking Mode
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isArabic) "طريقة التفكير:" else "Thinking mode:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = StudioDark400
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ThinkingMode.values().forEach { mode ->
                                val isSelected = thinkingMode == mode
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) StudioDark800 else Color.Transparent)
                                        .border(1.dp, if (isSelected) SoftCyan else StudioDark700, RoundedCornerShape(8.dp))
                                        .clickable { onThinkingModeSelect(mode) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("thinking_mode_${mode.name.lowercase()}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isArabic) mode.labelAr else mode.labelEn,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) SoftCyan else StudioDark400
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isAnalyzing) {
                        ProcessingStateCard(
                            currentProcessingStage = currentProcessingStage,
                            processingStepIndex = processingStepIndex,
                            isArabic = isArabic
                        )
                    } else {
                        PrimaryAnalyzeButton(
                            enabled = rawText.isNotBlank(),
                            isArabic = isArabic,
                            onClick = onAnalyzeClick
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Quick Demo Presets
                    Text(
                        text = if (isArabic) "نماذج بريفات للتجربة:" else "Sample briefs to test:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = StudioDark400
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SampleData.demoPrompts.forEach { demo ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(StudioDark900)
                                    .border(1.dp, StudioDark800, RoundedCornerShape(8.dp))
                                    .clickable { onLoadDemo(demo) }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                                    .testTag("demo_chip_${demo.id}")
                            ) {
                                Text(demo.label, fontSize = 11.sp, color = StudioDark300)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun PrimaryAnalyzeButton(
    enabled: Boolean,
    isArabic: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("primary_analyze_button"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = StudioDark800
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (enabled) {
                        Brush.horizontalGradient(listOf(ElectricPurple, StudioIndigo))
                    } else {
                        Brush.horizontalGradient(listOf(StudioDark800, StudioDark850))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isArabic) "حلّل الـBrief" else "Analyze Brief",
                    color = if (enabled) Color.White else StudioDark500,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (isArabic) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = if (enabled) SoftCyan else StudioDark500,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ProcessingStateCard(
    currentProcessingStage: String,
    processingStepIndex: Int,
    isArabic: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ElectricPurple.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioDark900)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    color = SoftCyan,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = currentProcessingStage.ifBlank {
                        if (isArabic) "جاري تشخيص البريف وصياغة الاستراتيجية..." else "Analyzing brief & forming strategy..."
                    },
                    color = StudioDark50,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress Step Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (step in 0..4) {
                    val isDone = step <= processingStepIndex
                    Box(
                        modifier = Modifier
                            .size(if (step == processingStepIndex) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    step == processingStepIndex -> ElectricPurple
                                    isDone -> SoftCyan
                                    else -> StudioDark700
                                }
                            )
                    )
                }
            }
        }
    }
}
