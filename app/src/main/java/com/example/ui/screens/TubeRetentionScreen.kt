package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.DropMoment
import com.example.domain.model.RetentionDiagnostic
import com.example.domain.model.VideoRetentionAnalysis
import com.example.ui.components.RetentionLineChart
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite
import com.example.util.ExportHelper

@Composable
fun TubeRetentionScreen(
    retention: VideoRetentionAnalysis,
    onSelectVideo: (String) -> Unit,
    onDiagnoseRetention: (videoTitle: String, timestamp: String, desc: String, dropPercent: Int, onResult: (RetentionDiagnostic) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Diagnostic Modal State
    var activeDiagnosticMoment by remember { mutableStateOf<DropMoment?>(null) }
    var currentDiagnostic by remember { mutableStateOf<RetentionDiagnostic?>(null) }
    var isLoadingDiagnostic by remember { mutableStateOf(false) }

    // Retention Pacing Simulator State
    var pacingMultiplier by remember { mutableFloatStateOf(1.0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column {
            Text(
                text = "Análise de Retenção & Audiência",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 21.sp),
                color = TubeMasterWhite
            )
            Text(
                text = "Diagnóstico segundo a segundo com soluções de edição da IA",
                style = MaterialTheme.typography.bodySmall,
                color = TubeMasterGray
            )
        }

        // Video Selector Dropdown
        Box {
            Surface(
                color = TubeMasterCard,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDropdownExpanded = true }
                    .testTag("video_selector_dropdown")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Brush.linearGradient(listOf(Color(0xFF7F1D1D), Color(0xFF000000)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TubeMasterWhite, modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = retention.videoTitle,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = TubeMasterWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Duração: ${retention.duration} • Publicado em ${retention.uploadDate}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TubeMasterGray
                            )
                        }
                    }
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TubeMasterGray)
                }
            }

            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.background(TubeMasterCard).border(1.dp, TubeMasterBorder)
            ) {
                DropdownMenuItem(
                    text = { Text("Como criar thumbnails que realmente funcionam", color = TubeMasterWhite, fontSize = 13.sp) },
                    onClick = {
                        onSelectVideo("v1")
                        isDropdownExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Ferramentas de IA para criadores de conteúdo", color = TubeMasterWhite, fontSize = 13.sp) },
                    onClick = {
                        onSelectVideo("v2")
                        isDropdownExpanded = false
                    }
                )
            }
        }

        // Two Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = TubeMasterCard,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Taxa de Retenção", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    val simulatedRate = (62.8f * pacingMultiplier).coerceAtMost(94.0f)
                    Text(
                        text = if (pacingMultiplier == 1.0f) retention.retentionRate else "%.1f%%".format(simulatedRate),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TubeMasterWhite
                    )
                    Text(text = retention.retentionChange, style = MaterialTheme.typography.labelSmall, color = TubeMasterGreen)
                }
            }

            Surface(
                color = TubeMasterCard,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Tempo Médio de Exibição", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    Text(text = retention.avgWatchDuration, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TubeMasterWhite)
                    Text(text = retention.avgWatchChange, style = MaterialTheme.typography.labelSmall, color = TubeMasterGreen)
                }
            }
        }

        // Chart Card
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Curva de Retenção do Espectador", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold), color = TubeMasterWhite)
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF1B2E1E)) {
                        Text(
                            text = "Média Canal: 48%",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                            color = TubeMasterGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Chart Graphic
                RetentionLineChart(
                    curvePoints = retention.curvePoints,
                    abandonmentCallout = retention.abandonmentCallout,
                    peakCallout = retention.peakCallout,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Retention Pacing Simulator
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(16.dp))
                    Text(
                        text = "Simulador de Ritmo & Retenção Prevista",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TubeMasterWhite
                    )
                }

                Text(
                    text = "Ajuste o ritmo da edição (cortes a cada 3s, zoom-in e pattern interrupts) para simular o ganho na retenção:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TubeMasterGray
                )

                Slider(
                    value = pacingMultiplier,
                    onValueChange = { pacingMultiplier = it },
                    valueRange = 0.8f..1.3f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF60A5FA),
                        activeTrackColor = Color(0xFF3B82F6),
                        inactiveTrackColor = Color(0xFF262626)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("retention_pacing_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Edição Lenta (0.8x)", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = TubeMasterGrayDark)
                    Text(
                        text = "Retenção Projetada: %.1f%% (%s)".format(
                            62.8f * pacingMultiplier,
                            if (pacingMultiplier >= 1.0f) "+%.1f%%".format((pacingMultiplier - 1f) * 100)
                            else "-%.1f%%".format((1f - pacingMultiplier) * 100)
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                        color = if (pacingMultiplier >= 1.0f) TubeMasterGreen else TubeMasterRed
                    )
                    Text("Super Dinâmico (1.3x)", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = TubeMasterGrayDark)
                }
            }
        }

        // Drop Moments List with Working AI Diagnosis
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Pontos de Queda no Vídeo",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TubeMasterWhite
                )
                Text(
                    text = "Toque em qualquer momento para abrir o Diagnóstico & Solução IA:",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                    color = TubeMasterGray
                )

                retention.dropMoments.forEach { moment ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF131313),
                        border = BorderStroke(0.8.dp, TubeMasterBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeDiagnosticMoment = moment
                                isLoadingDiagnostic = true
                                onDiagnoseRetention(retention.videoTitle, moment.timestamp, moment.description, moment.dropPercent) { diag ->
                                    currentDiagnostic = diag
                                    isLoadingDiagnostic = false
                                }
                            }
                            .testTag("drop_moment_${moment.timestamp.replace(":", "_")}")
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF262626)
                                ) {
                                    Text(
                                        text = moment.timestamp,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TubeMasterWhite,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = moment.description,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        color = TubeMasterWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Toque para ver a correção da IA",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = TubeMasterRed
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { moment.dropPercent / 100f },
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (moment.dropPercent > 50) TubeMasterGreen else TubeMasterRed,
                                    trackColor = Color(0xFF262626)
                                )

                                Text(
                                    text = "${moment.dropPercent}%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (moment.dropPercent > 50) TubeMasterGreen else TubeMasterRed
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Modal: Diagnóstico & Solução IA
    if (activeDiagnosticMoment != null) {
        val moment = activeDiagnosticMoment!!
        Dialog(onDismissRequest = {
            activeDiagnosticMoment = null
            currentDiagnostic = null
        }) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = TubeMasterCard,
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(shape = RoundedCornerShape(6.dp), color = TubeMasterRed.copy(alpha = 0.2f)) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.padding(6.dp).size(16.dp))
                            }
                            Text(
                                text = "Diagnóstico IA (${moment.timestamp})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TubeMasterWhite
                            )
                        }

                        IconButton(
                            onClick = {
                                activeDiagnosticMoment = null
                                currentDiagnostic = null
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TubeMasterGray)
                        }
                    }

                    if (isLoadingDiagnostic) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = TubeMasterRed, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analisando perda de retenção com IA...", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                        }
                    } else {
                        val diag = currentDiagnostic ?: RetentionDiagnostic(
                            timestamp = moment.timestamp,
                            dropPercent = moment.dropPercent,
                            diagnosticReason = moment.reasonAnalysis.ifBlank { "Queda provocada por perda de ritmo na narrativa e falta de estímulos visuais." },
                            recommendedFix = moment.actionableFix.ifBlank { "Corte os 10s excedentes e insira b-roll demonstrativo." },
                            editingTip = "Adicione corte zoom in 1.15x e efeito sonoro 'whoosh'."
                        )

                        // Motivo do Abandono
                        Surface(
                            color = Color(0xFF261212),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.8.dp, TubeMasterRed.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Por que os espectadores saíram:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TubeMasterRed)
                                Text(diag.diagnosticReason, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp), color = TubeMasterWhite)
                            }
                        }

                        // Ação Recomendada
                        Surface(
                            color = Color(0xFF142416),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.8.dp, TubeMasterGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Solução Prática de Roteiro / Edição:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TubeMasterGreen)
                                Text(diag.recommendedFix, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp), color = TubeMasterWhite)
                            }
                        }

                        // Dica Técnica
                        Surface(
                            color = Color(0xFF121212),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.5.dp, TubeMasterBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Dica Técnica (Pattern Interrupt):", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF60A5FA))
                                Text(diag.editingTip, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = TubeMasterGray)
                            }
                        }

                        // Copiar notas de edição
                        Button(
                            onClick = {
                                val note = "Nota de Edição (${moment.timestamp}): ${diag.recommendedFix} | Técnica: ${diag.editingTip}"
                                ExportHelper.copyText(context, "Nota de Edição", note)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TubeMasterRed,
                                contentColor = TubeMasterWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(40.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copiar Notas para o Editor", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}
