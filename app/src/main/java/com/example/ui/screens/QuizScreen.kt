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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: AprovaViewModel
) {
    val subjects = remember { viewModel.repository.getAllSubjects() }
    var selectedSubjectId by remember { mutableStateOf("mat") }
    var selectedDifficulty by remember { mutableStateOf("Médio") }
    var selectedCount by remember { mutableIntStateOf(10) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quiz de Treino",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "Pratica questões com feedback e explicações imediatas",
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
            // Setup Card
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "CONFIGURA O TEU QUIZ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AprovaTextMuted,
                            letterSpacing = 1.sp
                        )
                    )

                    // 1. Disciplina Selector
                    Column {
                        Text(
                            text = "Disciplina",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Chips for subjects
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "mat" to "Matemática",
                                "pt" to "Português",
                                "fis" to "Física",
                                "bio" to "Biologia"
                            ).forEach { (id, name) ->
                                val isSelected = selectedSubjectId == id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedSubjectId = id },
                                    label = { Text(name, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AprovaBluePrimary,
                                        selectedLabelColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }

                    // 2. Dificuldade
                    Column {
                        Text(
                            text = "Dificuldade",
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
                            listOf("Fácil", "Médio", "Difícil").forEach { diff ->
                                val isSelected = selectedDifficulty == diff
                                OutlinedButton(
                                    onClick = { selectedDifficulty = diff },
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
                                        text = diff,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // 3. Número de Perguntas
                    Column {
                        Text(
                            text = "Número de Perguntas",
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
                            listOf(5, 10, 15).forEach { count ->
                                val isSelected = selectedCount == count
                                OutlinedButton(
                                    onClick = { selectedCount = count },
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
                                        text = "$count questões",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botão COMEÇAR QUIZ
                    Button(
                        onClick = {
                            val subjectName = subjects.find { it.id == selectedSubjectId }?.name ?: "Matemática"
                            viewModel.startQuiz(
                                subjectId = selectedSubjectId,
                                count = selectedCount,
                                title = "Quiz de $subjectName"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("comecar_quiz_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AprovaBluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "COMEÇAR QUIZ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }

            // Quick topic quizzes
            Text(
                text = "TREINOS RÁPIDOS EM DESTAQUE",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = AprovaTextMuted,
                    letterSpacing = 1.sp
                )
            )

            QuickQuizCard(
                title = "Equações do 2.º grau e Bhaskara",
                subject = "Matemática",
                questionsCount = 5,
                onClick = {
                    viewModel.startQuiz(
                        subjectId = "mat",
                        topicId = "mat_eq_2grau",
                        count = 5,
                        title = "Quiz: Equações do 2.º grau"
                    )
                }
            )

            QuickQuizCard(
                title = "Orações Subordinadas e Sintaxe",
                subject = "Língua Portuguesa",
                questionsCount = 5,
                onClick = {
                    viewModel.startQuiz(
                        subjectId = "pt",
                        topicId = "pt_oracoes",
                        count = 5,
                        title = "Quiz: Sintaxe e Orações"
                    )
                }
            )

            QuickQuizCard(
                title = "Leis de Newton e Dinâmica",
                subject = "Física",
                questionsCount = 5,
                onClick = {
                    viewModel.startQuiz(
                        subjectId = "fis",
                        count = 5,
                        title = "Quiz: Física e Leis de Newton"
                    )
                }
            )
        }
    }
}

@Composable
fun QuickQuizCard(
    title: String,
    subject: String,
    questionsCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AprovaBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = AprovaBluePrimary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AprovaTextPrimary
                    )
                )
                Text(
                    text = "$subject • $questionsCount perguntas • +50 XP",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AprovaTextSecondary,
                        fontSize = 12.sp
                    )
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AprovaTextMuted
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveQuizScreen(
    viewModel: AprovaViewModel
) {
    val quizState by viewModel.quizState.collectAsState()
    val currentQuestion = quizState.questions.getOrNull(quizState.currentIndex)

    if (currentQuestion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Sem perguntas disponíveis.")
        }
        return
    }

    val total = quizState.questions.size
    val currentNum = quizState.currentIndex + 1
    val progress = currentNum.toFloat() / total.toFloat()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = quizState.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = "Pergunta $currentNum de $total",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AprovaBluePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Sair",
                            tint = AprovaTextSecondary
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AprovaAmberLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+${quizState.xpEarned} XP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
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
            if (quizState.isSubmitted) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.nextQuizQuestion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("proxima_pergunta_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AprovaBluePrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = if (currentNum == total) "VER RESULTADO" else "PRÓXIMA PERGUNTA",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
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
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = AprovaBluePrimary,
                trackColor = AprovaSurfaceVariant
            )

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
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AprovaSurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = currentQuestion.difficulty,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = AprovaTextSecondary,
                                    fontSize = 10.sp
                                )
                            )
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

            // Alternatives (A, B, C, D)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val optionLetters = listOf("A", "B", "C", "D")
                currentQuestion.options.forEachIndexed { index, optionText ->
                    val letter = optionLetters.getOrElse(index) { "$index" }
                    val isSelected = quizState.selectedOptionIndex == index
                    val isCorrect = index == currentQuestion.correctOptionIndex

                    val cardBgColor = when {
                        !quizState.isSubmitted -> {
                            if (isSelected) AprovaBlueLight else MaterialTheme.colorScheme.surface
                        }
                        isCorrect -> AprovaGreenLight
                        isSelected && !isCorrect -> AprovaRedLight
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val borderColor = when {
                        !quizState.isSubmitted -> {
                            if (isSelected) AprovaBluePrimary else Color.Transparent
                        }
                        isCorrect -> AprovaGreenSuccess
                        isSelected && !isCorrect -> AprovaRedError
                        else -> Color.Transparent
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !quizState.isSubmitted) {
                                viewModel.selectQuizOption(index)
                            }
                            .testTag("quiz_option_$index"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBgColor),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Letter Pill
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            quizState.isSubmitted && isCorrect -> AprovaGreenSuccess
                                            quizState.isSubmitted && isSelected && !isCorrect -> AprovaRedError
                                            isSelected -> AprovaBluePrimary
                                            else -> AprovaSurfaceVariant
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = letter,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected || (quizState.isSubmitted && isCorrect)) Color.White else AprovaTextPrimary
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

                            // Status Icon
                            if (quizState.isSubmitted) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correto",
                                        tint = AprovaGreenSuccess
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Incorreto",
                                        tint = AprovaRedError
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Feedback Card (Appears immediately after submitting)
            AnimatedVisibility(visible = quizState.isSubmitted) {
                val isCorrect = quizState.isCorrect == true
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) AprovaGreenLight else AprovaRedLight
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCorrect) AprovaGreenSuccess.copy(alpha = 0.4f) else AprovaRedError.copy(alpha = 0.4f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (isCorrect) AprovaGreenSuccess else AprovaRedError
                            )
                            Text(
                                text = if (isCorrect) "Resposta Correta! (+10 XP)" else "Resposta Incorreta",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) AprovaGreenSuccess else AprovaRedError
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentQuestion.explanation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isCorrect) Color(0xFF065F46) else Color(0xFF991B1B),
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizSummaryScreen(
    viewModel: AprovaViewModel
) {
    val quizState by viewModel.quizState.collectAsState()
    val total = quizState.questions.size
    val correct = quizState.correctAnswersCount
    val percentage = if (total > 0) (correct * 100) / total else 0

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (percentage >= 70) AprovaGreenLight else AprovaAmberLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (percentage >= 70) "🎉" else "💪",
                    fontSize = 36.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "QUIZ CONCLUÍDO!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = AprovaTextPrimary,
                    letterSpacing = 1.sp
                )
            )

            Text(
                text = if (percentage >= 70) "Excelente desempenho! O teu treino está a dar frutos." else "Bom esforço! Continua a praticar para dominar a matéria.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = AprovaTextSecondary,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Score Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$correct / $total",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AprovaBluePrimary
                            )
                        )
                        Text(
                            text = "Acertos",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(AprovaBorderLight)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (percentage >= 70) AprovaGreenSuccess else AprovaAmberWarn
                            )
                        )
                        Text(
                            text = "Aproveitamento",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(AprovaBorderLight)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${quizState.xpEarned}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFB45309)
                            )
                        )
                        Text(
                            text = "XP Ganho",
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaTextSecondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { viewModel.selectTab(com.example.ui.viewmodel.NavTab.QUIZ) },
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
                    text = "NOVO QUIZ",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

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
