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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SimuladoResult
import com.example.model.WrongQuestionItem
import com.example.ui.components.StatCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimuladoScreen(
    viewModel: AprovaViewModel
) {
    var selectedExamType by remember { mutableStateOf("Exame Geral de Acesso") }
    var selectedQuestionCount by remember { mutableIntStateOf(10) }
    var selectedDurationMinutes by remember { mutableIntStateOf(30) }
    var isRigoroso by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Simulados de Exame",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "Simulação real dos exames de acesso à universidade",
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Highlight info banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4C1D95)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "🎓", fontSize = 22.sp)
                        Text(
                            text = "SIMULAÇÃO OFICIAL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Treina sob pressão real com cronómetro e padrão de faculdade",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No fim recebes a tua nota de 0 a 20, percentagem de acertos e revisão detalhada de erros.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            // Setup Form Card
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
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "CONFIGURAÇÃO DO SIMULADO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AprovaTextMuted,
                            letterSpacing = 1.sp
                        )
                    )

                    // 1. Tipo de Exame
                    Column {
                        Text(
                            text = "Tipo de Exame",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        listOf(
                            "Exame Geral de Acesso",
                            "Exame de Engenharia & Tecnologias",
                            "Exame de Medicina & Saúde",
                            "Exame de Ciências Sociais & Direito"
                        ).forEach { examType ->
                            val isSelected = selectedExamType == examType
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedExamType = examType }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedExamType = examType },
                                    colors = RadioButtonDefaults.colors(selectedColor = AprovaBluePrimary)
                                )
                                Text(
                                    text = examType,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = if (isSelected) AprovaTextPrimary else AprovaTextSecondary,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    // 2. Número de Questões
                    Column {
                        Text(
                            text = "Número de Questões",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf(
                                10 to "10 questões",
                                20 to "20 questões",
                                50 to "50 questões"
                            ).forEach { (count, label) ->
                                val isSelected = selectedQuestionCount == count
                                OutlinedButton(
                                    onClick = {
                                        selectedQuestionCount = count
                                        selectedDurationMinutes = when (count) {
                                            10 -> 20
                                            20 -> 45
                                            else -> 90
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) AprovaBluePrimary else Color.Transparent,
                                        contentColor = if (isSelected) Color.White else AprovaTextPrimary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) AprovaBluePrimary else AprovaBorderLight
                                    )
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // 3. Tempo Limite
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tempo Limite",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AprovaTextPrimary
                                )
                            )
                            Text(
                                text = "Tempo proporcional ao número de perguntas",
                                style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AprovaBlueLight)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "$selectedDurationMinutes min",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AprovaBlueDark
                                )
                            )
                        }
                    }

                    // 4. Modo de Exame Rigoroso
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AprovaSurfaceVariant)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Modo Exame Rigoroso",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AprovaTextPrimary
                                )
                            )
                            Text(
                                text = "Cronómetro estrito sem respostas durante a prova",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AprovaTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Switch(
                            checked = isRigoroso,
                            onCheckedChange = { isRigoroso = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AprovaBluePrimary)
                        )
                    }

                    // Iniciar Simulado Button
                    Button(
                        onClick = {
                            viewModel.startSimulado(
                                examTitle = selectedExamType,
                                count = selectedQuestionCount,
                                durationMinutes = selectedDurationMinutes,
                                isExamMode = isRigoroso
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("iniciar_simulado_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AprovaBluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "INICIAR SIMULADO",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSimuladoScreen(
    viewModel: AprovaViewModel
) {
    val simuladoState by viewModel.simuladoState.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    val currentQuestion = simuladoState.questions.getOrNull(simuladoState.currentIndex)

    if (currentQuestion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhuma pergunta carregada.")
        }
        return
    }

    // Format remaining time
    val minutes = simuladoState.remainingSeconds / 60
    val seconds = simuladoState.remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val isTimeRunningOut = simuladoState.remainingSeconds < 300 // under 5 min

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Entregar Simulado?") },
            text = {
                val answered = simuladoState.userAnswers.size
                val total = simuladoState.questions.size
                Text("Respondeste a $answered de $total questões. Tens a certeza de que queres entregar agora para ver os resultados e a nota?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.submitSimulado()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AprovaGreenSuccess)
                ) {
                    Text("Sim, Entregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Continuar a Resolver")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = simuladoState.examTitle,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "Questão ${simuladoState.currentIndex + 1} de ${simuladoState.questions.size}",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
                        )
                    }
                },
                actions = {
                    // Real-time Countdown Timer
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isTimeRunningOut) AprovaRedLight else AprovaBlueLight)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = if (isTimeRunningOut) AprovaRedError else AprovaBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isTimeRunningOut) AprovaRedError else AprovaBlueDark,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.testTag("entregar_simulado_top_button")
                    ) {
                        Text(
                            text = "Entregar",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = AprovaGreenSuccess,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (simuladoState.currentIndex > 0) {
                                viewModel.goToSimuladoQuestion(simuladoState.currentIndex - 1)
                            }
                        },
                        enabled = simuladoState.currentIndex > 0,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Anterior")
                    }

                    OutlinedButton(
                        onClick = { viewModel.toggleSimuladoReviewMark() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (simuladoState.markedForReview.contains(simuladoState.currentIndex)) AprovaAmberWarn else AprovaTextSecondary
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (simuladoState.markedForReview.contains(simuladoState.currentIndex)) AprovaAmberWarn else AprovaBorderLight
                        )
                    ) {
                        Icon(
                            imageVector = if (simuladoState.markedForReview.contains(simuladoState.currentIndex)) Icons.Default.Flag else Icons.Default.OutlinedFlag,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (simuladoState.markedForReview.contains(simuladoState.currentIndex)) "Marcada" else "Rever depois",
                            fontSize = 12.sp
                        )
                    }

                    if (simuladoState.currentIndex + 1 < simuladoState.questions.size) {
                        Button(
                            onClick = { viewModel.goToSimuladoQuestion(simuladoState.currentIndex + 1) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AprovaBluePrimary)
                        ) {
                            Text("Próxima")
                        }
                    } else {
                        Button(
                            onClick = { showConfirmDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AprovaGreenSuccess),
                            modifier = Modifier.testTag("entregar_simulado_button")
                        ) {
                            Text("Entregar")
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra de navegação entre questões (1, 2, 3...)
            Text(
                text = "NAVEGAÇÃO DE QUESTÕES",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AprovaTextMuted,
                    letterSpacing = 1.sp
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                simuladoState.questions.forEachIndexed { index, _ ->
                    val isCurrent = simuladoState.currentIndex == index
                    val isAnswered = simuladoState.userAnswers.containsKey(index)
                    val isMarked = simuladoState.markedForReview.contains(index)

                    val bgColor = when {
                        isCurrent -> AprovaBluePrimary
                        isMarked -> AprovaAmberWarn
                        isAnswered -> AprovaGreenSuccess
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val textColor = when {
                        isCurrent || isMarked || isAnswered -> Color.White
                        else -> AprovaTextPrimary
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(bgColor)
                            .border(
                                1.dp,
                                if (isCurrent) AprovaBlueDark else AprovaBorderLight,
                                CircleShape
                            )
                            .clickable { viewModel.goToSimuladoQuestion(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                    }
                }
            }

            // Question Statement Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                        Text(
                            text = currentQuestion.topicTitle.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaBluePrimary,
                                letterSpacing = 1.sp
                            )
                        )
                        if (simuladoState.markedForReview.contains(simuladoState.currentIndex)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Flag,
                                    contentDescription = null,
                                    tint = AprovaAmberWarn,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Marcada para rever",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = AprovaAmberWarn,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentQuestion.questionText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AprovaTextPrimary,
                            lineHeight = 24.sp
                        )
                    )
                }
            }

            // 4 Alternatives
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val optionLetters = listOf("A", "B", "C", "D")
                val selectedOption = simuladoState.userAnswers[simuladoState.currentIndex]

                currentQuestion.options.forEachIndexed { index, optionText ->
                    val letter = optionLetters.getOrElse(index) { "$index" }
                    val isSelected = selectedOption == index

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectSimuladoAnswer(index) }
                            .testTag("simulado_option_$index"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) AprovaBlueLight else MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) AprovaBluePrimary else Color.Transparent
                        ),
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
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) AprovaBluePrimary else AprovaSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else AprovaTextPrimary
                                    )
                                )
                            }

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = AprovaTextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimuladoResultsScreen(
    viewModel: AprovaViewModel
) {
    val result = viewModel.lastSimuladoResult.collectAsState().value

    if (result == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Sem resultados disponíveis.")
        }
        return
    }

    val isApproved = result.scorePercentage >= 50

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (isApproved) AprovaGreenLight else AprovaAmberLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isApproved) "🎓" else "📚",
                    fontSize = 38.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "RESULTADOS DO SIMULADO",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = AprovaTextPrimary,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = result.examTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AprovaTextSecondary
                    )
                )
            }

            // Nota Principal Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CLASSIFICAÇÃO ESTIMADA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AprovaTextMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = String.format("%.1f / 20", result.scoreOutOf20),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isApproved) AprovaBluePrimary else AprovaAmberWarn
                        )
                    )
                    Text(
                        text = "${result.scorePercentage}% de aproveitamento",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isApproved) AprovaGreenSuccess else AprovaRedError
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isApproved) AprovaGreenLight else AprovaAmberLight)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isApproved) "Apto para Acesso ao Ensino Superior 🎉" else "Recomendado reforçar matérias antes da prova",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isApproved) AprovaGreenSuccess else Color(0xFFB45309)
                            )
                        )
                    }
                }
            }

            // Stat breakdown grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Corretas",
                    value = "${result.correctCount}",
                    icon = Icons.Default.CheckCircle,
                    iconColor = AprovaGreenSuccess,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Erradas",
                    value = "${result.wrongCount}",
                    icon = Icons.Default.Cancel,
                    iconColor = AprovaRedError,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Não respondidas",
                    value = "${result.unansweredCount}",
                    icon = Icons.Default.RemoveCircleOutline,
                    iconColor = AprovaTextMuted,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tempo gasto",
                    value = result.timeSpentFormatted,
                    icon = Icons.Default.Timer,
                    iconColor = AprovaBluePrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // REVER ERROS Button
            if (result.wrongQuestions.isNotEmpty()) {
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.ReviewErrors) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("rever_erros_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AprovaAmberWarn,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "REVER ERROS (${result.wrongQuestions.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Button(
                onClick = { viewModel.selectTab(com.example.ui.viewmodel.NavTab.SIMULADOS) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AprovaBluePrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "NOVO SIMULADO",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            OutlinedButton(
                onClick = { viewModel.selectTab(com.example.ui.viewmodel.NavTab.HOME) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AprovaTextPrimary),
                border = androidx.compose.foundation.BorderStroke(1.dp, AprovaBorderLight)
            ) {
                Text(
                    text = "VOLTAR AO DASHBOARD",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewErrorsScreen(
    viewModel: AprovaViewModel
) {
    val result = viewModel.lastSimuladoResult.collectAsState().value
    val wrongQuestions = result?.wrongQuestions ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Revisão dos Erros",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "${wrongQuestions.size} questões para analisar",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaRedError)
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
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AprovaAmberLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "💡", fontSize = 22.sp)
                        Text(
                            text = "Aprender com os erros é o método mais eficaz para garantir aprovação nos exames de acesso!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF78350F),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            items(wrongQuestions) { item ->
                WrongQuestionCard(item = item, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WrongQuestionCard(
    item: WrongQuestionItem,
    viewModel: AprovaViewModel
) {
    val q = item.question
    val optionLetters = listOf("A", "B", "C", "D")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Topic header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = q.topicTitle.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AprovaBluePrimary,
                        letterSpacing = 1.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AprovaRedLight)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (item.userSelectedIndex == null) "Não respondida" else "Incorreta",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AprovaRedError,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question statement
            Text(
                text = q.questionText,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AprovaTextPrimary,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // User's choice (if any)
            if (item.userSelectedIndex != null) {
                val userLetter = optionLetters.getOrElse(item.userSelectedIndex) { "" }
                val userText = q.options.getOrElse(item.userSelectedIndex) { "" }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(AprovaRedLight)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = AprovaRedError,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Tua resposta: [$userLetter] $userText",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF991B1B),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Correct choice
            val correctLetter = optionLetters.getOrElse(q.correctOptionIndex) { "" }
            val correctText = q.options.getOrElse(q.correctOptionIndex) { "" }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AprovaGreenLight)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = AprovaGreenSuccess,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Resposta correta: [$correctLetter] $correctText",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF065F46),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explanation Card
            Text(
                text = "POR QUE ESTÁ CORRETA?",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = AprovaTextMuted,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = q.explanation,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AprovaTextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Estudar este tema shortcut button
            TextButton(
                onClick = { viewModel.navigateTo(AppScreen.TopicStudy(q.topicId)) },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Estudar teoria deste tema →",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = AprovaBluePrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
