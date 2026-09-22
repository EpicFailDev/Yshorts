package com.example.domain.model

enum class NavSection(val title: String) {
    DASHBOARD("Dashboard"),
    UPLOADS("Uploads"),
    SEO("SEO"),
    COMENTARIOS("Comentários"),
    ANALISES("Retenção"),
    CONFIGURACOES("Configurações")
}

enum class ViewDisplayMode {
    MOCKUP_PRESENTATION,
    FOCUSED_INTERACTIVE
}

enum class VideoUploadStatus(val label: String) {
    AGENDADO("Agendado"),
    RASCUNHO("Rascunho"),
    PUBLICADO("Publicado")
}

data class SparklineData(
    val title: String,
    val valueFormatted: String,
    val percentChange: String,
    val isPositive: Boolean = true,
    val points: List<Float>
)

data class DayViewStat(
    val day: String,
    val valueK: Float
)

data class TubeMasterVideo(
    val id: String,
    val title: String,
    val duration: String,
    val status: VideoUploadStatus,
    val scheduledTime: String = "",
    val viewsCount: String = "",
    val likesCount: String = "",
    val commentsCount: String = "",
    val ratingBadge: String? = null,
    val description: String = "",
    val tags: List<String> = emptyList(),
    val thumbnailGradientStart: Long = 0xFF7F1D1D,
    val thumbnailGradientEnd: Long = 0xFF000000
)

data class RetentionPoint(
    val timestamp: String,
    val percentage: Float
)

data class DropMoment(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: String,
    val description: String,
    val dropPercent: Int,
    val reasonAnalysis: String = "",
    val actionableFix: String = ""
)

data class VideoRetentionAnalysis(
    val videoId: String = "v1",
    val videoTitle: String,
    val duration: String,
    val uploadDate: String,
    val retentionRate: String,
    val retentionChange: String,
    val avgWatchDuration: String,
    val avgWatchChange: String,
    val curvePoints: List<RetentionPoint>,
    val dropMoments: List<DropMoment>,
    val abandonmentCallout: String = "Ponto de abandono (4:32) 58%",
    val peakCallout: String = "Bom momento (7:15) 82%"
)

data class CommentItem(
    val id: String,
    val author: String,
    val text: String,
    val time: String,
    val tag: String,
    val tagColor: Long, // Color ARGB
    val isSpam: Boolean = false,
    val isPinned: Boolean = false,
    val category: String = "Dúvidas", // "Dúvidas", "Mais curtidos", "Spam", "Elogios"
    val creatorReply: String? = null,
    val likesCount: Int = 12
)

data class TitleSuggestion(
    val title: String,
    val estimatedCtr: String,
    val angle: String
)

data class SeoOptimizationResult(
    val topic: String,
    val seoScore: Int,
    val scoreBadge: String,
    val potential: String,
    val suggestedTitles: List<TitleSuggestion>,
    val commaSeparatedTags: String,
    val hashtagList: List<String>,
    val optimizedDescription: String,
    val thumbnailVisualConcept: String,
    val thumbnailPromptForAi: String
)

data class RetentionDiagnostic(
    val timestamp: String,
    val dropPercent: Int,
    val diagnosticReason: String,
    val recommendedFix: String,
    val editingTip: String
)

data class SceneScript(
    val timeRange: String,
    val narration: String,
    val visualPrompt: String,
    val sfxAndMusic: String
)

data class FacelessAutomationPackage(
    val niche: String,
    val title: String,
    val hook: String,
    val targetDurationSeconds: Int,
    val scenes: List<SceneScript>,
    val callToAction: String,
    val suggestedTags: List<String>,
    val thumbnailConcept: String
)
