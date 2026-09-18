package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.*

@Composable
fun MainAppScaffold(
    viewModel: AprovaViewModel
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()

    BackHandler(enabled = currentScreen != AppScreen.MainNav && currentScreen != AppScreen.Onboarding) {
        viewModel.navigateBack()
    }

    Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
        when (screen) {
            is AppScreen.Onboarding -> {
                OnboardingScreen(
                    onEnterClick = { name -> viewModel.submitStudentName(name) }
                )
            }
            is AppScreen.MainNav -> {
                Scaffold(
                    bottomBar = {
                        AprovaBottomNavigationBar(
                            selectedTab = currentTab,
                            onTabSelected = { tab -> viewModel.selectTab(tab) }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        when (currentTab) {
                            NavTab.HOME -> DashboardScreen(viewModel = viewModel)
                            NavTab.DISCIPLINAS -> DisciplinasScreen(viewModel = viewModel)
                            NavTab.QUIZ -> QuizScreen(viewModel = viewModel)
                            NavTab.SIMULADOS -> SimuladoScreen(viewModel = viewModel)
                            NavTab.PERFIL -> ProfileScreen(viewModel = viewModel)
                        }
                    }
                }
            }
            is AppScreen.SubjectDetail -> {
                SubjectDetailScreen(subjectId = screen.subjectId, viewModel = viewModel)
            }
            is AppScreen.TopicStudy -> {
                TopicStudyScreen(topicId = screen.topicId, viewModel = viewModel)
            }
            is AppScreen.QuizActive -> {
                ActiveQuizScreen(viewModel = viewModel)
            }
            is AppScreen.QuizSummary -> {
                QuizSummaryScreen(viewModel = viewModel)
            }
            is AppScreen.SimuladoActive -> {
                ActiveSimuladoScreen(viewModel = viewModel)
            }
            is AppScreen.SimuladoResults -> {
                SimuladoResultsScreen(viewModel = viewModel)
            }
            is AppScreen.ReviewErrors -> {
                ReviewErrorsScreen(viewModel = viewModel)
            }
            is AppScreen.TutorChat -> {
                TutorScreen(viewModel = viewModel)
            }
            is AppScreen.StudyPlan -> {
                StudyPlanScreen(viewModel = viewModel)
            }
            is AppScreen.ProgressDetail -> {
                ProgressScreen(viewModel = viewModel)
            }
            is AppScreen.Search -> {
                SearchScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AprovaBottomNavigationBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            NavTab.HOME to ("Início" to Icons.Default.Home),
            NavTab.DISCIPLINAS to ("Disciplinas" to Icons.Default.MenuBook),
            NavTab.QUIZ to ("Quiz" to Icons.Default.Lightbulb),
            NavTab.SIMULADOS to ("Simulados" to Icons.Default.Timer),
            NavTab.PERFIL to ("Perfil" to Icons.Default.Person)
        )

        items.forEach { (tab, details) ->
            val (label, icon) = details
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AprovaBluePrimary,
                    selectedTextColor = AprovaBluePrimary,
                    indicatorColor = AprovaBlueLight,
                    unselectedIconColor = AprovaTextMuted,
                    unselectedTextColor = AprovaTextMuted
                ),
                modifier = Modifier.testTag("nav_item_${label.lowercase()}")
            )
        }
    }
}
