package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.NavTab

@Composable
fun DashboardScreen(
    viewModel: AprovaViewModel
) {
    val studentName by viewModel.studentName.collectAsState()
    val totalXp by viewModel.totalXp.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val questionsAnswered by viewModel.questionsAnswered.collectAsState()
    val correctAnswers by viewModel.correctAnswers.collectAsState()
    val simuladosCount by viewModel.simuladosCount.collectAsState()

    val revisionTopics = remember { viewModel.repository.getRevisionTopics() }
    val dailyTasks = remember { viewModel.repository.getDailyTasks() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp) // accommodate bottom navigation
    ) {
        // Global Search Bar Trigger
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.navigateTo(AppScreen.Search) }
                    .testTag("dashboard_search_bar"),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar",
                        tint = AprovaBluePrimary
                    )
                    Text(
                        text = "O que queres estudar?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AprovaTextMuted
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AprovaBlueLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Pesquisar",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AprovaBlueDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        // Greeting Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Olá, $studentName! 👋",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = AprovaTextPrimary
                    )
                )
                Text(
                    text = "Vamos estudar hoje?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AprovaTextSecondary
                    )
                )
            }

            // Streak Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AprovaAmberLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, AprovaAmberWarn.copy(alpha = 0.3f)),
                modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.StudyPlan) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "🔥", fontSize = 14.sp)
                    Text(
                        text = "$streakDays dias",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PROGRESSO GERAL Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("progresso_geral_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PROGRESSO GERAL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = AprovaTextMuted
                            )
                        )
                        Text(
                            text = "72% de preparação",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AprovaTextPrimary
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AprovaGreenLight)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Nível 4 • Candidato",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AprovaGreenSuccess,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LinearProgressIndicator(
                    progress = { 0.72f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = AprovaGreenSuccess,
                    trackColor = AprovaGreenLight
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "18 temas dominados",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AprovaTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "$totalXp XP Acumulado",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AprovaBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // "Continua de onde paraste" Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable { viewModel.navigateTo(AppScreen.TopicStudy("mat_eq_2grau")) }
                .testTag("continue_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = AprovaBluePrimary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CONTINUA DE ONDE PARASTE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Matemática",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Equações do 2.º grau",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }

                Button(
                    onClick = { viewModel.navigateTo(AppScreen.TopicStudy("mat_eq_2grau")) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = AprovaBluePrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("continue_button")
                ) {
                    Text(
                        text = "CONTINUAR",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Estatísticas Grid
        SectionHeader(
            title = "ESTATÍSTICAS",
            actionText = "Ver Detalhes",
            onActionClick = { viewModel.navigateTo(AppScreen.ProgressDetail) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Questões",
                value = "$questionsAnswered",
                icon = Icons.Default.Quiz,
                iconColor = AprovaBluePrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Acertos",
                value = "$correctAnswers",
                icon = Icons.Default.CheckCircle,
                iconColor = AprovaGreenSuccess,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Simulados",
                value = "$simuladosCount",
                icon = Icons.Default.Assignment,
                iconColor = Color(0xFF7C3AED),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "XP Total",
                value = "$totalXp",
                icon = Icons.Default.Bolt,
                iconColor = AprovaAmberWarn,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Acesso Rápido
        SectionHeader(
            title = "ACESSO RÁPIDO",
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionTile(
                title = "DISCIPLINAS",
                icon = Icons.Default.MenuBook,
                color = AprovaBluePrimary,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(NavTab.DISCIPLINAS) }
            )
            QuickActionTile(
                title = "QUIZ",
                icon = Icons.Default.Lightbulb,
                color = AprovaGreenSuccess,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(NavTab.QUIZ) }
            )
            QuickActionTile(
                title = "SIMULADOS",
                icon = Icons.Default.Timer,
                color = Color(0xFF7C3AED),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.selectTab(NavTab.SIMULADOS) }
            )
            QuickActionTile(
                title = "PESQUISAR",
                icon = Icons.Default.Search,
                color = AprovaAmberWarn,
                modifier = Modifier.weight(1f),
                onClick = { viewModel.navigateTo(AppScreen.Search) }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Tutor APROVA IA Highlight Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable { viewModel.navigateTo(AppScreen.TutorChat) }
                .testTag("tutor_banner_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(AprovaBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🤖", fontSize = 24.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "TUTOR APROVA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AprovaBluePrimary)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "IA",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                    Text(
                        text = "Tens dúvidas numa matéria? Pede uma explicação simples ou passo a passo.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AprovaTextSecondary,
                            lineHeight = 16.sp
                        )
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = AprovaBluePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // REVISÃO INTELIGENTE
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("smart_revision_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🎯", fontSize = 18.sp)
                        Text(
                            text = "REVISÃO INTELIGENTE",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp,
                                color = AprovaTextPrimary
                            )
                        )
                    }
                    Text(
                        text = "Precisas de rever",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AprovaRedError,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                revisionTopics.take(3).forEach { topic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = topic.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = AprovaTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (topic.progress < 50) AprovaRedLight else AprovaAmberLight
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${topic.progress}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (topic.progress < 50) AprovaRedError else AprovaAmberWarn,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { viewModel.startSmartRevision() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("start_revision_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AprovaGreenSuccess,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "COMEÇAR REVISÃO",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // MEU PLANO DE ESTUDO
        SectionHeader(
            title = "MEU PLANO DE ESTUDO",
            actionText = "Ver Agenda",
            onActionClick = { viewModel.navigateTo(AppScreen.StudyPlan) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HOJE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AprovaBluePrimary
                        )
                    )
                    Text(
                        text = "🔥 $streakDays dias consecutivos",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                dailyTasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (task.isCompleted) AprovaGreenSuccess else AprovaTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (task.isCompleted) AprovaTextMuted else AprovaTextPrimary
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${task.subject} — ${task.durationText}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AprovaTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun QuickActionTile(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(84.dp)
            .clickable { onClick() }
            .testTag("quick_action_${title.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = AprovaTextPrimary
                ),
                maxLines = 1
            )
        }
    }
}
