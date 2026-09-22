package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.CommentItem
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeCommentsScreen(
    comments: List<CommentItem>,
    onReplyToComment: (commentId: String, replyText: String) -> Unit,
    onGenerateAiReplies: (author: String, comment: String, onResult: (List<String>) -> Unit) -> Unit,
    onToggleSpam: (commentId: String) -> Unit,
    onTogglePin: (commentId: String) -> Unit,
    onDeleteComment: (commentId: String) -> Unit,
    onAddComment: (author: String, text: String, tag: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Dúvidas IA", "Mais Curtidos", "Spam Detectado", "Respondidos")

    // State for Reply Modal
    var activeReplyComment by remember { mutableStateOf<CommentItem?>(null) }
    var aiReplyOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoadingReplies by remember { mutableStateOf(false) }
    var customReplyText by remember { mutableStateOf("") }

    // State for Add Comment Dialog
    var showAddCommentDialog by remember { mutableStateOf(false) }
    var newCommentAuthor by remember { mutableStateOf("") }
    var newCommentText by remember { mutableStateOf("") }

    // Filter comments according to selected tab
    val filteredComments = when (selectedTab) {
        0 -> comments.filter { !it.isSpam && (it.category == "Dúvidas" || it.text.contains("?")) }
        1 -> comments.filter { !it.isSpam }.sortedByDescending { it.likesCount }
        2 -> comments.filter { it.isSpam }
        3 -> comments.filter { it.creatorReply != null }
        else -> comments
    }.sortedByDescending { it.isPinned }

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
                    text = "Comentários & Moderação IA",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 21.sp),
                    color = TubeMasterWhite
                )
                Text(
                    text = "Respostas inteligentes na voz do canal & filtro anti-tóxico",
                    style = MaterialTheme.typography.bodySmall,
                    color = TubeMasterGray
                )
            }

            OutlinedButton(
                onClick = { showAddCommentDialog = true },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, TubeMasterBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TubeMasterWhite),
                modifier = Modifier.height(34.dp).testTag("add_comment_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Simular", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold))
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
                    0 -> comments.count { !it.isSpam && (it.category == "Dúvidas" || it.text.contains("?")) }
                    1 -> comments.count { !it.isSpam }
                    2 -> comments.count { it.isSpam }
                    3 -> comments.count { it.creatorReply != null }
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

        // Comments List
        if (filteredComments.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum comentário nesta aba.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TubeMasterGray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredComments, key = { it.id }) { comment ->
                    Surface(
                        color = TubeMasterCard,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (comment.isPinned) Color(0xFFEAB308).copy(alpha = 0.5f)
                            else if (comment.isSpam) TubeMasterRed.copy(alpha = 0.5f)
                            else TubeMasterBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("comment_card_${comment.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Top Row: Author, Date, Tag, Pinned
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(Color(comment.tagColor).copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = comment.author.take(1).uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color(comment.tagColor)
                                        )
                                    }

                                    Text(
                                        text = comment.author,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = TubeMasterWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (comment.isPinned) {
                                        Icon(
                                            Icons.Default.PushPin,
                                            contentDescription = "Fixado",
                                            tint = Color(0xFFEAB308),
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }

                                    Text(
                                        text = comment.time,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TubeMasterGrayDark,
                                        maxLines = 1
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(comment.tagColor).copy(alpha = 0.15f),
                                    border = BorderStroke(0.5.dp, Color(comment.tagColor))
                                ) {
                                    Text(
                                        text = comment.tag,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                        color = Color(comment.tagColor),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }

                            // Text
                            Text(
                                text = comment.text,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 17.sp),
                                color = TubeMasterWhite
                            )

                            // Creator Reply Display if present
                            if (comment.creatorReply != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF161616),
                                    border = BorderStroke(0.5.dp, Color(0xFF22C55E).copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = TubeMasterGreen, modifier = Modifier.size(12.dp))
                                            Text(
                                                text = "Sua Resposta (Criador):",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                                color = TubeMasterGreen
                                            )
                                        }
                                        Text(
                                            text = comment.creatorReply,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                            color = TubeMasterWhite
                                        )
                                    }
                                }
                            }

                            // Bottom Actions Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(Icons.Default.ThumbUp, contentDescription = "Curtidas", tint = TubeMasterGray, modifier = Modifier.size(12.dp))
                                        Text(text = "${comment.likesCount}", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                                    }

                                    // Pin button
                                    IconButton(
                                        onClick = {
                                            onTogglePin(comment.id)
                                            Toast.makeText(context, if (comment.isPinned) "Comentário desafixado" else "Comentário fixado no topo!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.PushPin,
                                            contentDescription = "Fixar",
                                            tint = if (comment.isPinned) Color(0xFFEAB308) else TubeMasterGrayDark,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }

                                    // Spam button
                                    IconButton(
                                        onClick = {
                                            onToggleSpam(comment.id)
                                            Toast.makeText(context, if (comment.isSpam) "Comentário restaurado" else "Marcado como Spam", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Report,
                                            contentDescription = "Spam",
                                            tint = if (comment.isSpam) TubeMasterRed else TubeMasterGrayDark,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }

                                    // Delete button
                                    IconButton(
                                        onClick = {
                                            onDeleteComment(comment.id)
                                            Toast.makeText(context, "Comentário excluído", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = TubeMasterGrayDark, modifier = Modifier.size(14.dp))
                                    }
                                }

                                // Interactive "Responder com IA" button
                                Button(
                                    onClick = {
                                        activeReplyComment = comment
                                        isLoadingReplies = true
                                        customReplyText = ""
                                        onGenerateAiReplies(comment.author, comment.text) { replies ->
                                            aiReplyOptions = replies
                                            if (replies.isNotEmpty()) {
                                                customReplyText = replies.first()
                                            }
                                            isLoadingReplies = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TubeMasterRed,
                                        contentColor = TubeMasterWhite
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(28.dp).testTag("reply_ai_btn_${comment.id}")
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(11.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (comment.creatorReply != null) "Editar Resposta" else "Responder com IA",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Responder com IA
    if (activeReplyComment != null) {
        val comment = activeReplyComment!!
        Dialog(onDismissRequest = { activeReplyComment = null }) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = TubeMasterCard,
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = TubeMasterRed.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, TubeMasterRed)
                            ) {
                                Icon(Icons.Default.Reply, contentDescription = null, tint = TubeMasterRed, modifier = Modifier.padding(6.dp).size(16.dp))
                            }
                            Text(
                                text = "Responder a ${comment.author}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TubeMasterWhite
                            )
                        }

                        IconButton(onClick = { activeReplyComment = null }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TubeMasterGray)
                        }
                    }

                    // Original Comment snippet
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF141414),
                        border = BorderStroke(0.5.dp, TubeMasterBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"${comment.text}\"",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TubeMasterGray,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Text(
                        text = "Sugestões de Respostas na Voz do Criador:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TubeMasterWhite
                    )

                    if (isLoadingReplies) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = TubeMasterRed, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gerando respostas personalizadas...", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                        }
                    } else {
                        aiReplyOptions.forEachIndexed { idx, reply ->
                            val label = when (idx) {
                                0 -> "Opção 1 (Grato & Entusiasta)"
                                1 -> "Opção 2 (Educacional & Direto)"
                                else -> "Opção 3 (Curto & Descontraído)"
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (customReplyText == reply) Color(0xFF1C281F) else Color(0xFF121212),
                                border = BorderStroke(1.dp, if (customReplyText == reply) TubeMasterGreen else TubeMasterBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { customReplyText = reply }
                            ) {
                                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp, fontWeight = FontWeight.Bold), color = TubeMasterGreen)
                                    Text(text = reply, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = TubeMasterWhite)
                                }
                            }
                        }
                    }

                    Text("Editar Resposta Antes de Enviar:", style = MaterialTheme.typography.labelSmall, color = TubeMasterGray)
                    OutlinedTextField(
                        value = customReplyText,
                        onValueChange = { customReplyText = it },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TubeMasterRed,
                            unfocusedBorderColor = TubeMasterBorder,
                            focusedTextColor = TubeMasterWhite,
                            unfocusedTextColor = TubeMasterWhite
                        )
                    )

                    Button(
                        onClick = {
                            if (customReplyText.isNotBlank()) {
                                onReplyToComment(comment.id, customReplyText)
                                Toast.makeText(context, "Resposta enviada e salva com sucesso!", Toast.LENGTH_SHORT).show()
                                activeReplyComment = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TubeMasterRed,
                            contentColor = TubeMasterWhite
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp).testTag("confirm_reply_btn")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Enviar Resposta", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }

    // Dialog: Adicionar Novo Comentário de Teste
    if (showAddCommentDialog) {
        Dialog(onDismissRequest = { showAddCommentDialog = false }) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = TubeMasterCard,
                border = BorderStroke(1.dp, TubeMasterBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Simular Comentário de Espectador",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TubeMasterWhite
                    )

                    OutlinedTextField(
                        value = newCommentAuthor,
                        onValueChange = { newCommentAuthor = it },
                        label = { Text("Nome do Espectador") },
                        placeholder = { Text("Ex: Pedro Youtuber") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TubeMasterRed,
                            unfocusedBorderColor = TubeMasterBorder,
                            focusedTextColor = TubeMasterWhite,
                            unfocusedTextColor = TubeMasterWhite
                        )
                    )

                    OutlinedTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        label = { Text("Texto do Comentário") },
                        placeholder = { Text("Ex: Como você faz para achar tags que ranqueiam?") },
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TubeMasterRed,
                            unfocusedBorderColor = TubeMasterBorder,
                            focusedTextColor = TubeMasterWhite,
                            unfocusedTextColor = TubeMasterWhite
                        )
                    )

                    Button(
                        onClick = {
                            if (newCommentAuthor.isNotBlank() && newCommentText.isNotBlank()) {
                                onAddComment(newCommentAuthor, newCommentText, "Pergunta")
                                Toast.makeText(context, "Novo comentário adicionado!", Toast.LENGTH_SHORT).show()
                                showAddCommentDialog = false
                                newCommentAuthor = ""
                                newCommentText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TubeMasterRed,
                            contentColor = TubeMasterWhite
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Adicionar à Lista")
                    }
                }
            }
        }
    }
}
