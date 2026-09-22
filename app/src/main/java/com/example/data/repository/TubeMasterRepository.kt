package com.example.data.repository

import com.example.domain.model.DayViewStat
import com.example.domain.model.DropMoment
import com.example.domain.model.RetentionPoint
import com.example.domain.model.SparklineData
import com.example.domain.model.TubeMasterVideo
import com.example.domain.model.VideoRetentionAnalysis
import com.example.domain.model.VideoUploadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TubeMasterRepository {

    private val _sparklineMetrics = MutableStateFlow(
        listOf(
            SparklineData(
                title = "Visualizações",
                valueFormatted = "248.532",
                percentChange = "↑ 32,5%",
                isPositive = true,
                points = listOf(14f, 18f, 22f, 19f, 25f, 31f, 38f, 34f, 42f, 48f)
            ),
            SparklineData(
                title = "Inscritos",
                valueFormatted = "+12.847",
                percentChange = "↑ 18,2%",
                isPositive = true,
                points = listOf(10f, 12f, 15f, 14f, 18f, 21f, 26f, 28f, 30f, 35f)
            ),
            SparklineData(
                title = "Receita estimada",
                valueFormatted = "R$ 4.328,76",
                percentChange = "↑ 27,4%",
                isPositive = true,
                points = listOf(20f, 22f, 28f, 26f, 32f, 35f, 39f, 44f, 42f, 50f)
            ),
            SparklineData(
                title = "Tempo de exibição",
                valueFormatted = "1.245,6 h",
                percentChange = "↑ 36,8%",
                isPositive = true,
                points = listOf(15f, 19f, 24f, 22f, 29f, 33f, 41f, 45f, 49f, 54f)
            )
        )
    )
    val sparklineMetrics: Flow<List<SparklineData>> = _sparklineMetrics.asStateFlow()

    private val _sevenDaysStats = MutableStateFlow(
        listOf(
            DayViewStat("03/06", 24f),
            DayViewStat("04/06", 42f),
            DayViewStat("05/06", 36f),
            DayViewStat("06/06", 58f),
            DayViewStat("07/06", 51f),
            DayViewStat("08/06", 74f),
            DayViewStat("09/06", 82f)
        )
    )
    val sevenDaysStats: Flow<List<DayViewStat>> = _sevenDaysStats.asStateFlow()

    private val _uploadQueue = MutableStateFlow(
        listOf(
            TubeMasterVideo(
                id = "v1",
                title = "Como criar thumbnails que realmente funcionam",
                duration = "12:34",
                status = VideoUploadStatus.AGENDADO,
                scheduledTime = "12/06/2025 • 15:00",
                viewsCount = "42.6K",
                likesCount = "2.8K",
                commentsCount = "312",
                ratingBadge = "Excelente",
                thumbnailGradientStart = 0xFF991B1B,
                thumbnailGradientEnd = 0xFF1E293B
            ),
            TubeMasterVideo(
                id = "v2",
                title = "Ferramentas de IA para criadores de conteúdo",
                duration = "18:20",
                status = VideoUploadStatus.AGENDADO,
                scheduledTime = "14/06/2025 • 10:00",
                viewsCount = "38.1K",
                likesCount = "2.1K",
                commentsCount = "198",
                ratingBadge = "Bom",
                thumbnailGradientStart = 0xFF1E3A8A,
                thumbnailGradientEnd = 0xFF4C1D95
            ),
            TubeMasterVideo(
                id = "v3",
                title = "Edição de vídeo no celular (passo a passo)",
                duration = "14:12",
                status = VideoUploadStatus.AGENDADO,
                scheduledTime = "18/06/2025 • 17:00",
                thumbnailGradientStart = 0xFFB91C1C,
                thumbnailGradientEnd = 0xFF0F172A
            ),
            TubeMasterVideo(
                id = "v4",
                title = "Estratégias para crescer no YouTube em 2025",
                duration = "16:48",
                status = VideoUploadStatus.RASCUNHO,
                scheduledTime = "—",
                thumbnailGradientStart = 0xFF7C2D12,
                thumbnailGradientEnd = 0xFF18181B
            ),
            TubeMasterVideo(
                id = "v5",
                title = "Análise completa do meu canal (resultados)",
                duration = "11:32",
                status = VideoUploadStatus.RASCUNHO,
                scheduledTime = "—",
                thumbnailGradientStart = 0xFF831843,
                thumbnailGradientEnd = 0xFF09090B
            )
        )
    )
    val uploadQueue: Flow<List<TubeMasterVideo>> = _uploadQueue.asStateFlow()

    private val _retentionAnalysis = MutableStateFlow(
        VideoRetentionAnalysis(
            videoTitle = "Como criar thumbnails que realmente funcionam",
            duration = "12:34",
            uploadDate = "12/06/2025",
            retentionRate = "62,8%",
            retentionChange = "↑ 14,2%",
            avgWatchDuration = "7:48",
            avgWatchChange = "↑ 22,5%",
            curvePoints = listOf(
                RetentionPoint("0:00", 100f),
                RetentionPoint("2:00", 72f),
                RetentionPoint("4:00", 64f),
                RetentionPoint("4:32", 58f), // Ponto de abandono
                RetentionPoint("6:00", 68f),
                RetentionPoint("7:15", 82f), // Ponto alto
                RetentionPoint("8:00", 55f),
                RetentionPoint("10:00", 48f),
                RetentionPoint("12:34", 40f)
            ),
            dropMoments = listOf(
                DropMoment("0:45", "Introdução (muito longa)", 42),
                DropMoment("4:32", "Explicação técnica", 58),
                DropMoment("8:17", "Chamada para ação", 36),
                DropMoment("11:50", "Final do vídeo", 28)
            ),
            abandonmentCallout = "Ponto de abandono (4:32) 58%",
            peakCallout = "Bom momento (7:15) 82%"
        )
    )
    val retentionAnalysis: Flow<VideoRetentionAnalysis> = _retentionAnalysis.asStateFlow()

    fun scheduleVideoWithAi(
        title: String,
        duration: String,
        scheduledDate: String
    ) {
        val newVideo = TubeMasterVideo(
            id = "v_${System.currentTimeMillis()}",
            title = title,
            duration = duration,
            status = VideoUploadStatus.AGENDADO,
            scheduledTime = scheduledDate,
            thumbnailGradientStart = 0xFFDC2626,
            thumbnailGradientEnd = 0xFF1E1E1E
        )
        _uploadQueue.value = listOf(newVideo) + _uploadQueue.value
    }
}
