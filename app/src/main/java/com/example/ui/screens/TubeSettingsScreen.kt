package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CommentItem
import com.example.domain.model.TubeMasterVideo
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite
import com.example.util.ExportHelper

@Composable
fun TubeSettingsScreen(
    videos: List<TubeMasterVideo>,
    comments: List<CommentItem>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var autoModerateSpam by remember { mutableStateOf(true) }
    var autoSuggestSchedule by remember { mutableStateOf(true) }
    var highRetentionPacing by remember { mutableStateOf(true) }

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
                text = "Configurações & Automação do Canal",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 21.sp),
                color = TubeMasterWhite
            )
            Text(
                text = "Gerenciamento de pipelines, IA e integrações com robôs e uploaders",
                style = MaterialTheme.typography.bodySmall,
                color = TubeMasterGray
            )
        }

        // Integration Pipelines Card (Inspirado nos repositórios)
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(6.dp), color = TubeMasterRed.copy(alpha = 0.2f)) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.padding(6.dp).size(16.dp))
                    }
                    Column {
                        Text(
                            text = "Agentes & Pipelines Conectados",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TubeMasterWhite
                        )
                        Text(
                            text = "Compatível com agentes open-source (Shortsmith, Auto-Uploader 2026)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TubeMasterGray
                        )
                    }
                }

                // Pipelines status badges
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF141414),
                    border = BorderStroke(0.6.dp, TubeMasterBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Auto-Uploader 2026 (CSV Sync):", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF1B2E1E)) {
                                Text("PRONTO", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold), color = TubeMasterGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Motor IA de Roteiros & SEO:", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF1E293B)) {
                                Text("GEMINI PRO / ALGORÍTMICO", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold), color = Color(0xFF60A5FA), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Diagnóstico de Retenção:", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF1B2E1E)) {
                                Text("ATIVO", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold), color = TubeMasterGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                }
            }
        }

        // Toggles de Comportamento Autônomo
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Regras de Automação & Segurança",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TubeMasterWhite
                )

                // Toggle 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Detecção Proativa de Spam em Comentários", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = TubeMasterWhite)
                        Text("Sinaliza termos como 'investimento no zap', crypto e bots", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    }
                    Switch(
                        checked = autoModerateSpam,
                        onCheckedChange = {
                            autoModerateSpam = it
                            Toast.makeText(context, if (it) "Auto-moderação ativada" else "Auto-moderação desativada", Toast.LENGTH_SHORT).show()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = TubeMasterWhite, checkedTrackColor = TubeMasterRed)
                    )
                }

                // Toggle 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Cálculo Algorítmico do Melhor Horário", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = TubeMasterWhite)
                        Text("Calcula o pico de público baseado no histórico dos últimos 7 dias", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    }
                    Switch(
                        checked = autoSuggestSchedule,
                        onCheckedChange = {
                            autoSuggestSchedule = it
                            Toast.makeText(context, if (it) "Otimizador de horários ativo" else "Horário manual selecionado", Toast.LENGTH_SHORT).show()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = TubeMasterWhite, checkedTrackColor = TubeMasterRed)
                    )
                }

                // Toggle 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sugestões de Pattern Interrupt na Retenção", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium), color = TubeMasterWhite)
                        Text("Recomenda zooms e sound effects em quedas maiores que 15%", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    }
                    Switch(
                        checked = highRetentionPacing,
                        onCheckedChange = {
                            highRetentionPacing = it
                            Toast.makeText(context, if (it) "Pattern Interrupts ativos" else "Desativado", Toast.LENGTH_SHORT).show()
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = TubeMasterWhite, checkedTrackColor = TubeMasterRed)
                    )
                }
            }
        }

        // Export All Data & Backups
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Backup & Exportação de Dados do Canal",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = TubeMasterWhite
                )

                Text(
                    text = "Exporte todos os vídeos agendados, rascunhos e métricas para uso em planilhas ou bots de terceiros:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TubeMasterGray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            ExportHelper.exportMetadataCsv(context, videos)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TubeMasterRed, contentColor = TubeMasterWhite),
                        modifier = Modifier.weight(1f).height(40.dp).testTag("settings_export_csv_btn")
                    ) {
                        Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Exportar CSV", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    OutlinedButton(
                        onClick = {
                            val summary = buildString {
                                appendLine("=== CANAL TUBE MASTER RESUMO ===")
                                appendLine("Total de Vídeos na Fila: ${videos.size}")
                                videos.forEach { v ->
                                    appendLine("- ${v.title} [${v.status.label}] (${v.scheduledTime})")
                                }
                                appendLine("\nTotal de Comentários Moderados: ${comments.size}")
                            }
                            ExportHelper.copyText(context, "Resumo do Canal", summary)
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, TubeMasterBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TubeMasterWhite),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copiar Sumário", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
