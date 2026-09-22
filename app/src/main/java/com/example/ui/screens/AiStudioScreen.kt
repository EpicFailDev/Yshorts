package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.GeminiService
import com.example.data.remote.GeneratedShortsScript
import com.example.data.remote.ThemeSuggestion
import com.example.domain.model.ScriptStatus
import com.example.domain.model.ShortsScript
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun AiStudioScreen(
    geminiService: GeminiService,
    onSaveScript: (ShortsScript) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Roteiro Viral, 1: Temas em Alta, 2: Resumo

    // Tab 1 States (Gerar Roteiro)
    var scriptTopic by remember { mutableStateOf("") }
    var scriptCategory by remember { mutableStateOf("Curiosidades") }
    var scriptDuration by remember { mutableIntStateOf(30) }
    var isGeneratingScript by remember { mutableStateOf(false) }
    var generatedScript by remember { mutableStateOf<GeneratedShortsScript?>(null) }

    // Tab 2 States (Sugerir Temas)
    var themeNiche by remember { mutableStateOf("Tecnologia") }
    var isSuggestingThemes by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf<List<ThemeSuggestion>>(emptyList()) }

    // Tab 3 States (Resumo)
    var rawTextToSummarize by remember { mutableStateOf("") }
    var isSummarizing by remember { mutableStateOf(false) }
    var summaryResult by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        // Header Banner: Gemini 3.1 Pro Preview with Thinking Mode (HIGH)
        Surface(
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "IA Shorts Studio",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Modelo: gemini-3.1-pro-preview • High Thinking",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Thinking: HIGH",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Clean Secondary Tab Row (Flat, minimal)
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Roteiro Viral", style = MaterialTheme.typography.labelMedium) },
                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_viral_script")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Temas em Alta", style = MaterialTheme.typography.labelMedium) },
                icon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_trending_themes")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Resumo Rápido", style = MaterialTheme.typography.labelMedium) },
                icon = { Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp)) },
                modifier = Modifier.testTag("tab_quick_summary")
            )
        }

        // Tab Contents
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // TAB 0: GERADOR DE ROTEIROS VIRAIS
                    item {
                        Text(
                            text = "Criar Novo Roteiro Baseado em Tendências",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "A IA analisa estruturas de alta retenção, ganchos nos primeiros 3s e pacing ideal para YouTube Shorts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = scriptTopic,
                            onValueChange = { scriptTopic = it },
                            label = { Text("Tema ou Tendência de Busca") },
                            placeholder = { Text("Ex: O que acontece se parar de tomar café por 7 dias?") },
                            modifier = Modifier.fillMaxWidth().testTag("ai_script_topic_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = scriptCategory,
                                onValueChange = { scriptCategory = it },
                                label = { Text("Categoria") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // Duration Chips
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf(30, 45, 60).forEach { sec ->
                                    FilterChip(
                                        selected = scriptDuration == sec,
                                        onClick = { scriptDuration = sec },
                                        label = { Text("${sec}s", style = MaterialTheme.typography.labelSmall) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (scriptTopic.isBlank()) {
                                    Toast.makeText(context, "Digite o tema do vídeo", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isGeneratingScript = true
                                scope.launch {
                                    val result = geminiService.generateShortsScriptWithThinking(
                                        topicOrTrend = scriptTopic,
                                        category = scriptCategory,
                                        targetDurationSeconds = scriptDuration
                                    )
                                    isGeneratingScript = false
                                    if (result.isSuccess) {
                                        generatedScript = result.getOrNull()
                                        Toast.makeText(context, "Roteiro gerado com High Thinking!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Erro: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            enabled = !isGeneratingScript,
                            modifier = Modifier.fillMaxWidth().testTag("ai_generate_script_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (isGeneratingScript) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pensando em ganchos e retenção...")
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gerar Roteiro Viral")
                            }
                        }
                    }

                    // Result Display Card
                    generatedScript?.let { script ->
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = script.title,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary)
                                        ) {
                                            Text(
                                                text = "${scriptDuration}s",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    // Hook
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "⚡ GANCHO INICIAL (0-3s)",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = script.hook,
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                            )
                                        }
                                    }

                                    // Body
                                    Text(
                                        text = "CORPO DO ROTEIRO",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = script.bodyContent,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    // CTA
                                    Text(
                                        text = "CHAMADA PARA AÇÃO (CTA)",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = script.callToAction,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    // Visual notes
                                    if (script.visualNotes.isNotBlank()) {
                                        Text(
                                            text = "NOTAS VISUAIS / B-ROLL",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = script.visualNotes,
                                            style = MaterialTheme.typography.labelSmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Save Button
                                    Button(
                                        onClick = {
                                            val newShortsScript = ShortsScript(
                                                title = script.title,
                                                category = script.category,
                                                hook = script.hook,
                                                bodyContent = script.bodyContent,
                                                callToAction = script.callToAction,
                                                visualNotes = script.visualNotes,
                                                targetDurationSeconds = scriptDuration,
                                                status = ScriptStatus.DRAFT,
                                                tags = script.tags
                                            )
                                            onSaveScript(newShortsScript)
                                            Toast.makeText(context, "Roteiro salvo na sua lista!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.fillMaxWidth().testTag("save_generated_script_btn"),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Salvar na Minha Lista de Roteiros")
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: SUGESTÕES DE TEMAS EM ALTA
                    item {
                        Text(
                            text = "Sugestões de Novos Temas em Alta",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Descubra ângulos com alta probabilidade de clique, buscas em alta e ganchos prontos.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = themeNiche,
                            onValueChange = { themeNiche = it },
                            label = { Text("Nicho ou Categoria") },
                            placeholder = { Text("Ex: Finanças para Jovens, Inteligência Artificial, Saúde") },
                            modifier = Modifier.fillMaxWidth().testTag("ai_theme_niche_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    item {
                        Button(
                            onClick = {
                                if (themeNiche.isBlank()) {
                                    Toast.makeText(context, "Digite o nicho ou categoria", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isSuggestingThemes = true
                                scope.launch {
                                    val result = geminiService.suggestTrendingThemesWithThinking(themeNiche)
                                    isSuggestingThemes = false
                                    if (result.isSuccess) {
                                        suggestions = result.getOrNull() ?: emptyList()
                                        Toast.makeText(context, "Ideias geradas com High Thinking!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Erro: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            enabled = !isSuggestingThemes,
                            modifier = Modifier.fillMaxWidth().testTag("ai_suggest_themes_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (isSuggestingThemes) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Buscando tendências...")
                            } else {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Gerar 5 Temas em Alta")
                            }
                        }
                    }

                    items(suggestions) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Gancho 0-3s: \"${item.hookIdea}\"",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                Text(
                                    text = "Por que funciona: ${item.whyItWorks}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                OutlinedButton(
                                    onClick = {
                                        // Save as a draft idea
                                        val newScript = ShortsScript(
                                            title = item.title,
                                            category = themeNiche,
                                            hook = item.hookIdea,
                                            bodyContent = "",
                                            callToAction = "",
                                            visualNotes = item.whyItWorks,
                                            targetDurationSeconds = 30,
                                            status = ScriptStatus.IDEA
                                        )
                                        onSaveScript(newScript)
                                        Toast.makeText(context, "Tema salvo como ideia!", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Salvar como Ideia", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: RESUMO RÁPIDO
                    item {
                        Text(
                            text = "Resumir Transcrição ou Roteiro",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Cole uma transcrição de vídeo ou roteiro bruto para extrair os pontos centrais e avaliar o ritmo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = rawTextToSummarize,
                            onValueChange = { rawTextToSummarize = it },
                            label = { Text("Texto da Transcrição ou Roteiro") },
                            placeholder = { Text("Cole aqui o texto completo...") },
                            modifier = Modifier.fillMaxWidth().testTag("ai_raw_text_input"),
                            minLines = 6,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    item {
                        Button(
                            onClick = {
                                if (rawTextToSummarize.isBlank()) {
                                    Toast.makeText(context, "Cole um texto para resumir", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isSummarizing = true
                                scope.launch {
                                    val result = geminiService.summarizeScriptWithThinking(rawTextToSummarize)
                                    isSummarizing = false
                                    if (result.isSuccess) {
                                        summaryResult = result.getOrNull() ?: ""
                                    } else {
                                        Toast.makeText(context, "Erro: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            enabled = !isSummarizing,
                            modifier = Modifier.fillMaxWidth().testTag("ai_summarize_submit_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (isSummarizing) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Resumindo com High Thinking...")
                            } else {
                                Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Resumir Conteúdo")
                            }
                        }
                    }

                    if (summaryResult.isNotBlank()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Resultado da Análise:",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = summaryResult,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
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
