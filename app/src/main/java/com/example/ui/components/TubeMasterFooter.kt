package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterWhite

data class FooterFeatureItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
)

@Composable
fun TubeMasterFooterBar(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        FooterFeatureItem(Icons.Default.CloudUpload, "Upload Automático", "Publique no melhor horário"),
        FooterFeatureItem(Icons.Default.Search, "SEO com IA", "Títulos, descrições e tags"),
        FooterFeatureItem(Icons.Default.CalendarToday, "Agendamento", "Sua rotina, mais leve"),
        FooterFeatureItem(Icons.Default.Message, "Moderação de Comentários", "Mais engajamento, menos spam"),
        FooterFeatureItem(Icons.AutoMirrored.Filled.TrendingUp, "Análises Avançadas", "Entenda seu público"),
        FooterFeatureItem(Icons.Default.PlaylistPlay, "Playlists Inteligentes", "Organize e amplie seu alcance"),
        FooterFeatureItem(Icons.Default.MailOutline, "Relatórios Semanais", "Tudo por e-mail")
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = TubeMasterBg,
        border = BorderStroke(1.dp, TubeMasterBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,
                        tint = TubeMasterGray,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = feature.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            ),
                            color = TubeMasterWhite
                        )
                        Text(
                            text = feature.subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp
                            ),
                            color = TubeMasterGrayDark
                        )
                    }
                }
            }
        }
    }
}
