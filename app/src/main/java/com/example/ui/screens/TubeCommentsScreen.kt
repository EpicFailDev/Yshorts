package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

data class CommentItem(
    val id: String,
    val author: String,
    val text: String,
    val time: String,
    val tag: String,
    val tagColor: Color,
    val isSpam: Boolean = false
)

@Composable
fun TubeCommentsScreen(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dúvidas (IA)", "Mais curtidos", "Spam Detectado")

    val comments = listOf(
        CommentItem(
            id = "c1",
            author = "Lucas Dev",
            text = "Qual software você recomenda para edição de áudio?",
            time = "Há 2 horas",
            tag = "Respondido por IA",
            tagColor = TubeMasterGreen
        ),
        CommentItem(
            id = "c2",
            author = "Marina Creator",
            text = "Esse vídeo salvou meu canal! As thumbnails dobraram meu CTR.",
            time = "Há 4 horas",
            tag = "Sentimento Positivo",
            tagColor = Color(0xFF38BDF8)
        ),
        CommentItem(
            id = "c3",
            author = "BotPromo12",
            text = "Ganhe 10k seguidores grátis no link da bio!!!",
            time = "Há 6 horas",
            tag = "Spam Bloqueado",
            tagColor = TubeMasterRed,
            isSpam = true
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TubeMasterBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "Moderação de Comentários",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                color = TubeMasterWhite
            )
            Text(
                text = "Mais engajamento da comunidade com filtros inteligentes e respostas por IA",
                style = MaterialTheme.typography.bodySmall,
                color = TubeMasterGray
            )
        }

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
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(TubeMasterBorder))
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(comments, key = { it.id }) { comment ->
                Surface(
                    color = TubeMasterCard,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, TubeMasterBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF333333)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = comment.author.take(1),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TubeMasterWhite
                                    )
                                }
                                Text(
                                    text = comment.author,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TubeMasterWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = comment.time,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TubeMasterGrayDark,
                                    maxLines = 1
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = comment.tagColor.copy(alpha = 0.15f),
                                border = BorderStroke(0.5.dp, comment.tagColor)
                            ) {
                                Text(
                                    text = comment.tag,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                    color = comment.tagColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }

                        Text(
                            text = comment.text,
                            style = MaterialTheme.typography.bodySmall,
                            color = TubeMasterGray
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (comment.isSpam) {
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = TubeMasterRed, contentColor = TubeMasterWhite),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Excluir Spam", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
                                }
                            } else {
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = TubeMasterRed.copy(alpha = 0.15f), contentColor = TubeMasterRed),
                                    border = BorderStroke(1.dp, TubeMasterRed),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Responder com IA", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
