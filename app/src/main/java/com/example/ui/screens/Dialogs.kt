package com.example.ui.screens

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.BriefEntity
import com.example.data.model.AppLanguage
import com.example.data.model.TargetTeam
import com.example.ui.theme.*

@Composable
fun HistoryDialog(
    historyList: List<BriefEntity>,
    appLanguage: AppLanguage = AppLanguage.ARABIC,
    onSelectBrief: (BriefEntity) -> Unit,
    onDeleteBrief: (BriefEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val isArabic = appLanguage == AppLanguage.ARABIC

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .border(1.dp, StudioDark700, RoundedCornerShape(16.dp))
                .testTag("history_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDark900)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = SoftCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "أرشيف البريفات والأفكار" else "Briefs & Ideas Archive",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark50
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = if (isArabic) "إغلاق" else "Close",
                            tint = StudioDark400
                        )
                    }
                }

                Text(
                    text = if (isArabic) "البريفات والمخرجات الإبداعية التي تم تحليلها مسبقاً (كل بريف مستقل تماماً)" else "Independently archived client briefs",
                    fontSize = 12.sp,
                    color = StudioDark400
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (historyList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = StudioDark600,
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isArabic) "لا توجد بريفات محفوظة حتى الآن" else "No saved briefs yet",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = StudioDark400
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isArabic) "عند تحليل أي كلام عميل، سيتم حفظه بشكل مستقل هنا." else "When you analyze a brief, it will be archived independently here.",
                                fontSize = 11.sp,
                                color = StudioDark500
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(historyList, key = { it.id }) { item ->
                            val dateFormatted = DateFormat.format("d MMM yyyy, h:mm a", item.createdAt)
                            val teamEnum = try { TargetTeam.valueOf(item.targetTeam) } catch (e: Exception) { TargetTeam.ALL }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, StudioDark800, RoundedCornerShape(10.dp))
                                    .clickable { onSelectBrief(item) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = StudioDark850)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ElectricPurple.copy(alpha = 0.2f))
                                            .border(1.dp, ElectricPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isArabic) teamEnum.labelAr.take(3) else teamEnum.labelEn.take(3),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SoftCyan
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StudioDark100,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "$dateFormatted • ${if (isArabic) "تفكير ${item.thinkingMode}" else "Mode: ${item.thinkingMode}"}",
                                            fontSize = 11.sp,
                                            color = StudioDark400,
                                            maxLines = 1
                                        )
                                    }

                                    IconButton(
                                        onClick = { onDeleteBrief(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = if (isArabic) "حذف" else "Delete",
                                            tint = StudioDark500,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApiKeyDialog(
    currentCustomKey: String,
    isAiActive: Boolean,
    appLanguage: AppLanguage = AppLanguage.ARABIC,
    onSaveKey: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var keyInput by remember { mutableStateOf(currentCustomKey) }
    val isArabic = appLanguage == AppLanguage.ARABIC

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, StudioDark700, RoundedCornerShape(16.dp))
                .testTag("api_key_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDark900)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SettingsSuggest,
                            contentDescription = null,
                            tint = SoftCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "إعدادات محرك الذكاء الإبداعي" else "Creative Intelligence Settings",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark50
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = if (isArabic) "إغلاق" else "Close",
                            tint = StudioDark400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Status card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isAiActive) StudioSuccess.copy(alpha = 0.15f) else StudioDark850)
                        .border(1.dp, if (isAiActive) StudioSuccess.copy(alpha = 0.6f) else StudioDark700, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isAiActive) Icons.Default.CheckCircle else Icons.Default.SensorsOff,
                            contentDescription = null,
                            tint = if (isAiActive) StudioSuccess else StudioWarning,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAiActive) {
                                    if (isArabic) "نموذج Gemini السحابي نشط" else "Gemini Cloud AI is Active"
                                } else {
                                    if (isArabic) "المحرك الإبداعي الذاتي نشط" else "Local Creative Engine Active"
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAiActive) StudioSuccess else StudioWarning
                            )
                            Text(
                                text = if (isAiActive) {
                                    if (isArabic) "يتم استخدام Gemini لتشخيص وتحليل البريفات بشكل مستقل وفوري." else "Using Gemini model for agency brief analysis and independent creative intelligence."
                                } else {
                                    if (isArabic) "يعمل التطبيق بكفاءة كاملة عبر محرك تشخيص الوكالة المدمج." else "App runs at full speed using built-in agency creative intelligence engine."
                                },
                                fontSize = 11.sp,
                                color = StudioDark300,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isArabic) "مفتاح Gemini API مخصص (اختياري)" else "Custom Gemini API Key (Optional)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioDark200
                )
                Text(
                    text = if (isArabic) {
                        "يمكنك إدخال مفتاحك الخاص من Google AI Studio (aistudio.google.com/app/apikey) لتجنب حدود الحصة المجانية (Quota Limits). في حال نفاد الحصة، يعمل المحرك الإبداعي الذكي المدمج تلقائياً دون توقف."
                    } else {
                        "Use your own API key from Google AI Studio (aistudio.google.com/app/apikey) to avoid shared quota limits. In case of quota exhaustion, the built-in diagnostic engine takes over automatically."
                    },
                    fontSize = 11.sp,
                    color = StudioDark400,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    placeholder = { Text("AIzaSy...", fontSize = 12.sp, color = StudioDark500) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("api_key_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricPurple,
                        unfocusedBorderColor = StudioDark700,
                        focusedContainerColor = StudioDark850,
                        unfocusedContainerColor = StudioDark850,
                        focusedTextColor = StudioDark50,
                        unfocusedTextColor = StudioDark100
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(if (isArabic) "إلغاء" else "Cancel", color = StudioDark400, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSaveKey(keyInput.trim().ifEmpty { null }) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isArabic) "حفظ الإعدادات" else "Save Settings", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
