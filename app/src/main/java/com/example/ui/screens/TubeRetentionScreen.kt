package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.domain.model.DropMoment
import com.example.domain.model.VideoRetentionAnalysis
import com.example.ui.components.RetentionLineChart
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeRetentionScreen(
    analysis: VideoRetentionAnalysis,
    onAiRetentionAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Retenção", "Engajamento", "Público")
    var videoDropdownOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Column {
            Text(
                text = "Análise de Retenção",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = TubeMasterWhite
            )
            Text(
                text = "Entenda onde seu público assiste e desiste",
                style = MaterialTheme.typography.bodySmall,
                color = TubeMasterGray
            )
        }

        // Video Selector Dropdown Card
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { videoDropdownOpen = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mini thumbnail
                Box(
                    modifier = Modifier
                        .size(width = 54.dp, height = 34.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF991B1B), Color(0xFF1E293B))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = TubeMasterWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = analysis.videoTitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = TubeMasterWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${analysis.duration} • ${analysis.uploadDate}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGrayDark
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TubeMasterGray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        DropdownMenu(
            expanded = videoDropdownOpen,
            onDismissRequest = { videoDropdownOpen = false }
        ) {
            DropdownMenuItem(
                text = { Text("Como criar thumbnails que realmente funcionam", color = TubeMasterWhite) },
                onClick = { videoDropdownOpen = false }
            )
            DropdownMenuItem(
                text = { Text("Ferramentas de IA para criadores de conteúdo", color = TubeMasterGray) },
                onClick = { videoDropdownOpen = false }
            )
        }

        // Tabs: Retenção | Engajamento | Público - smooth scrollable, never breaks words
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = TubeMasterWhite,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = TubeMasterRed,
                    height = 2.dp
                )
            },
            divider = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TubeMasterBorder)
                )
            }
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tabTitle,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            ),
                            color = if (selectedTab == index) TubeMasterWhite else TubeMasterGray,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                )
            }
        }

        // 2 Metric Cards: Taxa de retenção e Duração média assistida
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Retention Rate Card
            Surface(
                modifier = Modifier.weight(1f),
                color = TubeMasterCard,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, TubeMasterBorder)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Taxa de retenção",
                        style = MaterialTheme.typography.labelSmall,
                        color = TubeMasterGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = analysis.retentionRate,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = TubeMasterWhite,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = analysis.retentionChange,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TubeMasterGreen,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "Média do vídeo",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGrayDark,
                        maxLines = 1
                    )
                }
            }

            // Avg Duration Card
            Surface(
                modifier = Modifier.weight(1f),
                color = TubeMasterCard,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, TubeMasterBorder)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Duração média assistida",
                        style = MaterialTheme.typography.labelSmall,
                        color = TubeMasterGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = analysis.avgWatchDuration,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = TubeMasterWhite,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = analysis.avgWatchChange,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TubeMasterGreen,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "Tempo por viewer",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGrayDark,
                        maxLines = 1
                    )
                }
            }
        }

        // Retention Line Chart
        RetentionLineChart(
            curvePoints = analysis.curvePoints,
            abandonmentCallout = analysis.abandonmentCallout,
            peakCallout = analysis.peakCallout,
            modifier = Modifier.fillMaxWidth()
        )

        // Section: "Principais momentos de abandono"
        Surface(
            color = TubeMasterCard,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, TubeMasterBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Principais momentos de abandono",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TubeMasterWhite
                    )

                    Button(
                        onClick = onAiRetentionAnalysis,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TubeMasterRed.copy(alpha = 0.15f),
                            contentColor = TubeMasterRed
                        ),
                        border = BorderStroke(1.dp, TubeMasterRed),
                        modifier = Modifier.height(28.dp).testTag("ai_retention_insight_btn")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Dicas IA", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
                    }
                }

                analysis.dropMoments.forEach { moment ->
                    DropMomentRow(moment = moment)
                }
            }
        }
    }
}

@Composable
fun DropMomentRow(
    moment: DropMoment,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Red timestamp badge
        Surface(
            shape = RoundedCornerShape(3.dp),
            color = Color(0xFF2A1012),
            border = BorderStroke(0.5.dp, TubeMasterRed)
        ) {
            Text(
                text = moment.timestamp,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = TubeMasterRed,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
        }

        // Description
        Text(
            text = moment.description,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TubeMasterGray,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )

        // Drop percent
        Text(
            text = "${moment.dropPercent}%",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = TubeMasterWhite
        )

        // Horizontal red bar indicator
        Box(
            modifier = Modifier
                .width(55.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFF262626))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(moment.dropPercent / 100f)
                    .height(6.dp)
                    .background(TubeMasterRed)
            )
        }
    }
}
