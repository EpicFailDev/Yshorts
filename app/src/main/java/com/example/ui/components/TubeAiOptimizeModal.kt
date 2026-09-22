package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeAiOptimizeModal(
    onDismiss: () -> Unit,
    onApplyOptimization: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = TubeMasterCard,
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = TubeMasterRed.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, TubeMasterRed)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = TubeMasterRed,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(16.dp)
                            )
                        }
                        Text(
                            text = "Otimizador TubeMaster AI",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TubeMasterWhite
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TubeMasterGray)
                    }
                }

                // AI Suggested Scheduled Time
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF142B1B),
                    border = BorderStroke(0.8.dp, TubeMasterGreen)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = TubeMasterGreen, modifier = Modifier.size(16.dp))
                        Column {
                            Text(
                                text = "Melhor Horário de Publicação (Pico de Retenção):",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TubeMasterGreen
                            )
                            Text(
                                text = "Terça-feira, 15:00 (+48% de engajamento previsto)",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = TubeMasterWhite
                            )
                        }
                    }
                }

                // AI Generated Titles
                Text(
                    text = "Títulos Otimizados para Alto CTR:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TubeMasterWhite
                )

                listOf(
                    "Como Criar Thumbnails que Realmente Funcionam (Passo a Passo)" to "9.4% CTR",
                    "O Segredo das Thumbnails que Geram Milhões de Views" to "8.8% CTR"
                ).forEach { (title, ctr) ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF121212),
                        border = BorderStroke(0.5.dp, TubeMasterBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = title, style = MaterialTheme.typography.bodySmall, color = TubeMasterWhite)
                                Text(text = "Projeção: $ctr", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = TubeMasterGreen)
                            }
                            Button(
                                onClick = { onApplyOptimization(title) },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TubeMasterRed,
                                    contentColor = TubeMasterWhite
                                ),
                                modifier = Modifier.height(28.dp).testTag("select_optimized_title_btn")
                            ) {
                                Text("Usar", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }

                // AI Thumbnail Concept
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF181818),
                    border = BorderStroke(0.5.dp, TubeMasterBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.size(20.dp))
                        Column {
                            Text("Conceito de Thumbnail Recomendado:", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = TubeMasterGray)
                            Text("Close facial com expressão de surpresa à direita, texto 'NUNCA FAÇA ISSO' em amarelo fluorescente e borda vermelha.", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = TubeMasterWhite)
                        }
                    }
                }

                Button(
                    onClick = {
                        onApplyOptimization("Como Criar Thumbnails que Realmente Funcionam")
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TubeMasterRed,
                        contentColor = TubeMasterWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("apply_all_optimizations_btn")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Aplicar Otimizações ao Vídeo", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
