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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.domain.model.TubeMasterVideo
import com.example.domain.model.VideoUploadStatus
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeUploadsScreen(
    videos: List<TubeMasterVideo>,
    onScheduleWithAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Fila de Uploads", "Agendados", "Publicados (3)")

    val filteredVideos = when (selectedTab) {
        0 -> videos
        1 -> videos.filter { it.status == VideoUploadStatus.AGENDADO }
        else -> videos.filter { it.ratingBadge != null || it.status == VideoUploadStatus.PUBLICADO }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
            ) {
                Text(
                    text = "Automação de Uploads",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    ),
                    color = TubeMasterWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Deixe a IA cuidar do seu conteúdo",
                    style = MaterialTheme.typography.bodySmall,
                    color = TubeMasterGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Button: "Agendar com IA" - properly sized, never wraps vertically
            Button(
                onClick = onScheduleWithAi,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TubeMasterRed,
                    contentColor = TubeMasterWhite
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.testTag("schedule_with_ai_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Agendar com IA",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        // Tabs: Fila de Uploads | Agendados | Publicados (3)
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

        // Video Queue List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredVideos, key = { it.id }) { video ->
                UploadVideoRowItem(video = video)
            }
        }
    }
}

@Composable
fun UploadVideoRowItem(
    video: TubeMasterVideo,
    modifier: Modifier = Modifier
) {
    var menuOpen by remember { mutableStateOf(false) }

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
            // Thumbnail with gradient + Duration
            Box(
                modifier = Modifier
                    .size(width = 84.dp, height = 50.dp)
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

            // Info column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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

                // Status pill
                if (video.status == VideoUploadStatus.AGENDADO) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color(0xFF142B1B),
                        border = BorderStroke(0.5.dp, TubeMasterGreen)
                    ) {
                        Text(
                            text = "Agendado",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = TubeMasterGreen,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = video.scheduledTime,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGrayDark
                    )
                } else {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color(0xFF262626),
                        border = BorderStroke(0.5.dp, TubeMasterBorder)
                    ) {
                        Text(
                            text = "Rascunho",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = TubeMasterGray,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TubeMasterGrayDark
                    )
                }
            }

            // More Options Menu
            Box {
                IconButton(
                    onClick = { menuOpen = true },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Mais opções",
                        tint = TubeMasterGray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Otimizar com IA", color = TubeMasterRed) },
                        onClick = { menuOpen = false },
                        leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.size(16.dp)) }
                    )
                    DropdownMenuItem(
                        text = { Text("Alterar Horário", color = TubeMasterWhite) },
                        onClick = { menuOpen = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Remover da Fila", color = TubeMasterGray) },
                        onClick = { menuOpen = false }
                    )
                }
            }
        }
    }
}
