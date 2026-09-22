package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.domain.model.ScriptStatus
import com.example.domain.model.ShortsScript
import com.example.util.ExportHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScriptScreen(
    initialScript: ShortsScript?,
    categories: List<String>,
    geminiService: GeminiService,
    onSave: (ShortsScript) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf(initialScript?.title ?: "") }
    var category by remember { mutableStateOf(initialScript?.category ?: "Geral") }
    var hook by remember { mutableStateOf(initialScript?.hook ?: "") }
    var bodyContent by remember { mutableStateOf(initialScript?.bodyContent ?: "") }
    var callToAction by remember { mutableStateOf(initialScript?.callToAction ?: "") }
    var visualNotes by remember { mutableStateOf(initialScript?.visualNotes ?: "") }
    var targetDuration by remember { mutableIntStateOf(initialScript?.targetDurationSeconds ?: 45) }
    var status by remember { mutableStateOf(initialScript?.status ?: ScriptStatus.DRAFT) }
    var tags by remember { mutableStateOf(initialScript?.tags ?: "") }
    var aiSummary by remember { mutableStateOf(initialScript?.aiSummary ?: "") }

    var isSummarizing by remember { mutableStateOf(false) }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    // Pacing calculations
    val allText = "$hook $bodyContent $callToAction".trim()
    val wordCount = if (allText.isEmpty()) 0 else allText.split("\\s+".toRegex()).count { it.isNotEmpty() }
    val estimatedSec = if (wordCount == 0) 0 else ((wordCount / 2.5).toInt()).coerceAtLeast(1)

    fun buildCurrentScript(): ShortsScript {
        return ShortsScript(
            id = initialScript?.id ?: 0L,
            title = title.trim().ifBlank { "Roteiro sem título" },
            category = category.trim().ifBlank { "Geral" },
            hook = hook.trim(),
            bodyContent = bodyContent.trim(),
            callToAction = callToAction.trim(),
            visualNotes = visualNotes.trim(),
            targetDurationSeconds = targetDuration,
            status = status,
            tags = tags.trim(),
            aiSummary = aiSummary.trim(),
            createdAt = initialScript?.createdAt ?: System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isFavorite = initialScript?.isFavorite ?: false,
            firestoreId = initialScript?.firestoreId
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialScript == null) "Novo Roteiro Shorts" else "Editar Roteiro",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val script = buildCurrentScript()
                            ExportHelper.exportAsTxt(context, script)
                        },
                        modifier = Modifier.testTag("export_txt_action")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Exportar TXT")
                    }
                    IconButton(
                        onClick = {
                            val script = buildCurrentScript()
                            ExportHelper.exportAsPdf(context, script)
                        },
                        modifier = Modifier.testTag("export_pdf_action")
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Exportar PDF")
                    }
                    IconButton(
                        onClick = {
                            val script = buildCurrentScript()
                            ExportHelper.copyToClipboard(context, script)
                        },
                        modifier = Modifier.testTag("copy_action")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copiar Roteiro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("cancel_btn")
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val script = buildCurrentScript()
                            onSave(script)
                            Toast.makeText(context, "Roteiro salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f).testTag("save_script_btn")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Salvar")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Pacing & Duration Real-Time Card (No shadows, flat border)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Ritmo do Shorts: ~$estimatedSec seg",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$wordCount palavras (meta: $targetDuration seg)",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (estimatedSec > targetDuration + 5) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Target duration selector chips
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(15, 30, 45, 60).forEach { sec ->
                            FilterChip(
                                selected = targetDuration == sec,
                                onClick = { targetDuration = sec },
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

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Vídeo Shorts") },
                placeholder = { Text("Ex: O segredo para não procrastinar...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("script_title_input"),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Category & Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Category
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoria") },
                    placeholder = { Text("Ex: Produtividade") },
                    singleLine = true,
                    modifier = Modifier.weight(1f).testTag("script_category_input"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Status dropdown
                ExposedDropdownMenuBox(
                    expanded = statusDropdownExpanded,
                    onExpandedChange = { statusDropdownExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = status.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = statusDropdownExpanded,
                        onDismissRequest = { statusDropdownExpanded = false }
                    ) {
                        ScriptStatus.entries.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    status = st
                                    statusDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Quick Category Suggestions
            val presetCategories = listOf("Dicas Rápidas", "Curiosidades", "Tutoriais", "Finanças", "Tecnologia", "Humor", "Produtividade")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                presetCategories.take(4).forEach { cat ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.weight(1f)
                    ) {
                        Button(
                            onClick = { category = cat },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = ButtonDefaults.TextButtonContentPadding
                        ) {
                            Text(cat, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                        }
                    }
                }
            }

            // SECTION 1: GANCHO INICIAL (0-3s)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1. Gancho Inicial (0 a 3s) ⚡",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Crucial para retenção",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                OutlinedTextField(
                    value = hook,
                    onValueChange = { hook = it },
                    placeholder = { Text("Ex: Se você comete esse erro todos os dias, pare agora mesmo...") },
                    modifier = Modifier.fillMaxWidth().testTag("script_hook_input"),
                    minLines = 2,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // SECTION 2: CORPO / TRANSCRIÇÃO (3-45s)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "2. Conteúdo Principal / Transcrição",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = bodyContent,
                    onValueChange = { bodyContent = it },
                    placeholder = { Text("Desenvolva o roteiro com cortes de raciocínio a cada 3 a 5 segundos sem enrolação...") },
                    modifier = Modifier.fillMaxWidth().testTag("script_body_input"),
                    minLines = 5,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // SECTION 3: CHAMADA PARA AÇÃO (CTA)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "3. Chamada para Ação (CTA final 5-10s)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = callToAction,
                    onValueChange = { callToAction = it },
                    placeholder = { Text("Ex: Você já sabia disso? Deixe nos comentários e inscreva-se para a parte 2!") },
                    modifier = Modifier.fillMaxWidth().testTag("script_cta_input"),
                    minLines = 2,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // SECTION 4: NOTAS VISUAIS / B-ROLL
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "4. Notas de Edição / B-Roll / Efeitos",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = visualNotes,
                    onValueChange = { visualNotes = it },
                    placeholder = { Text("Ex: Zoom nos 0:03, texto em vermelho na tela, som de sino no CTA...") },
                    modifier = Modifier.fillMaxWidth().testTag("script_notes_input"),
                    minLines = 2,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            // Tags
            OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags e Palavras-chave (separadas por vírgula)") },
                placeholder = { Text("shorts, viral, youtube, produtividade") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("script_tags_input"),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // AI RESUMO INTELIGENTE SECTION
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Resumo & Destaques com IA (Thinking Mode)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Button(
                            onClick = {
                                if (bodyContent.isBlank() && hook.isBlank()) {
                                    Toast.makeText(context, "Preencha o roteiro antes de resumir", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isSummarizing = true
                                scope.launch {
                                    val fullText = "Título: $title\nGancho: $hook\nConteúdo: $bodyContent\nCTA: $callToAction"
                                    val result = geminiService.summarizeScriptWithThinking(fullText)
                                    isSummarizing = false
                                    if (result.isSuccess) {
                                        aiSummary = result.getOrNull() ?: ""
                                        Toast.makeText(context, "Resumo gerado com sucesso!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Erro da IA: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            enabled = !isSummarizing,
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.testTag("ai_summarize_btn")
                        ) {
                            if (isSummarizing) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Resumir", color = MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    if (aiSummary.isNotBlank()) {
                        Text(
                            text = aiSummary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Gere automaticamente os pontos-chave e a avaliação do gancho para este roteiro.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
