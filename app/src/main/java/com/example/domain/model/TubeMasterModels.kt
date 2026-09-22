package com.example.domain.model

enum class NavSection(val title: String) {
    DASHBOARD("Dashboard"),
    UPLOADS("Uploads"),
    SEO("SEO"),
    COMENTARIOS("Comentários"),
    ANALISES("Análises"),
    CONFIGURACOES("Configurações")
}

enum class ViewDisplayMode {
    MOCKUP_PRESENTATION, // 3 telas lado a lado exatamente como no preview de alta fidelidade
    FOCUSED_INTERACTIVE  // Navegação interativa focada por tela
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
    val thumbnailGradientStart: Long = 0xFF7F1D1D,
    val thumbnailGradientEnd: Long = 0xFF000000
)

data class RetentionPoint(
    val timestamp: String,
    val percentage: Float
)

data class DropMoment(
    val timestamp: String,
    val description: String,
    val dropPercent: Int
)

data class VideoRetentionAnalysis(
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
