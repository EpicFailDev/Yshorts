package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.remote.FirebaseSyncManager
import com.example.data.remote.GeminiService
import com.example.data.repository.ScriptRepository
import com.example.data.repository.TubeMasterRepository
import com.example.domain.model.AdvancedSearchFilter
import com.example.domain.model.DayViewStat
import com.example.domain.model.NavSection
import com.example.domain.model.ShortsScript
import com.example.domain.model.SparklineData
import com.example.domain.model.ThemeMode
import com.example.domain.model.TubeMasterVideo
import com.example.domain.model.VideoRetentionAnalysis
import com.example.domain.model.ViewDisplayMode
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val firebaseSync = FirebaseSyncManager(application)
    val geminiService = GeminiService()
    val repository = ScriptRepository(database.scriptDao(), firebaseSync)

    // TubeMaster AI Repository & State
    val tubeMasterRepo = TubeMasterRepository()

    private val _viewDisplayMode = MutableStateFlow(ViewDisplayMode.FOCUSED_INTERACTIVE)
    val viewDisplayMode: StateFlow<ViewDisplayMode> = _viewDisplayMode.asStateFlow()

    private val _currentNavSection = MutableStateFlow(NavSection.DASHBOARD)
    val currentNavSection: StateFlow<NavSection> = _currentNavSection.asStateFlow()

    private val _isAiOptimizeModalOpen = MutableStateFlow(false)
    val isAiOptimizeModalOpen: StateFlow<Boolean> = _isAiOptimizeModalOpen.asStateFlow()

    private val _isScheduleModalOpen = MutableStateFlow(false)
    val isScheduleModalOpen: StateFlow<Boolean> = _isScheduleModalOpen.asStateFlow()

    val tubeMasterMetrics: StateFlow<List<SparklineData>> = tubeMasterRepo.sparklineMetrics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tubeMasterSevenDays: StateFlow<List<DayViewStat>> = tubeMasterRepo.sevenDaysStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tubeMasterVideos: StateFlow<List<TubeMasterVideo>> = tubeMasterRepo.uploadQueue
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tubeMasterRetention: StateFlow<VideoRetentionAnalysis> = tubeMasterRepo.retentionAnalysis
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            VideoRetentionAnalysis(
                videoTitle = "Como criar thumbnails que realmente funcionam",
                duration = "12:34",
                uploadDate = "12/06/2025",
                retentionRate = "62,8%",
                retentionChange = "↑ 14,2%",
                avgWatchDuration = "7:48",
                avgWatchChange = "↑ 22,5%",
                curvePoints = emptyList(),
                dropMoments = emptyList()
            )
        )

    fun setViewDisplayMode(mode: ViewDisplayMode) {
        _viewDisplayMode.value = mode
    }

    fun setNavSection(section: NavSection) {
        _currentNavSection.value = section
        _viewDisplayMode.value = ViewDisplayMode.FOCUSED_INTERACTIVE
    }

    fun openAiOptimizeModal() {
        _isAiOptimizeModalOpen.value = true
    }

    fun closeAiOptimizeModal() {
        _isAiOptimizeModalOpen.value = false
    }

    fun openScheduleModal() {
        _isScheduleModalOpen.value = true
    }

    fun closeScheduleModal() {
        _isScheduleModalOpen.value = false
    }

    fun scheduleVideo(title: String, duration: String, scheduledTime: String) {
        tubeMasterRepo.scheduleVideoWithAi(title, duration, scheduledTime)
    }

    private val _themeMode = MutableStateFlow(ThemeMode.AUTO)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _currentTab = MutableStateFlow(0) // 0: Roteiros, 1: IA Studio, 2: Configurações
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _advancedFilter = MutableStateFlow(AdvancedSearchFilter())
    val advancedFilter: StateFlow<AdvancedSearchFilter> = _advancedFilter.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todas")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isEditorOpen = MutableStateFlow(false)
    val isEditorOpen: StateFlow<Boolean> = _isEditorOpen.asStateFlow()

    private val _editingScript = MutableStateFlow<ShortsScript?>(null)
    val editingScript: StateFlow<ShortsScript?> = _editingScript.asStateFlow()

    val currentUser: StateFlow<FirebaseUser?> = firebaseSync.authState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), firebaseSync.currentUser)

    @OptIn(ExperimentalCoroutinesApi::class)
    val scripts: StateFlow<List<ShortsScript>> = _advancedFilter
        .flatMapLatest { filter ->
            repository.searchScriptsAdvanced(filter)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableTags: StateFlow<List<String>> = repository.getAllTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = repository.getAllCategories()
        .combine(MutableStateFlow(listOf("Produtividade", "Tecnologia", "Finanças", "Curiosidades", "Dicas Rápidas"))) { dbCats, defaultCats ->
            (dbCats + defaultCats).distinct().filter { it.isNotBlank() }.sorted()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("Produtividade", "Tecnologia", "Finanças", "Curiosidades"))

    init {
        viewModelScope.launch {
            // Seed initial viral shorts templates if empty
            val initialList = repository.searchScripts("", "Todos")
            launch {
                initialList.collect { list ->
                    if (list.isEmpty()) {
                        repository.seedSampleScriptsIfEmpty()
                    }
                }
            }
        }
    }

    fun setTab(tab: Int) {
        _currentTab.value = tab
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        _advancedFilter.value = _advancedFilter.value.copy(category = category)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _advancedFilter.value = _advancedFilter.value.copy(query = query)
    }

    fun setAdvancedFilter(filter: AdvancedSearchFilter) {
        _advancedFilter.value = filter
        _searchQuery.value = filter.query
        _selectedCategory.value = filter.category
    }

    fun resetAdvancedFilter() {
        _advancedFilter.value = AdvancedSearchFilter()
        _searchQuery.value = ""
        _selectedCategory.value = "Todas"
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun openNewScriptEditor() {
        _editingScript.value = null
        _isEditorOpen.value = true
    }

    fun openEditScriptEditor(script: ShortsScript) {
        _editingScript.value = script
        _isEditorOpen.value = true
    }

    fun closeEditor() {
        _isEditorOpen.value = false
        _editingScript.value = null
    }

    fun saveScript(script: ShortsScript) {
        viewModelScope.launch {
            if (script.id == 0L) {
                repository.insertScript(script)
            } else {
                repository.updateScript(script)
            }
            closeEditor()
        }
    }

    fun deleteScript(script: ShortsScript) {
        viewModelScope.launch {
            repository.deleteScript(script)
        }
    }

    fun toggleFavorite(script: ShortsScript) {
        viewModelScope.launch {
            repository.updateScript(script.copy(isFavorite = !script.isFavorite))
        }
    }

    suspend fun syncCloud(): Result<Int> {
        return repository.syncWithCloud()
    }
}
