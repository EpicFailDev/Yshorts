package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.NavSection
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

data class NavItemSpec(
    val section: NavSection,
    val icon: ImageVector
)

@Composable
fun TubeMasterSidebar(
    currentSection: NavSection,
    onSelectSection: (NavSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItemSpec(NavSection.DASHBOARD, Icons.Default.Dashboard),
        NavItemSpec(NavSection.UPLOADS, Icons.Default.CloudUpload),
        NavItemSpec(NavSection.SEO, Icons.Default.Search),
        NavItemSpec(NavSection.COMENTARIOS, Icons.Default.ChatBubbleOutline),
        NavItemSpec(NavSection.ANALISES, Icons.AutoMirrored.Filled.TrendingUp),
        NavItemSpec(NavSection.CONFIGURACOES, Icons.Default.Settings)
    )

    Surface(
        modifier = modifier
            .width(170.dp)
            .fillMaxHeight(),
        color = TubeMasterCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, TubeMasterBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items.forEach { item ->
                val isSelected = currentSection == item.section
                val bg = if (isSelected) TubeMasterRed.copy(alpha = 0.15f) else Color.Transparent
                val border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, TubeMasterRed) else null
                val contentColor = if (isSelected) TubeMasterRed else TubeMasterGray

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = bg,
                    border = border,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSelectSection(item.section) }
                        .testTag("nav_item_${item.section.name}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.section.title,
                            tint = contentColor,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = item.section.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp
                            ),
                            color = if (isSelected) TubeMasterWhite else TubeMasterGray
                        )
                    }
                }
            }
        }
    }
}
