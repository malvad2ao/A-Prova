package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AprovaViewModel
) {
    val studentName by viewModel.studentName.collectAsState()
    val totalXp by viewModel.totalXp.collectAsState()
    val streakDays by viewModel.streakDays.collectAsState()
    val questionsAnswered by viewModel.questionsAnswered.collectAsState()
    val correctAnswers by viewModel.correctAnswers.collectAsState()
    val simuladosCount by viewModel.simuladosCount.collectAsState()

    val achievements = remember { viewModel.repository.getAchievements() }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var newNameInput by remember { mutableStateOf("") }
    var showAboutDialog by remember { mutableStateOf(false) }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("Alterar Nome") },
            text = {
                Column {
                    Text("Como gostarias que o APROVA te trate?")
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newNameInput,
                        onValueChange = { newNameInput = it },
                        placeholder = { Text("Novo nome") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newNameInput.isNotBlank()) {
                            viewModel.updateStudentName(newNameInput.trim())
                            showEditNameDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AprovaBluePrimary)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("Sobre o APROVA") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("APROVA", fontWeight = FontWeight.Bold, color = AprovaBluePrimary)
                    Text("\"Estuda melhor. Treina. APROVA.\"")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Plataforma desenhada para estudantes que preparam exames de acesso à faculdade e universidade. Focada em síntese didática, resolução passo a passo, treino de questões e simulados de exames reais.")
                    Text("Versão: 1.0.0 (Sem contas, focado na produtividade do aluno).")
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) {
                    Text("Fechar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil do Estudante",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = AprovaTextPrimary
                        )
                    )
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(AprovaBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = studentName.take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = studentName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        IconButton(
                            onClick = {
                                newNameInput = studentName
                                showEditNameDialog = true
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar nome",
                                tint = AprovaBluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Text(
                        text = "Estudante Focado • Nível 4 • $totalXp XP",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AprovaTextSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AprovaGreenLight)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Acesso Rápido Local (Sem Contas)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AprovaGreenSuccess,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Estatísticas Gerais
            SectionHeader(title = "MEU DESEMPENHO")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Questões Feitas",
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

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    title = "Sequência",
                    value = "$streakDays dias",
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = AprovaAmberWarn,
                    modifier = Modifier.weight(1f)
                )
            }

            // Conquistas Desbloqueadas
            SectionHeader(title = "CONQUISTAS DESBLOQUEADAS")

            achievements.forEach { ach ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (ach.isUnlocked) AprovaGreenLight else AprovaSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = ach.iconEmoji, fontSize = 20.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ach.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AprovaTextPrimary
                                )
                            )
                            Text(
                                text = ach.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AprovaTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        if (ach.isUnlocked) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AprovaGreenLight)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Concluída",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AprovaGreenSuccess,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Atalhos e Configurações
            SectionHeader(title = "OPÇÕES")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OptionRowItem(
                        icon = Icons.Default.CalendarMonth,
                        title = "Meu Plano de Estudo",
                        onClick = { viewModel.navigateTo(AppScreen.StudyPlan) }
                    )
                    HorizontalDivider(color = AprovaBorderLight)
                    OptionRowItem(
                        icon = Icons.Default.BarChart,
                        title = "Meu Progresso Detalhado",
                        onClick = { viewModel.navigateTo(AppScreen.ProgressDetail) }
                    )
                    HorizontalDivider(color = AprovaBorderLight)
                    OptionRowItem(
                        icon = Icons.Default.Info,
                        title = "Sobre o APROVA",
                        onClick = { showAboutDialog = true }
                    )
                }
            }
        }
    }
}

@Composable
fun OptionRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AprovaBluePrimary,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = AprovaTextPrimary
            ),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = AprovaTextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
