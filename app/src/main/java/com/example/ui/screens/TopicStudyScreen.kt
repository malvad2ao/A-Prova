package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FormulaItem
import com.example.model.ResolvedExample
import com.example.model.StudyContent
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicStudyScreen(
    topicId: String,
    viewModel: AprovaViewModel
) {
    val studyContent = remember(topicId) {
        viewModel.repository.getStudyContentForTopic(topicId)
    }

    var showSimplerExplanation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = studyContent.topicTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary
                            )
                        )
                        Text(
                            text = studyContent.subjectName,
                            style = MaterialTheme.typography.bodySmall.copy(color = AprovaBluePrimary)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("study_back_button")
                    ) {
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
                            viewModel.sendTutorMessage("Podes ajudar-me a estudar o tema ${studyContent.topicTitle}?")
                            viewModel.navigateTo(com.example.ui.viewmodel.AppScreen.TutorChat)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Pedir ajuda ao Tutor",
                            tint = AprovaBluePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            // Persistent bottom action bar
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showSimplerExplanation = !showSimplerExplanation },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AprovaAmberWarn
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AprovaAmberWarn),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("nao_entendi_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (showSimplerExplanation) "VER ORIGINAL" else "NÃO ENTENDI",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.startQuiz(
                                subjectId = null,
                                topicId = studyContent.topicId,
                                count = 5,
                                title = "Questões: ${studyContent.topicTitle}"
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AprovaGreenSuccess,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(50.dp)
                            .testTag("resolver_questoes_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RESOLVER QUESTÕES",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Simplified Explanation Banner (when "NÃO ENTENDI" is triggered)
            AnimatedVisibility(
                visible = showSimplerExplanation,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AprovaAmberLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AprovaAmberWarn.copy(alpha = 0.5f))
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
                            Text(text = "💡", fontSize = 20.sp)
                            Text(
                                text = "EXPLICAÇÃO ALTERNATIVA MAIS SIMPLES",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = studyContent.simplifiedExplanation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF78350F),
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            // 1. O QUE SÃO?
            StudySectionCard(
                title = "O QUE SÃO?",
                icon = Icons.Default.Info,
                accentColor = AprovaBluePrimary
            ) {
                Text(
                    text = studyContent.whatIs,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AprovaTextPrimary,
                        lineHeight = 22.sp
                    )
                )
            }

            // 2. CONCEITO
            StudySectionCard(
                title = "CONCEITO",
                icon = Icons.Default.Lightbulb,
                accentColor = Color(0xFF7C3AED)
            ) {
                Text(
                    text = studyContent.concept,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = AprovaTextPrimary,
                        lineHeight = 22.sp
                    )
                )
            }

            // 3. FÓRMULA (Visual formula card)
            studyContent.formula?.let { formula ->
                FormulaVisualCard(formula = formula)
            }

            // 4. EXEMPLO RESOLVIDO (Passo a passo)
            ResolvedExampleCard(example = studyContent.resolvedExample)

            // 5. RESUMO (Pontos importantes a memorizar)
            SummaryPointsCard(points = studyContent.summaryPoints)

            // Bottom prompt to solve questions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AprovaGreenLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, AprovaGreenSuccess.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TESTA OS TEUS CONHECIMENTOS",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AprovaGreenSuccess,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Responde a questões selecionadas sobre este tema e ganha +10 XP por acerto!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AprovaTextSecondary,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            viewModel.startQuiz(
                                subjectId = null,
                                topicId = studyContent.topicId,
                                count = 5,
                                title = "Questões: ${studyContent.topicTitle}"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AprovaGreenSuccess,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("RESOLVER QUESTÕES AGORA", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StudySectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = AprovaTextPrimary
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun FormulaVisualCard(formula: FormulaItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AprovaBluePrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Functions,
                        contentDescription = null,
                        tint = AprovaBluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "FÓRMULA",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = AprovaTextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main formula highlighted card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AprovaBluePrimary.copy(alpha = 0.07f))
                    .border(1.dp, AprovaBluePrimary.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formula.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AprovaBluePrimary,
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = formula.formulaMath,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = AprovaBlueDark,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    if (formula.discriminantFormula.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = formula.discriminantFormula,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AprovaTextPrimary,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            }

            if (formula.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                formula.notes.forEach { note ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "•", color = AprovaBluePrimary, fontWeight = FontWeight.Bold)
                        Text(
                            text = note,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = AprovaTextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResolvedExampleCard(example: ResolvedExample) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AprovaGreenSuccess.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EditNote,
                        contentDescription = null,
                        tint = AprovaGreenSuccess,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "EXEMPLO RESOLVIDO",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = AprovaTextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Enunciado
            Text(
                text = example.question,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AprovaBlueDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AprovaSurfaceVariant)
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Passo 1
            StepResolutionItem(
                stepNumber = "1",
                stepTitle = "Identificar os valores",
                stepContent = example.step1
            )

            // Passo 2
            StepResolutionItem(
                stepNumber = "2",
                stepTitle = "Aplicar a fórmula necessária",
                stepContent = example.step2
            )

            // Passo 3
            StepResolutionItem(
                stepNumber = "3",
                stepTitle = "Realizar os cálculos",
                stepContent = example.step3
            )

            // Passo 4
            StepResolutionItem(
                stepNumber = "4",
                stepTitle = "Apresentar a resposta",
                stepContent = example.step4,
                isFinal = true
            )
        }
    }
}

@Composable
fun StepResolutionItem(
    stepNumber: String,
    stepTitle: String,
    stepContent: String,
    isFinal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (isFinal) AprovaGreenSuccess else AprovaBluePrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "PASSO $stepNumber: $stepTitle",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isFinal) AprovaGreenSuccess else AprovaTextPrimary
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stepContent,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = AprovaTextSecondary,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@Composable
fun SummaryPointsCard(points: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AprovaAmberWarn.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = AprovaAmberWarn,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "RESUMO",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        color = AprovaTextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            points.forEach { point ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = AprovaGreenSuccess,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = AprovaTextPrimary,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }
    }
}
