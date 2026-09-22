package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DayViewStat
import com.example.domain.model.NavSection
import com.example.domain.model.SparklineData
import com.example.domain.model.TubeMasterVideo
import com.example.domain.model.VideoRetentionAnalysis
import com.example.domain.model.ViewDisplayMode
import com.example.ui.components.TubeAiOptimizeModal
import com.example.ui.components.TubeMasterFooterBar
import com.example.ui.components.TubeMasterHeroBanner
import com.example.ui.components.TubeMasterSidebar
import com.example.ui.components.TubeScheduleModal
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeMasterMainScreen(
    displayMode: ViewDisplayMode,
    currentSection: NavSection,
    metrics: List<SparklineData>,
    sevenDaysStats: List<DayViewStat>,
    videos: List<TubeMasterVideo>,
    retentionAnalysis: VideoRetentionAnalysis,
    comments: List<com.example.domain.model.CommentItem>,
    seoResult: com.example.domain.model.SeoOptimizationResult,
    isGeneratingSeo: Boolean,
    isAiOptimizeOpen: Boolean,
    isScheduleOpen: Boolean,
    onToggleDisplayMode: (ViewDisplayMode) -> Unit,
    onSelectSection: (NavSection) -> Unit,
    onOpenAiOptimizer: () -> Unit,
    onCloseAiOptimizer: () -> Unit,
    onOpenSchedule: () -> Unit,
    onCloseSchedule: () -> Unit,
    onScheduleVideo: (title: String, duration: String, scheduledTime: String) -> Unit,
    onGenerateSeo: (String) -> Unit,
    onReplyToComment: (commentId: String, replyText: String) -> Unit,
    onGenerateAiReplies: (author: String, comment: String, onResult: (List<String>) -> Unit) -> Unit,
    onToggleSpam: (commentId: String) -> Unit,
    onTogglePin: (commentId: String) -> Unit,
    onDeleteComment: (commentId: String) -> Unit,
    onAddComment: (author: String, text: String, tag: String) -> Unit,
    onDeleteVideo: (String) -> Unit,
    onUpdateVideoStatus: (String, com.example.domain.model.VideoUploadStatus) -> Unit,
    onSelectRetentionVideo: (String) -> Unit,
    onDiagnoseRetention: (videoTitle: String, timestamp: String, desc: String, dropPercent: Int, onResult: (com.example.domain.model.RetentionDiagnostic) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTabletOrDesktop = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = TubeMasterBg,
            topBar = {
                TubeMasterHeroBanner(
                    currentMode = displayMode,
                    onToggleMode = onToggleDisplayMode
                )
            },
            bottomBar = {
                if (!isTabletOrDesktop && displayMode == ViewDisplayMode.FOCUSED_INTERACTIVE) {
                    // Mobile Bottom Navigation Bar
                    Surface(
                        color = TubeMasterCard,
                        border = BorderStroke(1.dp, TubeMasterBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        NavigationBar(
                            containerColor = TubeMasterCard,
                            tonalElevation = 0.dp,
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            NavigationBarItem(
                                selected = currentSection == NavSection.DASHBOARD,
                                onClick = { onSelectSection(NavSection.DASHBOARD) },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard", modifier = Modifier.size(20.dp)) },
                                label = {
                                    Text(
                                        text = "Dashboard",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TubeMasterWhite,
                                    selectedTextColor = TubeMasterWhite,
                                    indicatorColor = TubeMasterRed,
                                    unselectedIconColor = TubeMasterGray,
                                    unselectedTextColor = TubeMasterGray
                                ),
                                modifier = Modifier.testTag("nav_dashboard")
                            )

                            NavigationBarItem(
                                selected = currentSection == NavSection.UPLOADS,
                                onClick = { onSelectSection(NavSection.UPLOADS) },
                                icon = { Icon(Icons.Default.CloudUpload, contentDescription = "Uploads", modifier = Modifier.size(20.dp)) },
                                label = {
                                    Text(
                                        text = "Uploads",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TubeMasterWhite,
                                    selectedTextColor = TubeMasterWhite,
                                    indicatorColor = TubeMasterRed,
                                    unselectedIconColor = TubeMasterGray,
                                    unselectedTextColor = TubeMasterGray
                                ),
                                modifier = Modifier.testTag("nav_uploads")
                            )

                            NavigationBarItem(
                                selected = currentSection == NavSection.ANALISES,
                                onClick = { onSelectSection(NavSection.ANALISES) },
                                icon = { Icon(Icons.Default.Timeline, contentDescription = "Retenção", modifier = Modifier.size(20.dp)) },
                                label = {
                                    Text(
                                        text = "Retenção",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TubeMasterWhite,
                                    selectedTextColor = TubeMasterWhite,
                                    indicatorColor = TubeMasterRed,
                                    unselectedIconColor = TubeMasterGray,
                                    unselectedTextColor = TubeMasterGray
                                ),
                                modifier = Modifier.testTag("nav_retention")
                            )

                            NavigationBarItem(
                                selected = currentSection == NavSection.SEO,
                                onClick = { onSelectSection(NavSection.SEO) },
                                icon = { Icon(Icons.Default.Search, contentDescription = "SEO", modifier = Modifier.size(20.dp)) },
                                label = {
                                    Text(
                                        text = "SEO IA",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TubeMasterWhite,
                                    selectedTextColor = TubeMasterWhite,
                                    indicatorColor = TubeMasterRed,
                                    unselectedIconColor = TubeMasterGray,
                                    unselectedTextColor = TubeMasterGray
                                ),
                                modifier = Modifier.testTag("nav_seo")
                            )

                            NavigationBarItem(
                                selected = currentSection == NavSection.COMENTARIOS,
                                onClick = { onSelectSection(NavSection.COMENTARIOS) },
                                icon = { Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = "Comentários", modifier = Modifier.size(20.dp)) },
                                label = {
                                    Text(
                                        text = "Comentários",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = TubeMasterWhite,
                                    selectedTextColor = TubeMasterWhite,
                                    indicatorColor = TubeMasterRed,
                                    unselectedIconColor = TubeMasterGray,
                                    unselectedTextColor = TubeMasterGray
                                ),
                                modifier = Modifier.testTag("nav_comments")
                            )
                        }
                    }
                } else {
                    TubeMasterFooterBar()
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(TubeMasterBg)
            ) {
                when (displayMode) {
                    ViewDisplayMode.MOCKUP_PRESENTATION -> {
                        // 3-screens side by side mockup presentation
                        TubeMockupPresentationView(
                            metrics = metrics,
                            sevenDaysStats = sevenDaysStats,
                            videos = videos,
                            retentionAnalysis = retentionAnalysis,
                            onOpenScreen = { section -> onSelectSection(section) },
                            onOpenAiOptimizer = onOpenAiOptimizer,
                            onScheduleWithAi = onOpenSchedule
                        )
                    }

                    ViewDisplayMode.FOCUSED_INTERACTIVE -> {
                        if (isTabletOrDesktop) {
                            // Desktop / Tablet layout with sidebar
                            Row(modifier = Modifier.fillMaxSize()) {
                                TubeMasterSidebar(
                                    currentSection = currentSection,
                                    onSelectSection = onSelectSection
                                )

                                Box(modifier = Modifier.weight(1f)) {
                                    RenderActiveScreen(
                                        section = currentSection,
                                        metrics = metrics,
                                        sevenDaysStats = sevenDaysStats,
                                        videos = videos,
                                        retentionAnalysis = retentionAnalysis,
                                        comments = comments,
                                        seoResult = seoResult,
                                        isGeneratingSeo = isGeneratingSeo,
                                        onOpenAiOptimizer = onOpenAiOptimizer,
                                        onOpenSchedule = onOpenSchedule,
                                        onScheduleVideo = onScheduleVideo,
                                        onGenerateSeo = onGenerateSeo,
                                        onReplyToComment = onReplyToComment,
                                        onGenerateAiReplies = onGenerateAiReplies,
                                        onToggleSpam = onToggleSpam,
                                        onTogglePin = onTogglePin,
                                        onDeleteComment = onDeleteComment,
                                        onAddComment = onAddComment,
                                        onDeleteVideo = onDeleteVideo,
                                        onUpdateVideoStatus = onUpdateVideoStatus,
                                        onSelectRetentionVideo = onSelectRetentionVideo,
                                        onDiagnoseRetention = onDiagnoseRetention
                                    )
                                }
                            }
                        } else {
                            // Mobile clean full-width screen
                            Box(modifier = Modifier.fillMaxSize()) {
                                RenderActiveScreen(
                                    section = currentSection,
                                    metrics = metrics,
                                    sevenDaysStats = sevenDaysStats,
                                    videos = videos,
                                    retentionAnalysis = retentionAnalysis,
                                    comments = comments,
                                    seoResult = seoResult,
                                    isGeneratingSeo = isGeneratingSeo,
                                    onOpenAiOptimizer = onOpenAiOptimizer,
                                    onOpenSchedule = onOpenSchedule,
                                    onScheduleVideo = onScheduleVideo,
                                    onGenerateSeo = onGenerateSeo,
                                    onReplyToComment = onReplyToComment,
                                    onGenerateAiReplies = onGenerateAiReplies,
                                    onToggleSpam = onToggleSpam,
                                    onTogglePin = onTogglePin,
                                    onDeleteComment = onDeleteComment,
                                    onAddComment = onAddComment,
                                    onDeleteVideo = onDeleteVideo,
                                    onUpdateVideoStatus = onUpdateVideoStatus,
                                    onSelectRetentionVideo = onSelectRetentionVideo,
                                    onDiagnoseRetention = onDiagnoseRetention
                                )
                            }
                        }
                    }
                }

                // Interactive Dialogs
                if (isAiOptimizeOpen) {
                    TubeAiOptimizeModal(
                        onDismiss = onCloseAiOptimizer,
                        onApplyOptimization = { _ ->
                            onCloseAiOptimizer()
                        }
                    )
                }

                if (isScheduleOpen) {
                    TubeScheduleModal(
                        onDismiss = onCloseSchedule,
                        onSchedule = { title, duration, time ->
                            onScheduleVideo(title, duration, time)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RenderActiveScreen(
    section: NavSection,
    metrics: List<SparklineData>,
    sevenDaysStats: List<DayViewStat>,
    videos: List<TubeMasterVideo>,
    retentionAnalysis: VideoRetentionAnalysis,
    comments: List<com.example.domain.model.CommentItem>,
    seoResult: com.example.domain.model.SeoOptimizationResult,
    isGeneratingSeo: Boolean,
    onOpenAiOptimizer: () -> Unit,
    onOpenSchedule: () -> Unit,
    onScheduleVideo: (title: String, duration: String, time: String) -> Unit,
    onGenerateSeo: (String) -> Unit,
    onReplyToComment: (commentId: String, replyText: String) -> Unit,
    onGenerateAiReplies: (author: String, comment: String, onResult: (List<String>) -> Unit) -> Unit,
    onToggleSpam: (commentId: String) -> Unit,
    onTogglePin: (commentId: String) -> Unit,
    onDeleteComment: (commentId: String) -> Unit,
    onAddComment: (author: String, text: String, tag: String) -> Unit,
    onDeleteVideo: (String) -> Unit,
    onUpdateVideoStatus: (String, com.example.domain.model.VideoUploadStatus) -> Unit,
    onSelectRetentionVideo: (String) -> Unit,
    onDiagnoseRetention: (videoTitle: String, timestamp: String, desc: String, dropPercent: Int, onResult: (com.example.domain.model.RetentionDiagnostic) -> Unit) -> Unit
) {
    when (section) {
        NavSection.DASHBOARD -> TubeDashboardScreen(
            metrics = metrics,
            sevenDaysStats = sevenDaysStats,
            recentVideos = videos,
            onOpenAiOptimizer = onOpenAiOptimizer
        )
        NavSection.UPLOADS -> TubeUploadsScreen(
            videos = videos,
            onOpenAiOptimize = onOpenAiOptimizer,
            onOpenScheduleModal = onOpenSchedule,
            onDeleteVideo = onDeleteVideo,
            onUpdateVideoStatus = onUpdateVideoStatus
        )
        NavSection.ANALISES -> TubeRetentionScreen(
            retention = retentionAnalysis,
            onSelectVideo = onSelectRetentionVideo,
            onDiagnoseRetention = onDiagnoseRetention
        )
        NavSection.SEO -> TubeSeoScreen(
            seoResult = seoResult,
            isGenerating = isGeneratingSeo,
            onGenerateSeo = onGenerateSeo,
            onScheduleVideo = onScheduleVideo
        )
        NavSection.COMENTARIOS -> TubeCommentsScreen(
            comments = comments,
            onReplyToComment = onReplyToComment,
            onGenerateAiReplies = onGenerateAiReplies,
            onToggleSpam = onToggleSpam,
            onTogglePin = onTogglePin,
            onDeleteComment = onDeleteComment,
            onAddComment = onAddComment
        )
        NavSection.CONFIGURACOES -> TubeSettingsScreen(
            videos = videos,
            comments = comments
        )
    }
}
