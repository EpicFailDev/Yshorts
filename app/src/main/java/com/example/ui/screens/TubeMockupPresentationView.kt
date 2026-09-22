package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DayViewStat
import com.example.domain.model.NavSection
import com.example.domain.model.SparklineData
import com.example.domain.model.TubeMasterVideo
import com.example.domain.model.VideoRetentionAnalysis
import com.example.ui.components.TubeMasterSidebar
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeMockupPresentationView(
    metrics: List<SparklineData>,
    sevenDaysStats: List<DayViewStat>,
    videos: List<TubeMasterVideo>,
    retentionAnalysis: VideoRetentionAnalysis,
    onOpenScreen: (NavSection) -> Unit,
    onOpenAiOptimizer: () -> Unit,
    onScheduleWithAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0A),
                        TubeMasterBg,
                        Color(0xFF080808)
                    )
                )
            )
    ) {
        // Presentation Hero Top Slogan & Showcase Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Title & Slogan
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(TubeMasterRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = TubeMasterWhite,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TubeMaster ",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = TubeMasterWhite
                    )
                    Text(
                        text = "AI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        ),
                        color = TubeMasterRed
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Automatize   •   Otimize   •   Cresça",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    ),
                    color = TubeMasterGray
                )
            }

            // Right Accent "MAIS TEMPO PARA O QUE REALMENTE IMPORTA"
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "MAIS TEMPO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp
                    ),
                    color = TubeMasterWhite
                )
                Text(
                    text = "PARA O QUE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp
                    ),
                    color = TubeMasterGray
                )
                Text(
                    text = "REALMENTE IMPORTA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp
                    ),
                    color = TubeMasterGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(2.dp)
                        .background(TubeMasterRed)
                )
            }
        }

        val coroutineScope = rememberCoroutineScope()

        // Quick Jump Control Bar for the 3 Devices
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = TubeMasterCard,
                border = BorderStroke(0.8.dp, TubeMasterBorder),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        coroutineScope.launch { scrollState.animateScrollTo(0) }
                    }
            ) {
                Text(
                    text = "1. Uploads",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    color = TubeMasterWhite,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = TubeMasterCard,
                border = BorderStroke(0.8.dp, TubeMasterRed),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        coroutineScope.launch { scrollState.animateScrollTo(scrollState.maxValue / 2) }
                    }
            ) {
                Text(
                    text = "2. Dashboard (Mac)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    color = TubeMasterRed,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = TubeMasterCard,
                border = BorderStroke(0.8.dp, TubeMasterBorder),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        coroutineScope.launch { scrollState.animateScrollTo(scrollState.maxValue) }
                    }
            ) {
                Text(
                    text = "3. Retenção",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    color = TubeMasterWhite,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }

        // 3 Screens Side-by-Side Presentation Mockup Carousel
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ================= SCREEN 2 (LEFT): Automação de Uploads =================
            MockupDeviceFrame(
                deviceTitle = "Automação de Uploads",
                isCenterLaptop = false,
                onClick = { onOpenScreen(NavSection.UPLOADS) },
                modifier = Modifier
                    .width(330.dp)
                    .fillMaxHeight(0.95f)
                    .testTag("mockup_screen_uploads")
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Mini Sidebar
                    MiniSidebarVisual(selectedSection = NavSection.UPLOADS)

                    // Content
                    Box(modifier = Modifier.weight(1f)) {
                        TubeUploadsScreen(
                            videos = videos,
                            onScheduleWithAi = onScheduleWithAi
                        )
                    }
                }
            }

            // ================= SCREEN 1 (CENTER): Dashboard Principal (MacBook Pro) =================
            MockupDeviceFrame(
                deviceTitle = "Dashboard Principal (MacBook Pro)",
                isCenterLaptop = true,
                onClick = { onOpenScreen(NavSection.DASHBOARD) },
                modifier = Modifier
                    .width(420.dp)
                    .fillMaxHeight(0.98f)
                    .testTag("mockup_screen_dashboard")
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Mini Sidebar
                    MiniSidebarVisual(selectedSection = NavSection.DASHBOARD)

                    // Content
                    Box(modifier = Modifier.weight(1f)) {
                        TubeDashboardScreen(
                            metrics = metrics,
                            sevenDaysStats = sevenDaysStats,
                            recentVideos = videos,
                            onOpenAiOptimizer = onOpenAiOptimizer
                        )
                    }
                }
            }

            // ================= SCREEN 3 (RIGHT): Análise de Retenção =================
            MockupDeviceFrame(
                deviceTitle = "Análise de Retenção",
                isCenterLaptop = false,
                onClick = { onOpenScreen(NavSection.ANALISES) },
                modifier = Modifier
                    .width(330.dp)
                    .fillMaxHeight(0.95f)
                    .testTag("mockup_screen_retention")
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Mini Sidebar
                    MiniSidebarVisual(selectedSection = NavSection.ANALISES)

                    // Content
                    Box(modifier = Modifier.weight(1f)) {
                        TubeRetentionScreen(
                            analysis = retentionAnalysis,
                            onAiRetentionAnalysis = onOpenAiOptimizer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MockupDeviceFrame(
    deviceTitle: String,
    isCenterLaptop: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Outer Sleek Device Frame
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF141414),
            border = BorderStroke(
                width = if (isCenterLaptop) 2.dp else 1.dp,
                brush = Brush.verticalGradient(
                    colors = if (isCenterLaptop) {
                        listOf(TubeMasterRed.copy(alpha = 0.5f), Color(0xFF2A2A2A), Color(0xFF111111))
                    } else {
                        listOf(Color(0xFF333333), Color(0xFF1A1A1A))
                    }
                )
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top device bar (MacBook camera notch or tablet bar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF111111))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 3 window buttons (Mac style)
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFFF5F56)))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF27C93F)))
                    }

                    Text(
                        text = deviceTitle,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGrayDark
                    )

                    Box(modifier = Modifier.size(7.dp))
                }

                // Inner Screen View
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(TubeMasterBg)
                ) {
                    content()
                }
            }
        }

        // Base of MacBook if center
        if (isCenterLaptop) {
            Surface(
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                color = Color(0xFF202020),
                modifier = Modifier
                    .width(220.dp)
                    .height(6.dp)
            ) {}
            Text(
                text = "MacBook Pro",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = TubeMasterGrayDark,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun MiniSidebarVisual(
    selectedSection: NavSection,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(42.dp)
            .fillMaxHeight(),
        color = Color(0xFF121212),
        border = BorderStroke(0.5.dp, TubeMasterBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            NavSection.values().forEach { section ->
                val isSelected = section == selectedSection
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) TubeMasterRed else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) TubeMasterWhite else TubeMasterGrayDark)
                    )
                }
            }
        }
    }
}
