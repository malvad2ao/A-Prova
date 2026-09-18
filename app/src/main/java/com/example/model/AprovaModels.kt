package com.example.model

data class Subject(
    val id: String,
    val name: String,
    val category: String,
    val colorHex: Long,
    val progressPercent: Int,
    val totalTopics: Int,
    val completedTopics: Int,
    val description: String,
    val targetAreas: List<String> = listOf("Geral", "Engenharia", "Medicina", "Direito", "Economia")
)

data class TopicCategory(
    val id: String,
    val subjectId: String,
    val name: String,
    val topics: List<Topic>
)

data class Topic(
    val id: String,
    val subjectId: String,
    val categoryId: String,
    val title: String,
    val progress: Int,
    val isDominado: Boolean = false,
    val needsRevision: Boolean = false,
    val studyTimeMinutes: Int = 20
)

data class FormulaItem(
    val title: String,
    val formulaMath: String,
    val discriminantFormula: String = "",
    val notes: List<String> = emptyList()
)

data class ResolvedExample(
    val question: String,
    val step1: String, // Identificar os valores
    val step2: String, // Aplicar a fórmula necessária
    val step3: String, // Realizar os cálculos
    val step4: String  // Apresentar a resposta
)

data class StudyContent(
    val topicId: String,
    val topicTitle: String,
    val subjectName: String,
    val whatIs: String,
    val concept: String,
    val formula: FormulaItem?,
    val resolvedExample: ResolvedExample,
    val summaryPoints: List<String>,
    val simplifiedExplanation: String
)

data class QuizQuestion(
    val id: String,
    val subjectId: String,
    val topicId: String,
    val topicTitle: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val difficulty: String = "Médio"
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val progressText: String,
    val xpReward: Int
)

data class StudyTask(
    val id: String,
    val title: String,
    val subject: String,
    val durationText: String,
    val isCompleted: Boolean,
    val type: String
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val quickReplies: List<String> = emptyList()
)

data class WrongQuestionItem(
    val question: QuizQuestion,
    val userSelectedIndex: Int?
)

data class SimuladoResult(
    val examTitle: String,
    val scorePercentage: Int,
    val scoreOutOf20: Float,
    val correctCount: Int,
    val wrongCount: Int,
    val unansweredCount: Int,
    val timeSpentFormatted: String,
    val xpEarned: Int,
    val wrongQuestions: List<WrongQuestionItem>
)
