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
                status = VideoUploadStatus.PUBLICADO,
                scheduledTime = "01/06/2025 • 12:00",
                viewsCount = "56.4K",
                likesCount = "4.2K",
                commentsCount = "480",
                ratingBadge = "Excelente",
                thumbnailGradientStart = 0xFF831843,
                thumbnailGradientEnd = 0xFF09090B
            )
        )
    )
    val uploadQueue: Flow<List<TubeMasterVideo>> = _uploadQueue.asStateFlow()

    private val sampleRetentionAnalyses = listOf(
        VideoRetentionAnalysis(
            videoId = "v1",
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
                RetentionPoint("4:32", 58f),
                RetentionPoint("6:00", 68f),
                RetentionPoint("7:15", 82f),
                RetentionPoint("8:00", 55f),
                RetentionPoint("10:00", 48f),
                RetentionPoint("12:34", 40f)
            ),
            dropMoments = listOf(
                DropMoment(
                    timestamp = "0:45",
                    description = "Introdução longa",
                    dropPercent = 42,
                    reasonAnalysis = "Explicação antes de mostrar a prova de conceito. O espectador quer ver a thumbnail antes da teoria.",
                    actionableFix = "Corte 20s de introdução. Mostre a thumbnail de 1 milhão de views no segundo 5."
                ),
                DropMoment(
                    timestamp = "4:32",
                    description = "Explicação técnica",
                    dropPercent = 58,
                    reasonAnalysis = "Uso de termos de Photoshop sem ilustração na tela. Ritmo caiu 40%.",
                    actionableFix = "Adicione um zoom 1.2x e destaque o texto com efeito sonoro 'click' e seta indicativa."
                ),
                DropMoment(
                    timestamp = "8:17",
                    description = "Chamada para ação",
                    dropPercent = 36,
                    reasonAnalysis = "CTA demorado pedindo like, inscrição e comentário ao mesmo tempo.",
                    actionableFix = "Faça apenas 1 pedido rápido (ex: 'comente seu nicho') integrado organicamente ao conteúdo."
                ),
                DropMoment(
                    timestamp = "11:50",
                    description = "Final do vídeo",
                    dropPercent = 28,
                    reasonAnalysis = "Despedida tradicional ('Por hoje é só pessoal').",
                    actionableFix = "Conecte diretamente com um card para o próximo vídeo sem avisar que está acabando."
                )
            ),
            abandonmentCallout = "Ponto de abandono (4:32) 58%",
            peakCallout = "Bom momento (7:15) 82%"
        ),
        VideoRetentionAnalysis(
            videoId = "v2",
            videoTitle = "Ferramentas de IA para criadores de conteúdo",
            duration = "18:20",
            uploadDate = "14/06/2025",
            retentionRate = "54,2%",
            retentionChange = "↑ 8,1%",
            avgWatchDuration = "9:56",
            avgWatchChange = "↑ 12,0%",
            curvePoints = listOf(
                RetentionPoint("0:00", 100f),
                RetentionPoint("3:00", 78f),
                RetentionPoint("6:00", 61f),
                RetentionPoint("9:10", 45f),
                RetentionPoint("12:00", 55f),
                RetentionPoint("15:00", 48f),
                RetentionPoint("18:20", 35f)
            ),
            dropMoments = listOf(
                DropMoment(
                    timestamp = "9:10",
                    description = "Demonstração de tela parada",
                    dropPercent = 45,
                    reasonAnalysis = "A tela ficou congelada enquanto o criador explicava um prompt sem movimento.",
                    actionableFix = "Acelere a digitação do prompt em 2.5x e adicione música de fundo de suspense."
                )
            ),
            abandonmentCallout = "Ponto de abandono (9:10) 45%",
            peakCallout = "Bom momento (3:00) 78%"
        )
    )

    private val _retentionAnalysis = MutableStateFlow(sampleRetentionAnalyses.first())
    val retentionAnalysis: Flow<VideoRetentionAnalysis> = _retentionAnalysis.asStateFlow()

    // Interactive Comments Stream
    private val _comments = MutableStateFlow(
        listOf(
            com.example.domain.model.CommentItem(
                id = "c1",
                author = "Lucas Dev",
                text = "Qual software você recomenda para edição de áudio de Shorts?",
                time = "Há 2 horas",
                tag = "Pergunta",
                tagColor = 0xFF22C55E,
                category = "Dúvidas",
                creatorReply = null,
                likesCount = 18
            ),
            com.example.domain.model.CommentItem(
                id = "c2",
                author = "Canal TechBR",
                text = "Esse vídeo salvou meu canal! Aumentei 30% da minha retenção no primeiro minuto só aplicando a dica do gancho.",
                time = "Há 4 horas",
                tag = "Feedback Positivo",
                tagColor = 0xFF3B82F6,
                category = "Mais curtidos",
                creatorReply = "Sensacional meu amigo! Esse é o poder de cortar a introdução longa. Bora rumo aos 100k! 🚀",
                likesCount = 42,
                isPinned = true
            ),
            com.example.domain.model.CommentItem(
                id = "c3",
                author = "Invest_Fast_100x",
                text = "CLIQUE AQUI PARA GANHAR CRIPTO GRÁTIS NO WHATSAPP +55 11 99999-9999",
                time = "Há 5 horas",
                tag = "Spam Detectado",
                tagColor = 0xFFEF4444,
                isSpam = true,
                category = "Spam",
                creatorReply = null,
                likesCount = 0
            ),
            com.example.domain.model.CommentItem(
                id = "c4",
                author = "Ana Criadora",
                text = "Como você faz para manter as cores da thumbnail tão chamativas sem estourar?",
                time = "Há 6 horas",
                tag = "Pergunta",
                tagColor = 0xFF22C55E,
                category = "Dúvidas",
                creatorReply = null,
                likesCount = 9
            ),
            com.example.domain.model.CommentItem(
                id = "c5",
                author = "Gabriel Motion",
                text = "Achei o ritmo desse vídeo muito bom, os cortes aos 4 minutos deram uma dinâmica perfeita.",
                time = "Há 8 horas",
                tag = "Elogio",
                tagColor = 0xFFEAB308,
                category = "Mais curtidos",
                creatorReply = null,
                likesCount = 14
            )
        )
    )
    val comments: Flow<List<com.example.domain.model.CommentItem>> = _comments.asStateFlow()

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

    fun deleteVideo(videoId: String) {
        _uploadQueue.value = _uploadQueue.value.filterNot { it.id == videoId }
    }

    fun updateVideoStatus(videoId: String, newStatus: VideoUploadStatus) {
        _uploadQueue.value = _uploadQueue.value.map {
            if (it.id == videoId) it.copy(status = newStatus) else it
        }
    }

    fun updateVideoTitle(videoId: String, newTitle: String) {
        _uploadQueue.value = _uploadQueue.value.map {
            if (it.id == videoId) it.copy(title = newTitle) else it
        }
    }

    fun selectRetentionVideo(videoId: String) {
        val found = sampleRetentionAnalyses.find { it.videoId == videoId }
        if (found != null) {
            _retentionAnalysis.value = found
        }
    }

    fun replyToComment(commentId: String, replyText: String) {
        _comments.value = _comments.value.map {
            if (it.id == commentId) {
                it.copy(
                    creatorReply = replyText,
                    tag = "Respondido por IA",
                    tagColor = 0xFF22C55E
                )
            } else it
        }
    }

    fun toggleCommentSpam(commentId: String) {
        _comments.value = _comments.value.map {
            if (it.id == commentId) {
                val newSpam = !it.isSpam
                it.copy(
                    isSpam = newSpam,
                    tag = if (newSpam) "Spam Marcado" else "Pergunta",
                    tagColor = if (newSpam) 0xFFEF4444 else 0xFF22C55E
                )
            } else it
        }
    }

    fun toggleCommentPin(commentId: String) {
        _comments.value = _comments.value.map {
            if (it.id == commentId) it.copy(isPinned = !it.isPinned) else it
        }
    }

    fun deleteComment(commentId: String) {
        _comments.value = _comments.value.filterNot { it.id == commentId }
    }

    fun addComment(comment: com.example.domain.model.CommentItem) {
        _comments.value = listOf(comment) + _comments.value
    }
}
