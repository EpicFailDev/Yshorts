package com.example.domain.model

enum class ScriptStatus(val label: String) {
    IDEA("Ideia"),
    DRAFT("Rascunho"),
    READY("Pronto para Gravar"),
    RECORDED("Gravado"),
    PUBLISHED("Publicado");

    companion object {
        fun fromLabel(label: String): ScriptStatus {
            return entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DRAFT
        }
    }
}

enum class ThemeMode(val label: String) {
    AUTO("Automático (Sistema)"),
    LIGHT("Claro"),
    DARK("Escuro (Economia de Bateria)")
}

data class ShortsScript(
    val id: Long = 0,
    val title: String,
    val category: String = "Geral",
    val hook: String = "", // Gancho inicial (0-3s)
    val bodyContent: String = "", // Transcrição / Roteiro principal
    val callToAction: String = "", // CTA final (45-60s)
    val visualNotes: String = "", // Anotações visuais / B-roll
    val targetDurationSeconds: Int = 45, // 15, 30, 45, 60s
    val status: ScriptStatus = ScriptStatus.DRAFT,
    val tags: String = "",
    val aiSummary: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val firestoreId: String? = null
) {
    // Estimativa de duração para fala em português (~130 a 160 palavras por minuto, ~2.5 palavras por segundo)
    val totalWords: Int
        get() {
            val allText = "$hook $bodyContent $callToAction".trim()
            if (allText.isEmpty()) return 0
            return allText.split("\\s+".toRegex()).count { it.isNotEmpty() }
        }

    val estimatedSeconds: Int
        get() = if (totalWords == 0) 0 else ((totalWords / 2.5).toInt()).coerceAtLeast(1)

    fun toFormattedPlainText(): String {
        return buildString {
            appendLine("=== ROTEIRO YOUTUBE SHORTS ===")
            appendLine("Título: $title")
            appendLine("Categoria: $category")
            appendLine("Status: ${status.label}")
            appendLine("Duração Alvo: ${targetDurationSeconds}s (Estimado: ~${estimatedSeconds}s | $totalWords palavras)")
            if (tags.isNotBlank()) appendLine("Tags: $tags")
            appendLine("----------------------------------------")
            if (hook.isNotBlank()) {
                appendLine("[GANCHO INICIAL - 0 a 3s]")
                appendLine(hook)
                appendLine()
            }
            if (bodyContent.isNotBlank()) {
                appendLine("[ROTEIRO / TRANSCRIÇÃO - 3 a 45s]")
                appendLine(bodyContent)
                appendLine()
            }
            if (callToAction.isNotBlank()) {
                appendLine("[CHAMADA PARA AÇÃO (CTA) - 45 a 60s]")
                appendLine(callToAction)
                appendLine()
            }
            if (visualNotes.isNotBlank()) {
                appendLine("[NOTAS VISUAIS / EDIÇÃO / B-ROLL]")
                appendLine(visualNotes)
                appendLine()
            }
            if (aiSummary.isNotBlank()) {
                appendLine("[RESUMO INTELIGENTE (IA)]")
                appendLine(aiSummary)
                appendLine()
            }
            appendLine("----------------------------------------")
            appendLine("Gerado por Roteiros Shorts")
        }
    }
}
