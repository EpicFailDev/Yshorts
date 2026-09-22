package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewSidebar
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ViewDisplayMode
import com.example.ui.theme.TubeMasterBg
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun TubeMasterHeroBanner(
    currentMode: ViewDisplayMode,
    onToggleMode: (ViewDisplayMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = TubeMasterBg,
        border = BorderStroke(1.dp, color = TubeMasterBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Logo & Branding on Left, Connected Pill & Profile on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Slogan
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TubeMasterRed,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "TubeMaster Logo",
                                tint = TubeMasterWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TubeMaster ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    letterSpacing = (-0.3).sp
                                ),
                                color = TubeMasterWhite
                            )
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    letterSpacing = (-0.3).sp
                                ),
                                color = TubeMasterRed
                            )
                        }
                        Text(
                            text = "Automatize  •  Otimize  •  Cresça",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = TubeMasterGrayDark
                        )
                    }
                }

                // YouTube Connected Pill & Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = TubeMasterCard,
                        border = BorderStroke(0.8.dp, TubeMasterBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(TubeMasterGreen)
                            )
                            Text(
                                text = "YouTube",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TubeMasterWhite
                            )
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = null,
                                tint = TubeMasterRed,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF831843))
                            .border(0.8.dp, TubeMasterBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = TubeMasterWhite
                        )
                    }
                }
            }

            // Row 2: View Mode Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF141414), RoundedCornerShape(8.dp))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Interactive Mode Button
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (currentMode == ViewDisplayMode.FOCUSED_INTERACTIVE) TubeMasterCard else Color.Transparent,
                    border = if (currentMode == ViewDisplayMode.FOCUSED_INTERACTIVE) BorderStroke(0.8.dp, TubeMasterBorder) else null,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToggleMode(ViewDisplayMode.FOCUSED_INTERACTIVE) }
                        .testTag("toggle_interactive_mode_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ViewSidebar,
                            contentDescription = null,
                            tint = if (currentMode == ViewDisplayMode.FOCUSED_INTERACTIVE) TubeMasterRed else TubeMasterGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Navegação Interativa",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (currentMode == ViewDisplayMode.FOCUSED_INTERACTIVE) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            ),
                            color = if (currentMode == ViewDisplayMode.FOCUSED_INTERACTIVE) TubeMasterWhite else TubeMasterGray,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }

                // Mockup 3 Telas Button
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (currentMode == ViewDisplayMode.MOCKUP_PRESENTATION) TubeMasterRed.copy(alpha = 0.2f) else Color.Transparent,
                    border = if (currentMode == ViewDisplayMode.MOCKUP_PRESENTATION) BorderStroke(0.8.dp, TubeMasterRed) else null,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToggleMode(ViewDisplayMode.MOCKUP_PRESENTATION) }
                        .testTag("toggle_mockup_mode_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = null,
                            tint = if (currentMode == ViewDisplayMode.MOCKUP_PRESENTATION) TubeMasterRed else TubeMasterGray,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Mockup 3 Telas",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (currentMode == ViewDisplayMode.MOCKUP_PRESENTATION) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            ),
                            color = if (currentMode == ViewDisplayMode.MOCKUP_PRESENTATION) TubeMasterWhite else TubeMasterGray,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}
