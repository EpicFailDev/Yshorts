package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SeoOptimizationResult
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite
import com.example.util.ExportHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TubeSeoScreen(
    seoResult: SeoOptimizationResult,
    isGenerating: Boolean,
    onGenerateSeo: (String) -> Unit,
    onScheduleVideo: (title: String, duration: String, time: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var videoTopic by remember { mutableStateOf(seoResult.topic) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "SEO & Metadados com IA",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                color = TubeMasterWhite
            )
            Text(
                text = "Otimizador profissional: títulos magnéticos, tags de busca, descrição e CTR",
                style = MaterialTheme.typography.bodySmall,
                color = TubeMasterGray
            )
        }

        // Input Box
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Tema ou Título Provisório",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TubeMasterWhite
                )

                OutlinedTextField(
                    value = videoTopic,
                    onValueChange = { videoTopic = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TubeMasterRed,
                        unfocusedBorderColor = TubeMasterBorder,
                        focusedTextColor = TubeMasterWhite,
                        unfocusedTextColor = TubeMasterWhite,
                        cursorColor = TubeMasterRed
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("seo_input_topic")
                )

                Button(
                    onClick = {
                        if (videoTopic.isNotBlank()) {
                            onGenerateSeo(videoTopic)
                        } else {
                            Toast.makeText(context, "Digite um tema para otimizar", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isGenerating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TubeMasterRed,
                        contentColor = TubeMasterWhite,
                        disabledContainerColor = TubeMasterRed.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("generate_seo_btn")
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = TubeMasterWhite,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Otimizando com IA...", style = MaterialTheme.typography.labelMedium)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gerar Otimização Completa com IA", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        // SEO Score Banner
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Pontuação Geral de SEO", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    Text("${seoResult.seoScore} / 100", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TubeMasterGreen)
                    Text("Potencial no Algoritmo: ${seoResult.potential}", style = MaterialTheme.typography.labelSmall, color = TubeMasterGrayDark)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF142B1B),
                    border = BorderStroke(1.dp, TubeMasterGreen)
                ) {
                    Text(
                        text = seoResult.scoreBadge,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TubeMasterGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // Suggested Titles
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Títulos Magnéticos Recomendados",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TubeMasterWhite
                    )
                    Text(
                        text = "Clique para copiar ou agendar",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGray
                    )
                }

                seoResult.suggestedTitles.forEach { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF121212), RoundedCornerShape(6.dp))
                            .border(0.5.dp, TubeMasterBorder, RoundedCornerShape(6.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = TubeMasterWhite,
                                modifier = Modifier.weight(1f)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = {
                                        ExportHelper.copyText(context, "Título", item.title)
                                    },
                                    modifier = Modifier.size(28.dp).testTag("copy_title_btn")
                                ) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = "Copiar Título",
                                        tint = TubeMasterGray,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        onScheduleVideo(item.title, "12:00", "Amanhã • 18:00")
                                        Toast.makeText(context, "Vídeo adicionado à fila de agendamento!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(28.dp).testTag("schedule_from_title_btn")
                                ) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = "Agendar com este título",
                                        tint = TubeMasterRed,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF1B2E1E)
                            ) {
                                Text(
                                    text = "CTR Previsto: ${item.estimatedCtr}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                    color = TubeMasterGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "Ângulo: ${item.angle}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TubeMasterGrayDark
                            )
                        }
                    }
                }
            }
        }

        // Tags virais
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tags de Busca & Hashtags",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TubeMasterWhite
                    )

                    OutlinedButton(
                        onClick = {
                            ExportHelper.copyText(context, "Tags do YouTube Studio", seoResult.commaSeparatedTags)
                        },
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.8.dp, TubeMasterRed),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TubeMasterRed),
                        modifier = Modifier.height(28.dp).testTag("copy_all_tags_btn")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar p/ YouTube Studio", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
                    }
                }

                Text(
                    text = "Clique em qualquer tag individual para copiar:",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TubeMasterGray
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    seoResult.commaSeparatedTags.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF222222),
                            border = BorderStroke(0.5.dp, TubeMasterBorder),
                            modifier = Modifier.clickable {
                                ExportHelper.copyText(context, "Tag", tag)
                            }
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = TubeMasterWhite,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hashtags sugeridas:",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                    color = TubeMasterGray
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    seoResult.hashtagList.forEach { hash ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF1A1A24),
                            border = BorderStroke(0.5.dp, Color(0xFF3B82F6).copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                ExportHelper.copyText(context, "Hashtag", hash)
                            }
                        ) {
                            Text(
                                text = hash,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Color(0xFF60A5FA),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Descrição Otimizada
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Descrição de Alta Conversão",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TubeMasterWhite
                        )
                    }

                    Button(
                        onClick = {
                            ExportHelper.copyText(context, "Descrição", seoResult.optimizedDescription)
                        },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TubeMasterRed,
                            contentColor = TubeMasterWhite
                        ),
                        modifier = Modifier.height(28.dp).testTag("copy_description_btn")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
                    }
                }

                Surface(
                    color = Color(0xFF101010),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.5.dp, TubeMasterBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = seoResult.optimizedDescription,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                        color = TubeMasterGray,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // Thumbnail Concept & AI Image Prompt
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = TubeMasterGreen, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Conceito de Thumbnail & Prompt IA",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TubeMasterWhite
                    )
                }

                Text(
                    text = seoResult.thumbnailVisualConcept,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TubeMasterWhite
                )

                Surface(
                    color = Color(0xFF141414),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(0.5.dp, TubeMasterBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = seoResult.thumbnailPromptForAi,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TubeMasterGray,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(
                            onClick = {
                                ExportHelper.copyText(context, "Prompt de Imagem IA", seoResult.thumbnailPromptForAi)
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar Prompt", tint = TubeMasterGray, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // Export Entire Package Button (TXT / Share)
        Button(
            onClick = {
                ExportHelper.exportSeoPackage(context, seoResult)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF262626),
                contentColor = TubeMasterWhite
            ),
            border = BorderStroke(1.dp, TubeMasterBorder),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(44.dp).testTag("export_seo_package_btn")
        ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Exportar Pacote de SEO Completo (TXT)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
