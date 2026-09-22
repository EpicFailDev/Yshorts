package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DayViewStat
import com.example.domain.model.SparklineData
import com.example.domain.model.TubeMasterVideo
import com.example.ui.components.SevenDaysBarChart
import com.example.ui.components.SparklineMetricCard
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
fun TubeDashboardScreen(
    metrics: List<SparklineData>,
    sevenDaysStats: List<DayViewStat>,
    recentVideos: List<TubeMasterVideo>,
    onOpenAiOptimizer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var periodDropdownOpen by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("Últimos 28 dias") }
    val periods = listOf("Últimos 7 dias", "Últimos 28 dias", "Últimos 90 dias", "Ano de 2025")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dashboard Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = TubeMasterWhite
                )
                Text(
                    text = "Visão geral do seu canal",
                    style = MaterialTheme.typography.bodySmall,
                    color = TubeMasterGray
                )
            }

            // Period Selector Dropdown
            Box {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TubeMasterCard,
                    border = BorderStroke(1.dp, TubeMasterBorder),
                    modifier = Modifier
                        .clickable { periodDropdownOpen = true }
                        .testTag("period_selector_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = TubeMasterGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = selectedPeriod,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = TubeMasterWhite
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TubeMasterGray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = periodDropdownOpen,
                    onDismissRequest = { periodDropdownOpen = false }
                ) {
                    periods.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p, color = if (p == selectedPeriod) TubeMasterRed else TubeMasterWhite) },
                            onClick = {
                                selectedPeriod = p
                                periodDropdownOpen = false
                            }
                        )
                    }
                }
            }
        }

        // 4 Key Metric Cards (Visualizações, Inscritos, Receita, Tempo de exibição)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2
        ) {
            metrics.forEach { metric ->
                SparklineMetricCard(
                    metric = metric,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Middle Row: 7 Days Views Bar Chart + AI Optimization Card
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Bar Chart
            SevenDaysBarChart(
                daysData = sevenDaysStats,
                modifier = Modifier.fillMaxWidth()
            )

            // AI Optimization Promo Card
            Surface(
                color = TubeMasterCard,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // AI Icon inside red circle
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(TubeMasterRed.copy(alpha = 0.15f))
                            .border(1.dp, TubeMasterRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ai",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            ),
                            color = TubeMasterRed
                        )
                    }

                    Text(
                        text = "Otimização com IA",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TubeMasterWhite
                    )

                    Text(
                        text = "Melhore seus títulos, descrições, tags e thumbnails em segundos.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = TubeMasterGray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Button(
                        onClick = onOpenAiOptimizer,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TubeMasterRed,
                            contentColor = TubeMasterWhite
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("dashboard_optimize_ai_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Otimizar com IA",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }

        // Section: "Últimos vídeos"
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Últimos vídeos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TubeMasterWhite
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "Ver todos",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = TubeMasterGray
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TubeMasterGray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            recentVideos.take(2).forEach { video ->
                VideoCompactCard(video = video)
            }
        }
    }
}

@Composable
fun VideoCompactCard(
    video: TubeMasterVideo,
    modifier: Modifier = Modifier
) {
    Surface(
        color = TubeMasterCard,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, TubeMasterBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mock thumbnail with gradient + play badge + duration
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 48.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(video.thumbnailGradientStart),
                                Color(video.thumbnailGradientEnd)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = TubeMasterWhite.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )

                // Duration badge
                Surface(
                    shape = RoundedCornerShape(2.dp),
                    color = Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp)
                ) {
                    Text(
                        text = video.duration,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, fontWeight = FontWeight.Bold),
                        color = TubeMasterWhite,
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                    )
                }
            }

            // Title and Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    ),
                    color = TubeMasterWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${video.duration} • 12/06/2025",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = TubeMasterGrayDark
                )

                // Stats: Views, Likes, Comments
                if (video.viewsCount.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = TubeMasterGray, modifier = Modifier.size(11.dp))
                            Text(video.viewsCount, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGray)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Default.ThumbUpOffAlt, contentDescription = null, tint = TubeMasterGray, modifier = Modifier.size(11.dp))
                            Text(video.likesCount, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGray)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = TubeMasterGray, modifier = Modifier.size(11.dp))
                            Text(video.commentsCount, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGray)
                        }
                    }
                }
            }

            // Rating Badge (Excelente / Bom)
            if (video.ratingBadge != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF142B1B),
                    border = BorderStroke(0.8.dp, TubeMasterGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TubeMasterGreen,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = video.ratingBadge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TubeMasterGreen
                        )
                    }
                }
            }
        }
    }
}
