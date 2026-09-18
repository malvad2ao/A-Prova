package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StudyTask
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyPlanScreen(
    viewModel: AprovaViewModel
) {
    val streakDays by viewModel.streakDays.collectAsState()
    var selectedDayTab by remember { mutableStateOf("HOJE") }
    var dailyTasks by remember { mutableStateOf(viewModel.repository.getDailyTasks()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Meu Plano de Estudo",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "Rotina diária para o teu exame de acesso",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = AprovaBluePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 60.dp)
        ) {
            // Streak Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AprovaAmberLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AprovaAmberWarn.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "🔥", fontSize = 32.sp)
                        Column {
                            Text(
                                text = "$streakDays DIAS SEGUIDOS DE ESTUDO",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB45309)
                                )
                            )
                            Text(
                                text = "A consistência diária é o maior fator de sucesso nos exames de acesso à universidade.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            }

            // Tabs for HOJE, AMANHÃ, ESTA SEMANA
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("HOJE", "AMANHÃ", "ESTA SEMANA").forEach { tab ->
                        val isSelected = selectedDayTab == tab
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedDayTab = tab },
                            label = { Text(tab, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AprovaBluePrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "METAS PROGRAMADAS PARA $selectedDayTab",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = AprovaTextMuted,
                        letterSpacing = 1.sp
                    )
                )
            }

            items(dailyTasks, key = { it.id }) { task ->
                StudyTaskCard(
                    task = task,
                    onToggleComplete = {
                        dailyTasks = dailyTasks.map {
                            if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    },
                    onStart = {
                        when {
                            task.title.contains("Quiz") -> viewModel.startQuiz(count = 10, title = "Quiz Diário")
                            task.title.contains("Simulado") -> viewModel.startSimulado(count = 10, durationMinutes = 20)
                            task.subject == "Matemática" -> viewModel.navigateTo(AppScreen.TopicStudy("mat_eq_2grau"))
                            else -> viewModel.selectTab(com.example.ui.viewmodel.NavTab.DISCIPLINAS)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StudyTaskCard(
    task: StudyTask,
    onToggleComplete: () -> Unit,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleComplete() }
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) AprovaSurfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconButton(
                onClick = onToggleComplete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (task.isCompleted) AprovaGreenSuccess else AprovaTextMuted
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) AprovaTextMuted else AprovaTextPrimary
                    )
                )
                Text(
                    text = "${task.subject} • ${task.durationText}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AprovaTextSecondary,
                        fontSize = 11.sp
                    )
                )
            }

            if (!task.isCompleted) {
                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AprovaBluePrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Iniciar",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
