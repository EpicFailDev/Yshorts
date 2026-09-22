package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.ThemeMode
import com.example.ui.MainViewModel
import com.example.ui.screens.TubeMasterMainScreen
import com.example.ui.theme.ShortsScriptTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val displayMode by viewModel.viewDisplayMode.collectAsStateWithLifecycle()
            val currentSection by viewModel.currentNavSection.collectAsStateWithLifecycle()
            val metrics by viewModel.tubeMasterMetrics.collectAsStateWithLifecycle()
            val sevenDays by viewModel.tubeMasterSevenDays.collectAsStateWithLifecycle()
            val videos by viewModel.tubeMasterVideos.collectAsStateWithLifecycle()
            val retention by viewModel.tubeMasterRetention.collectAsStateWithLifecycle()
            val isAiOptimizeOpen by viewModel.isAiOptimizeModalOpen.collectAsStateWithLifecycle()
            val isScheduleOpen by viewModel.isScheduleModalOpen.collectAsStateWithLifecycle()

            // TubeMaster AI is strictly high-tech dark mode as required
            ShortsScriptTheme(themeMode = ThemeMode.DARK) {
                TubeMasterMainScreen(
                    displayMode = displayMode,
                    currentSection = currentSection,
                    metrics = metrics,
                    sevenDaysStats = sevenDays,
                    videos = videos,
                    retentionAnalysis = retention,
                    isAiOptimizeOpen = isAiOptimizeOpen,
                    isScheduleOpen = isScheduleOpen,
                    onToggleDisplayMode = { viewModel.setViewDisplayMode(it) },
                    onSelectSection = { viewModel.setNavSection(it) },
                    onOpenAiOptimizer = { viewModel.openAiOptimizeModal() },
                    onCloseAiOptimizer = { viewModel.closeAiOptimizeModal() },
                    onOpenSchedule = { viewModel.openScheduleModal() },
                    onCloseSchedule = { viewModel.closeScheduleModal() },
                    onScheduleVideo = { title, duration, time ->
                        viewModel.scheduleVideo(title, duration, time)
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}


