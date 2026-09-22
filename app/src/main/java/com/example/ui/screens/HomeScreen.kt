package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.AdvancedSearchFilter
import com.example.domain.model.DateRangeFilter
import com.example.domain.model.ShortsScript
import com.example.domain.model.SortOrder
import com.example.ui.components.AdvancedSearchSheet
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.ScriptCard
import com.example.util.ExportHelper

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    scripts: List<ShortsScript>,
    categories: List<String>,
    availableTags: List<String>,
    advancedFilter: AdvancedSearchFilter,
    onApplyAdvancedFilter: (AdvancedSearchFilter) -> Unit,
    onResetAdvancedFilter: () -> Unit,
    onNewScript: () -> Unit,
    onEditScript: (ShortsScript) -> Unit,
    onDeleteScript: (ShortsScript) -> Unit,
    onToggleFavorite: (ShortsScript) -> Unit,
    isCloudSynced: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSearchSheetOpen by remember { mutableStateOf(false) }
    var isSortMenuOpen by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar: App Name, Status Pill & Quick Action Icons
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Roteiros Shorts",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (advancedFilter.isActive) "${scripts.size} resultado(s) filtrados" else "${scripts.size} roteiro(s) organizados",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (advancedFilter.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Offline/Online Status Pill
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isCloudSynced) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCloudSynced) Icons.Default.CheckCircle else Icons.Default.CloudOff,
                                        contentDescription = null,
                                        tint = if (isCloudSynced) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = if (isCloudSynced) "Nuvem" else "Offline / Local",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Quick Sort Menu Button
                            Box {
                                IconButton(
                                    onClick = { isSortMenuOpen = true },
                                    modifier = Modifier.testTag("sort_menu_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sort,
                                        contentDescription = "Ordenar resultados",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                DropdownMenu(
                                    expanded = isSortMenuOpen,
                                    onDismissRequest = { isSortMenuOpen = false }
                                ) {
                                    SortOrder.entries.forEach { order ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = order.label,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (advancedFilter.sortBy == order) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = if (advancedFilter.sortBy == order) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                            },
                                            onClick = {
                                                onApplyAdvancedFilter(advancedFilter.copy(sortBy = order))
                                                isSortMenuOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Main Search Input Bar with Filter Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = advancedFilter.query,
                            onValueChange = { newQuery ->
                                onApplyAdvancedFilter(advancedFilter.copy(query = newQuery))
                            },
                            placeholder = { Text("Buscar em roteiros, ganchos, tags...") },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                if (advancedFilter.query.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            onApplyAdvancedFilter(advancedFilter.copy(query = ""))
                                        }
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Limpar texto")
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("search_input_field"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        // Advanced Filter Button with badge if active
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                1.dp,
                                if (advancedFilter.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            color = if (advancedFilter.isActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                        ) {
                            IconButton(
                                onClick = { isSearchSheetOpen = true },
                                modifier = Modifier.testTag("open_advanced_search_btn")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (advancedFilter.isActive) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = "Busca Avançada",
                                        tint = if (advancedFilter.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Horizontal Category Quick Filter
                    CategoryFilterRow(
                        categories = categories,
                        selectedCategory = if (advancedFilter.category.isBlank()) "Todas" else advancedFilter.category,
                        onSelectCategory = { newCategory ->
                            onApplyAdvancedFilter(advancedFilter.copy(category = newCategory))
                        }
                    )

                    // Active Filters Bar (Chips showing active filters & Reset button)
                    if (advancedFilter.isActive) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Filtros:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (advancedFilter.query.isNotBlank()) {
                                    InputChip(
                                        selected = true,
                                        onClick = { onApplyAdvancedFilter(advancedFilter.copy(query = "")) },
                                        label = { Text("\"${advancedFilter.query}\"", style = MaterialTheme.typography.labelSmall) },
                                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp)) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = InputChipDefaults.inputChipColors(selectedContainerColor = MaterialTheme.colorScheme.surface)
                                    )
                                }

                                if (advancedFilter.category != "Todas" && advancedFilter.category != "Todos" && advancedFilter.category.isNotBlank()) {
                                    InputChip(
                                        selected = true,
                                        onClick = { onApplyAdvancedFilter(advancedFilter.copy(category = "Todas")) },
                                        label = { Text(advancedFilter.category, style = MaterialTheme.typography.labelSmall) },
                                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp)) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = InputChipDefaults.inputChipColors(selectedContainerColor = MaterialTheme.colorScheme.surface)
                                    )
                                }

                                if (advancedFilter.tag.isNotBlank()) {
                                    InputChip(
                                        selected = true,
                                        onClick = { onApplyAdvancedFilter(advancedFilter.copy(tag = "")) },
                                        label = { Text("#${advancedFilter.tag}", style = MaterialTheme.typography.labelSmall) },
                                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp)) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = InputChipDefaults.inputChipColors(selectedContainerColor = MaterialTheme.colorScheme.surface)
                                    )
                                }

                                if (advancedFilter.dateRange != DateRangeFilter.ALL) {
                                    InputChip(
                                        selected = true,
                                        onClick = { onApplyAdvancedFilter(advancedFilter.copy(dateRange = DateRangeFilter.ALL)) },
                                        label = { Text(advancedFilter.dateRange.label, style = MaterialTheme.typography.labelSmall) },
                                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp)) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = InputChipDefaults.inputChipColors(selectedContainerColor = MaterialTheme.colorScheme.surface)
                                    )
                                }

                                if (advancedFilter.sortBy != SortOrder.CREATED_DESC) {
                                    InputChip(
                                        selected = true,
                                        onClick = { onApplyAdvancedFilter(advancedFilter.copy(sortBy = SortOrder.CREATED_DESC)) },
                                        label = { Text(advancedFilter.sortBy.label, style = MaterialTheme.typography.labelSmall) },
                                        trailingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(12.dp)) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = InputChipDefaults.inputChipColors(selectedContainerColor = MaterialTheme.colorScheme.surface)
                                    )
                                }

                                TextButton(
                                    onClick = onResetAdvancedFilter,
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text("Limpar tudo", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            // Script List
            if (scripts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (advancedFilter.isActive) Icons.Default.FilterList else Icons.Default.Description,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Text(
                            text = if (advancedFilter.isActive) "Nenhum roteiro corresponde aos filtros" else "Nenhum roteiro cadastrado",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (advancedFilter.isActive)
                                "Tente ajustar o texto-chave, remover tags ou alterar o período de criação."
                            else
                                "Crie seu primeiro roteiro para Shorts ou use a IA para gerar ideias!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (advancedFilter.isActive) {
                            OutlinedButton(
                                onClick = onResetAdvancedFilter,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Limpar Todos os Filtros")
                            }
                        } else {
                            OutlinedButton(
                                onClick = onNewScript,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Criar Novo Roteiro")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 84.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Results Order Indicator banner
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ordenado por: ${advancedFilter.sortBy.label}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${scripts.size} roteiro(s)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    items(scripts, key = { it.id }) { script ->
                        ScriptCard(
                            script = script,
                            onClick = { onEditScript(script) },
                            onEdit = { onEditScript(script) },
                            onDelete = {
                                onDeleteScript(script)
                                Toast.makeText(context, "Roteiro excluído", Toast.LENGTH_SHORT).show()
                            },
                            onCopy = { ExportHelper.copyToClipboard(context, script) },
                            onExportPdf = { ExportHelper.exportAsPdf(context, script) },
                            onExportTxt = { ExportHelper.exportAsTxt(context, script) },
                            onToggleFavorite = { onToggleFavorite(script) }
                        )
                    }
                }
            }
        }

        // Floating Action Button to add script (No heavy elevation/shadows)
        FloatingActionButton(
            onClick = onNewScript,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(14.dp),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_script_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Roteiro")
                Text("Novo Roteiro", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
        }

        // Modal Bottom Sheet for Advanced Search
        if (isSearchSheetOpen) {
            AdvancedSearchSheet(
                initialFilter = advancedFilter,
                categories = categories,
                availableTags = availableTags,
                onApplyFilter = { newFilter ->
                    onApplyAdvancedFilter(newFilter)
                },
                onDismiss = { isSearchSheetOpen = false },
                sheetState = sheetState
            )
        }
    }
}
