package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TubeSeoScreen(
    modifier: Modifier = Modifier
) {
    var videoTopic by remember { mutableStateOf("Como crescer canal no YouTube do zero em 2025") }
    var isGenerating by remember { mutableStateOf(false) }

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
                text = "SEO com IA",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                color = TubeMasterWhite
            )
            Text(
                text = "Geração de títulos magnéticos, descrições otimizadas e tags virais",
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
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { isGenerating = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TubeMasterRed,
                        contentColor = TubeMasterWhite
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("generate_seo_btn")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gerar Otimização com IA", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
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
                    Text("94 / 100", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TubeMasterGreen)
                    Text("Potencial de busca: Muito Alto", style = MaterialTheme.typography.labelSmall, color = TubeMasterGrayDark)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF142B1B),
                    border = BorderStroke(1.dp, TubeMasterGreen)
                ) {
                    Text(
                        text = "Excelente",
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
                Text(
                    text = "Títulos Magnéticos Sugeridos",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TubeMasterWhite
                )

                listOf(
                    "Como Crescer no YouTube em 2025 (Segredo dos Canais Grandes)",
                    "O Algoritmo do YouTube Mudou: Faça Isso Para Ganhar Inscritos",
                    "Do Zero aos 100K: Guia Definitivo Para Criadores de Conteúdo"
                ).forEach { title ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF121212), RoundedCornerShape(6.dp))
                            .border(0.5.dp, TubeMasterBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = title, style = MaterialTheme.typography.bodySmall, color = TubeMasterWhite, modifier = Modifier.weight(1f))
                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = TubeMasterGray, modifier = Modifier.size(14.dp))
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
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Tags de Alto Volume de Busca",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TubeMasterWhite
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "crescer no youtube", "algoritmo youtube", "como ter mais visualizações",
                        "ganhar inscritos", "dicas para youtubers", "engajamento", "monetização"
                    ).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF222222),
                            border = BorderStroke(0.5.dp, TubeMasterBorder)
                        ) {
                            Text(
                                text = "#$tag",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = TubeMasterGray,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
