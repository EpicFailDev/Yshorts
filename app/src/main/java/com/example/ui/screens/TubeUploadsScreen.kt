package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.util.ExportHelper

@Composable
fun TubeUploadsScreen(
    videos: List<TubeMasterVideo>,
    onOpenAiOptimize: () -> Unit,
    onOpenScheduleModal: () -> Unit,
    onDeleteVideo: (String) -> Unit,
    onUpdateVideoStatus: (String, VideoUploadStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Fila Geral", "Agendados", "Publicados", "Rascunhos")

    val filteredVideos = when (selectedTab) {
        0 -> videos
        1 -> videos.filter { it.status == VideoUploadStatus.AGENDADO }
        2 -> videos.filter { it.status == VideoUploadStatus.PUBLICADO }
        3 -> videos.filter { it.status == VideoUploadStatus.RASCUNHO }
        else -> videos
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fila de Uploads & Automação",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 21.sp),
                    color = TubeMasterWhite
                )
                Text(
                    text = "Gerencie publicações, horários ideais e exporte em CSV",
                    style = MaterialTheme.typography.bodySmall,
                    color = TubeMasterGray
                )
            }

            OutlinedButton(
                onClick = {
                    ExportHelper.exportMetadataCsv(context, videos)
                },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, TubeMasterBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TubeMasterWhite),
                modifier = Modifier.height(34.dp).testTag("export_csv_btn")
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("CSV", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            }
        }

        // Action Buttons Row: Agendar com IA & Otimizar com IA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onOpenScheduleModal,
                colors = ButtonDefaults.buttonColors(containerColor = TubeMasterRed, contentColor = TubeMasterWhite),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).height(40.dp).testTag("schedule_ai_btn")
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Agendar Novo Vídeo", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }

            Button(
                onClick = onOpenAiOptimize,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B), contentColor = TubeMasterWhite),
                border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).height(40.dp).testTag("open_optimize_btn")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Otimizar com IA", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = TubeMasterWhite,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = TubeMasterRed,
                        height = 2.dp
                    )
                }
            },
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                val count = when (index) {
                    0 -> videos.size
                    1 -> videos.count { it.status == VideoUploadStatus.AGENDADO }
                    2 -> videos.count { it.status == VideoUploadStatus.PUBLICADO }
                    3 -> videos.count { it.status == VideoUploadStatus.RASCUNHO }
                    else -> 0
                }
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = "$title ($count)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.5.sp
                            ),
                            color = if (selectedTab == index) TubeMasterWhite else TubeMasterGray,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                )
            }
        }

        // Videos List
        if (filteredVideos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum vídeo nesta categoria.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TubeMasterGray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredVideos, key = { it.id }) { video ->
                    var isMenuExpanded by remember { mutableStateOf(false) }

                    Surface(
                        color = TubeMasterCard,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TubeMasterBorder),
                        modifier = Modifier.fillMaxWidth().testTag("video_card_${video.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Thumbnail representation
                            Box(
                                modifier = Modifier
                                    .size(width = 66.dp, height = 46.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(video.thumbnailGradientStart), Color(video.thumbnailGradientEnd))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TubeMasterWhite.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                            }

                            // Details
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = video.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
                                    color = TubeMasterWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Duração: ${video.duration}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        color = TubeMasterGray
                                    )

                                    if (video.scheduledTime.isNotBlank() && video.scheduledTime != "—") {
                                        Text(
                                            text = "• ${video.scheduledTime}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                            color = TubeMasterGrayDark,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val (badgeBg, badgeColor) = when (video.status) {
                                        VideoUploadStatus.AGENDADO -> Color(0xFF1B2E1E) to TubeMasterGreen
                                        VideoUploadStatus.PUBLICADO -> Color(0xFF172554) to Color(0xFF60A5FA)
                                        VideoUploadStatus.RASCUNHO -> Color(0xFF27272A) to TubeMasterGray
                                    }

                                    Surface(shape = RoundedCornerShape(4.dp), color = badgeBg) {
                                        Text(
                                            text = video.status.label,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                            color = badgeColor,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    if (video.viewsCount.isNotBlank()) {
                                        Text(
                                            text = "★ ${video.viewsCount} views",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TubeMasterGray
                                        )
                                    }
                                }
                            }

                            // 3-dots Menu with Working Actions
                            Box {
                                IconButton(
                                    onClick = { isMenuExpanded = true },
                                    modifier = Modifier.size(32.dp).testTag("video_menu_btn_${video.id}")
                                ) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Opções", tint = TubeMasterGray)
                                }

                                DropdownMenu(
                                    expanded = isMenuExpanded,
                                    onDismissRequest = { isMenuExpanded = false },
                                    modifier = Modifier.background(TubeMasterCard).border(1.dp, TubeMasterBorder)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Copiar Título", color = TubeMasterWhite, fontSize = 12.sp) },
                                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TubeMasterGray, modifier = Modifier.size(16.dp)) },
                                        onClick = {
                                            ExportHelper.copyText(context, "Título", video.title)
                                            isMenuExpanded = false
                                        }
                                    )

                                    if (video.status != VideoUploadStatus.PUBLICADO) {
                                        DropdownMenuItem(
                                            text = { Text("Marcar como Publicado", color = TubeMasterGreen, fontSize = 12.sp) },
                                            leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TubeMasterGreen, modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                onUpdateVideoStatus(video.id, VideoUploadStatus.PUBLICADO)
                                                Toast.makeText(context, "Status atualizado para Publicado!", Toast.LENGTH_SHORT).show()
                                                isMenuExpanded = false
                                            }
                                        )
                                    }

                                    if (video.status != VideoUploadStatus.AGENDADO) {
                                        DropdownMenuItem(
                                            text = { Text("Agendar Vídeo", color = Color(0xFF60A5FA), fontSize = 12.sp) },
                                            leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                onUpdateVideoStatus(video.id, VideoUploadStatus.AGENDADO)
                                                Toast.makeText(context, "Status atualizado para Agendado!", Toast.LENGTH_SHORT).show()
                                                isMenuExpanded = false
                                            }
                                        )
                                    }

                                    if (video.status != VideoUploadStatus.RASCUNHO) {
                                        DropdownMenuItem(
                                            text = { Text("Mover para Rascunho", color = TubeMasterGray, fontSize = 12.sp) },
                                            leadingIcon = { Icon(Icons.Default.Drafts, contentDescription = null, tint = TubeMasterGray, modifier = Modifier.size(16.dp)) },
                                            onClick = {
                                                onUpdateVideoStatus(video.id, VideoUploadStatus.RASCUNHO)
                                                isMenuExpanded = false
                                            }
                                        )
                                    }

                                    DropdownMenuItem(
                                        text = { Text("Excluir Vídeo", color = TubeMasterRed, fontSize = 12.sp) },
                                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.size(16.dp)) },
                                        onClick = {
                                            onDeleteVideo(video.id)
                                            Toast.makeText(context, "Vídeo removido da fila", Toast.LENGTH_SHORT).show()
                                            isMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
