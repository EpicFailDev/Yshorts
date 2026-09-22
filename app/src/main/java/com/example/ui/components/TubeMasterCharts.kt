package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DayViewStat
import com.example.domain.model.RetentionPoint
import com.example.domain.model.SparklineData
import com.example.ui.theme.TubeMasterBorder
import com.example.ui.theme.TubeMasterCard
import com.example.ui.theme.TubeMasterGray
import com.example.ui.theme.TubeMasterGrayDark
import com.example.ui.theme.TubeMasterGreen
import com.example.ui.theme.TubeMasterRed
import com.example.ui.theme.TubeMasterWhite

@Composable
fun SparklineMetricCard(
    metric: SparklineData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = TubeMasterCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TubeMasterBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = metric.title,
                style = MaterialTheme.typography.labelMedium,
                color = TubeMasterGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = metric.valueFormatted,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = TubeMasterWhite,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Growth indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = metric.percentChange,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = if (metric.isPositive) TubeMasterGreen else TubeMasterRed,
                        maxLines = 1
                    )
                }

                // Mini Red Sparkline
                Canvas(
                    modifier = Modifier
                        .width(56.dp)
                        .height(24.dp)
                ) {
                    val points = metric.points
                    if (points.size >= 2) {
                        val max = points.maxOrNull() ?: 1f
                        val min = points.minOrNull() ?: 0f
                        val range = if (max == min) 1f else max - min
                        val stepX = size.width / (points.size - 1)

                        val path = Path()
                        points.forEachIndexed { i, p ->
                            val x = i * stepX
                            val y = size.height - ((p - min) / range * size.height)
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }

                        drawPath(
                            path = path,
                            color = TubeMasterRed,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SevenDaysBarChart(
    daysData: List<DayViewStat>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = TubeMasterCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TubeMasterBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Visualizações nos últimos 7 dias",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = TubeMasterWhite
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Y-Axis Labels
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("80K", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("60K", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("40K", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("20K", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("0", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                }

                // Bars with Canvas
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    val maxVal = 100f
                    val barCount = daysData.size
                    val spacing = size.width / barCount
                    val barWidth = spacing * 0.45f

                    // Grid horizontal lines
                    for (i in 0..4) {
                        val y = size.height * (i / 4f)
                        drawLine(
                            color = Color(0xFF222222),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Red Bars
                    daysData.forEachIndexed { index, stat ->
                        val barHeight = (stat.valueK / maxVal) * size.height
                        val left = (index * spacing) + (spacing - barWidth) / 2
                        val top = size.height - barHeight

                        drawRoundRect(
                            color = TubeMasterRed,
                            topLeft = Offset(left, top),
                            size = Size(barWidth, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // X-Axis Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                daysData.forEach {
                    Text(
                        text = it.day,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = TubeMasterGrayDark
                    )
                }
            }
        }
    }
}

@Composable
fun RetentionLineChart(
    curvePoints: List<RetentionPoint>,
    abandonmentCallout: String,
    peakCallout: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = TubeMasterCard,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TubeMasterBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Annotations Row at top
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Drop callout pill
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF2B1214),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, TubeMasterRed)
                ) {
                    Text(
                        text = abandonmentCallout,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                        color = TubeMasterRed,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Peak moment callout pill
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF102A18),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, TubeMasterGreen)
                ) {
                    Text(
                        text = peakCallout,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                        color = TubeMasterGreen,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                // Y-Axis Percentages
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 6.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("100%", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("75%", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("50%", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("25%", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                    Text("0%", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = TubeMasterGrayDark)
                }

                // Chart Canvas
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    val maxVal = 100f
                    val stepX = size.width / (curvePoints.size - 1)

                    // Horizontal guidelines
                    for (i in 0..4) {
                        val y = size.height * (i / 4f)
                        drawLine(
                            color = Color(0xFF222222),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Build path for smooth retention line
                    val path = Path()
                    val fillPath = Path()

                    curvePoints.forEachIndexed { index, pt ->
                        val x = index * stepX
                        val y = size.height - (pt.percentage / maxVal * size.height)

                        if (index == 0) {
                            path.moveTo(x, y)
                            fillPath.moveTo(x, size.height)
                            fillPath.lineTo(x, y)
                        } else {
                            val prevX = (index - 1) * stepX
                            val prevY = size.height - (curvePoints[index - 1].percentage / maxVal * size.height)
                            val midX = (prevX + x) / 2
                            path.cubicTo(midX, prevY, midX, y, x, y)
                            fillPath.cubicTo(midX, prevY, midX, y, x, y)
                        }
                    }

                    fillPath.lineTo(size.width, size.height)
                    fillPath.close()

                    // Draw red gradient under curve
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                TubeMasterRed.copy(alpha = 0.35f),
                                TubeMasterRed.copy(alpha = 0.02f)
                            )
                        )
                    )

                    // Draw main red curve
                    drawPath(
                        path = path,
                        color = TubeMasterRed,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Marker dots for Abandonment (point 3: 4:32) and Peak (point 5: 7:15)
                    if (curvePoints.size > 5) {
                        // Point 3 (Abandonment)
                        val x3 = 3 * stepX
                        val y3 = size.height - (curvePoints[3].percentage / maxVal * size.height)
                        drawCircle(color = TubeMasterRed, radius = 4.dp.toPx(), center = Offset(x3, y3))
                        drawLine(
                            color = TubeMasterRed.copy(alpha = 0.6f),
                            start = Offset(x3, y3),
                            end = Offset(x3, size.height),
                            strokeWidth = 1.dp.toPx()
                        )

                        // Point 5 (Peak)
                        val x5 = 5 * stepX
                        val y5 = size.height - (curvePoints[5].percentage / maxVal * size.height)
                        drawCircle(color = TubeMasterGreen, radius = 4.dp.toPx(), center = Offset(x5, y5))
                        drawLine(
                            color = TubeMasterGreen.copy(alpha = 0.6f),
                            start = Offset(x5, y5),
                            end = Offset(x5, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // X-Axis Timestamps
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                curvePoints.filterIndexed { idx, _ -> idx % 2 == 0 || idx == curvePoints.lastIndex }.forEach {
                    Text(
                        text = it.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = TubeMasterGrayDark
                    )
                }
            }
        }
    }
}
