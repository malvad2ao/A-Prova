package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
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
import com.example.model.Subject
import com.example.model.Topic
import com.example.ui.components.SubjectIconBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplinasScreen(
    viewModel: AprovaViewModel
) {
    var selectedTargetArea by remember { mutableStateOf("Todas") }
    var selectedUniversity by remember { mutableStateOf("Todas as Universidades") }

    val allSubjects = remember { viewModel.repository.getAllSubjects() }

    val filteredSubjects = remember(selectedTargetArea) {
        if (selectedTargetArea == "Todas") {
            allSubjects
        } else {
            allSubjects.filter { it.targetAreas.contains(selectedTargetArea) || it.targetAreas.contains("Geral") }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Disciplinas",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "Seleciona a matéria para estudar por temas",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Exam & University Filter Bar (Prepared for future expansions)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                // Course / Area selector chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val areas = listOf("Todas", "Engenharia", "Medicina", "Direito", "Economia", "Humanidades")
                    areas.forEach { area ->
                        val isSelected = selectedTargetArea == area
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTargetArea = area },
                            label = { Text(area) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AprovaBluePrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subjects list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Text(
                        text = "${filteredSubjects.size} DISCIPLINAS DISPONÍVEIS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AprovaTextMuted,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(filteredSubjects, key = { it.id }) { subject ->
                    SubjectCardItem(
                        subject = subject,
                        onClick = { viewModel.navigateTo(AppScreen.SubjectDetail(subject.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun SubjectCardItem(
    subject: Subject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("subject_card_${subject.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SubjectIconBadge(
                subjectName = subject.name,
                colorHex = subject.colorHex,
                size = 48
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AprovaTextPrimary
                        )
                    )
                    Text(
                        text = "${subject.progressPercent}%",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (subject.progressPercent >= 75) AprovaGreenSuccess else AprovaBluePrimary
                        )
                    )
                }

                Text(
                    text = subject.category,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AprovaTextSecondary,
                        fontSize = 11.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { subject.progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (subject.progressPercent >= 75) AprovaGreenSuccess else AprovaBluePrimary,
                    trackColor = AprovaSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${subject.completedTopics}/${subject.totalTopics} temas dominados",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AprovaTextMuted,
                        fontSize = 10.sp
                    )
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = AprovaTextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    subjectId: String,
    viewModel: AprovaViewModel
) {
    val subject = remember(subjectId) {
        viewModel.repository.getAllSubjects().find { it.id == subjectId }
            ?: viewModel.repository.getAllSubjects().first()
    }
    val categories = remember(subjectId) {
        viewModel.repository.getCategoriesForSubject(subjectId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = subject.name.uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "Escolhe um tema para começar",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AprovaTextSecondary,
                                fontSize = 12.sp
                            )
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
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.startQuiz(subjectId = subject.id, count = 10, title = "Quiz de ${subject.name}")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "Quiz da Disciplina",
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
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            item {
                // Header Banner for the subject
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(subject.colorHex)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = subject.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = subject.name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = subject.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            categories.forEach { category ->
                item {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = AprovaTextPrimary
                        ),
                        modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                    )
                }

                items(category.topics, key = { it.id }) { topic ->
                    TopicItemCard(
                        topic = topic,
                        onClick = { viewModel.navigateTo(AppScreen.TopicStudy(topic.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun TopicItemCard(
    topic: Topic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("topic_item_${topic.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            topic.isDominado -> AprovaGreenLight
                            topic.needsRevision -> AprovaRedLight
                            else -> AprovaBlueLight
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        topic.isDominado -> Icons.Default.Check
                        topic.needsRevision -> Icons.Default.Warning
                        else -> Icons.Default.MenuBook
                    },
                    contentDescription = null,
                    tint = when {
                        topic.isDominado -> AprovaGreenSuccess
                        topic.needsRevision -> AprovaRedError
                        else -> AprovaBluePrimary
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = AprovaTextPrimary
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = when {
                            topic.isDominado -> "Dominado"
                            topic.needsRevision -> "Revisão recomendada"
                            else -> "Em estudo"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = when {
                                topic.isDominado -> AprovaGreenSuccess
                                topic.needsRevision -> AprovaRedError
                                else -> AprovaTextSecondary
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(text = "•", color = AprovaTextMuted, fontSize = 11.sp)
                    Text(
                        text = "${topic.progress}% concluído",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AprovaTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = AprovaTextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
