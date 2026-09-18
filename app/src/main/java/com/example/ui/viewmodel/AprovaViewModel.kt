package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.SearchResultItem
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NavTab {
    HOME,
    DISCIPLINAS,
    QUIZ,
    SIMULADOS,
    PERFIL
}

sealed class AppScreen {
    object Onboarding : AppScreen()
    object MainNav : AppScreen()
    data class SubjectDetail(val subjectId: String) : AppScreen()
    data class TopicStudy(val topicId: String) : AppScreen()
    object QuizActive : AppScreen()
    object QuizSummary : AppScreen()
    object SimuladoActive : AppScreen()
    object SimuladoResults : AppScreen()
    object ReviewErrors : AppScreen()
    object TutorChat : AppScreen()
    object StudyPlan : AppScreen()
    object ProgressDetail : AppScreen()
    object Search : AppScreen()
}

data class ActiveQuizState(
    val title: String = "Quiz de Prática",
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean? = null,
    val correctAnswersCount: Int = 0,
    val xpEarned: Int = 0
)

data class ActiveSimuladoState(
    val examTitle: String = "Simulado Geral de Acesso",
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: Map<Int, Int> = emptyMap(), // question index -> chosen option
    val markedForReview: Set<Int> = emptySet(),
    val remainingSeconds: Int = 5400, // 90 min = 01:30:00
    val totalSeconds: Int = 5400,
    val isExamMode: Boolean = true
)

class AprovaViewModel(application: Application) : AndroidViewModel(application) {

    val repository = AppRepository(application)

    private val _studentName = MutableStateFlow(repository.getStudentName())
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    private val _currentScreen = MutableStateFlow<AppScreen>(
        if (repository.hasStudentName()) AppScreen.MainNav else AppScreen.Onboarding
    )
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<AppScreen>()

    private val _totalXp = MutableStateFlow(repository.getTotalXp())
    val totalXp: StateFlow<Int> = _totalXp.asStateFlow()

    private val _streakDays = MutableStateFlow(repository.getStreakDays())
    val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

    private val _questionsAnswered = MutableStateFlow(repository.getQuestionsAnswered())
    val questionsAnswered: StateFlow<Int> = _questionsAnswered.asStateFlow()

    private val _correctAnswers = MutableStateFlow(repository.getCorrectAnswers())
    val correctAnswers: StateFlow<Int> = _correctAnswers.asStateFlow()

    private val _simuladosCount = MutableStateFlow(repository.getSimuladosCount())
    val simuladosCount: StateFlow<Int> = _simuladosCount.asStateFlow()

    // Active Quiz State
    private val _quizState = MutableStateFlow(ActiveQuizState())
    val quizState: StateFlow<ActiveQuizState> = _quizState.asStateFlow()

    // Active Simulado State
    private val _simuladoState = MutableStateFlow(ActiveSimuladoState())
    val simuladoState: StateFlow<ActiveSimuladoState> = _simuladoState.asStateFlow()

    private val _lastSimuladoResult = MutableStateFlow<SimuladoResult?>(null)
    val lastSimuladoResult: StateFlow<SimuladoResult?> = _lastSimuladoResult.asStateFlow()

    private var timerJob: Job? = null

    // Tutor Chat Messages
    private val _tutorMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "msg_welcome",
                isUser = false,
                message = "Olá! 👋 Sou o teu Tutor APROVA. Estou aqui para te explicar qualquer matéria passo a passo, tirar dúvidas e preparar-te para os teus exames de acesso.\n\nO que gostarias de estudar hoje?",
                quickReplies = listOf(
                    "Explica equações do 2.º grau",
                    "Como funciona a concordância verbal?",
                    "Dá-me um exemplo prático de Física",
                    "Faz uma questão para eu praticar"
                )
            )
        )
    )
    val tutorMessages: StateFlow<List<ChatMessage>> = _tutorMessages.asStateFlow()

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResultItem>>(emptyList())
    val searchResults: StateFlow<List<SearchResultItem>> = _searchResults.asStateFlow()

    fun submitStudentName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isNotEmpty()) {
            repository.saveStudentName(trimmed)
            _studentName.value = trimmed
            _currentScreen.value = AppScreen.MainNav
        }
    }

    fun updateStudentName(newName: String) {
        val trimmed = newName.trim()
        if (trimmed.isNotEmpty()) {
            repository.saveStudentName(trimmed)
            _studentName.value = trimmed
        }
    }

    fun selectTab(tab: NavTab) {
        _currentTab.value = tab
        _currentScreen.value = AppScreen.MainNav
        screenStack.clear()
    }

    fun navigateTo(screen: AppScreen) {
        screenStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            val prev = screenStack.removeAt(screenStack.size - 1)
            _currentScreen.value = prev
            return true
        } else if (_currentScreen.value != AppScreen.MainNav && _currentScreen.value != AppScreen.Onboarding) {
            _currentScreen.value = AppScreen.MainNav
            return true
        }
        return false
    }

    // Quiz functions
    fun startQuiz(
        subjectId: String? = null,
        topicId: String? = null,
        count: Int = 10,
        title: String = "Quiz APROVA"
    ) {
        val questions = repository.getQuestions(subjectId, topicId, count)
        _quizState.value = ActiveQuizState(
            title = title,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isSubmitted = false,
            isCorrect = null,
            correctAnswersCount = 0,
            xpEarned = 0
        )
        navigateTo(AppScreen.QuizActive)
    }

    fun selectQuizOption(index: Int) {
        val current = _quizState.value
        if (current.isSubmitted) return

        val q = current.questions.getOrNull(current.currentIndex) ?: return
        val isCorrect = index == q.correctOptionIndex
        val xpGain = if (isCorrect) 10 else 0

        if (isCorrect) {
            repository.addXp(xpGain)
            _totalXp.value = repository.getTotalXp()
        }
        repository.incrementQuestions(isCorrect)
        _questionsAnswered.value = repository.getQuestionsAnswered()
        _correctAnswers.value = repository.getCorrectAnswers()

        _quizState.value = current.copy(
            selectedOptionIndex = index,
            isSubmitted = true,
            isCorrect = isCorrect,
            correctAnswersCount = current.correctAnswersCount + if (isCorrect) 1 else 0,
            xpEarned = current.xpEarned + xpGain
        )
    }

    fun nextQuizQuestion() {
        val current = _quizState.value
        if (current.currentIndex + 1 < current.questions.size) {
            _quizState.value = current.copy(
                currentIndex = current.currentIndex + 1,
                selectedOptionIndex = null,
                isSubmitted = false,
                isCorrect = null
            )
        } else {
            // Quiz finished
            navigateTo(AppScreen.QuizSummary)
        }
    }

    // Simulado functions
    fun startSimulado(
        examTitle: String = "Simulado Geral de Acesso",
        subjectId: String? = null,
        count: Int = 10,
        durationMinutes: Int = 30,
        isExamMode: Boolean = true
    ) {
        val questions = repository.getQuestions(subjectId, null, count)
        val totalSec = durationMinutes * 60

        _simuladoState.value = ActiveSimuladoState(
            examTitle = examTitle,
            questions = questions,
            currentIndex = 0,
            userAnswers = emptyMap(),
            markedForReview = emptySet(),
            remainingSeconds = totalSec,
            totalSeconds = totalSec,
            isExamMode = isExamMode
        )

        startSimuladoTimer()
        navigateTo(AppScreen.SimuladoActive)
    }

    private fun startSimuladoTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_simuladoState.value.remainingSeconds > 0) {
                delay(1000)
                val newSec = _simuladoState.value.remainingSeconds - 1
                _simuladoState.value = _simuladoState.value.copy(remainingSeconds = newSec)
                if (newSec <= 0) {
                    submitSimulado()
                    break
                }
            }
        }
    }

    fun selectSimuladoAnswer(optionIndex: Int) {
        val current = _simuladoState.value
        val updatedAnswers = current.userAnswers.toMutableMap()
        updatedAnswers[current.currentIndex] = optionIndex
        _simuladoState.value = current.copy(userAnswers = updatedAnswers)
    }

    fun toggleSimuladoReviewMark() {
        val current = _simuladoState.value
        val marks = current.markedForReview.toMutableSet()
        if (marks.contains(current.currentIndex)) {
            marks.remove(current.currentIndex)
        } else {
            marks.add(current.currentIndex)
        }
        _simuladoState.value = current.copy(markedForReview = marks)
    }

    fun goToSimuladoQuestion(index: Int) {
        val current = _simuladoState.value
        if (index in current.questions.indices) {
            _simuladoState.value = current.copy(currentIndex = index)
        }
    }

    fun submitSimulado() {
        timerJob?.cancel()
        val current = _simuladoState.value
        var correct = 0
        var wrong = 0
        var unanswered = 0
        val wrongList = mutableListOf<WrongQuestionItem>()

        current.questions.forEachIndexed { index, q ->
            val chosen = current.userAnswers[index]
            if (chosen == null) {
                unanswered++
                wrongList.add(WrongQuestionItem(q, null))
            } else if (chosen == q.correctOptionIndex) {
                correct++
                repository.incrementQuestions(true)
            } else {
                wrong++
                repository.incrementQuestions(false)
                wrongList.add(WrongQuestionItem(q, chosen))
            }
        }

        val total = current.questions.size
        val scorePct = if (total > 0) (correct * 100) / total else 0
        val scoreOutOf20 = (scorePct * 20f) / 100f
        val timeSpentSec = current.totalSeconds - current.remainingSeconds
        val minutesSpent = timeSpentSec / 60
        val secondsSpent = timeSpentSec % 60
        val timeFormatted = String.format("%02d:%02d", minutesSpent, secondsSpent)
        val xpGain = correct * 15 + 100

        repository.addXp(xpGain)
        repository.incrementSimulados()
        _totalXp.value = repository.getTotalXp()
        _simuladosCount.value = repository.getSimuladosCount()
        _questionsAnswered.value = repository.getQuestionsAnswered()
        _correctAnswers.value = repository.getCorrectAnswers()

        val result = SimuladoResult(
            examTitle = current.examTitle,
            scorePercentage = scorePct,
            scoreOutOf20 = scoreOutOf20,
            correctCount = correct,
            wrongCount = wrong,
            unansweredCount = unanswered,
            timeSpentFormatted = timeFormatted,
            xpEarned = xpGain,
            wrongQuestions = wrongList
        )

        _lastSimuladoResult.value = result
        navigateTo(AppScreen.SimuladoResults)
    }

    // Tutor Chat Functions
    fun sendTutorMessage(text: String) {
        if (text.isBlank()) return

        val userMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            isUser = true,
            message = text
        )

        val currentList = _tutorMessages.value.toMutableList()
        currentList.add(userMsg)
        _tutorMessages.value = currentList

        // Pedagogical response simulation
        viewModelScope.launch {
            delay(800)
            val replyText = generateTutorReply(text)
            val tutorMsg = ChatMessage(
                id = "reply_${System.currentTimeMillis()}",
                isUser = false,
                message = replyText,
                quickReplies = listOf(
                    "Explica de forma mais simples",
                    "Dá-me um exemplo",
                    "Faz uma questão para eu praticar",
                    "Explica novamente"
                )
            )
            val updated = _tutorMessages.value.toMutableList()
            updated.add(tutorMsg)
            _tutorMessages.value = updated
        }
    }

    private fun generateTutorReply(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("equaç") || q.contains("bhaskara") || q.contains("2º") || q.contains("2.º") -> {
                "Excelente pergunta sobre Equações do 2.º grau! 📘\n\n" +
                        "1. Forma canónica: ax² + bx + c = 0 (com a ≠ 0).\n" +
                        "2. Calcula o discriminante: Δ = b² - 4ac.\n" +
                        "3. Aplica a fórmula resolvente: x = (-b ± √Δ) / (2a).\n\n" +
                        "💡 Dica de ouro: Se o 'b' for negativo, cuidado com o sinal: -b torna-se positivo! Queres experimentar resolver um exercício prático comigo?"
            }
            q.contains("concordância") || q.contains("português") || q.contains("haver") -> {
                "Muito bem! Um dos erros mais cobrados nos exames de Português é o verbo HAVER no sentido de existir.\n\n" +
                        "⚠️ Regra fundamental: O verbo HAVER é impessoal e NUNCA vai para o plural!\n" +
                        "✅ Correto: 'Houve muitos alunos aprovados.'\n" +
                        "❌ Errado: 'Houveram muitos alunos aprovados.'\n\n" +
                        "Já o verbo existir flexiona normalmente: 'Existiram muitos alunos aprovados.'"
            }
            q.contains("mais simples") -> {
                "Vou simplificar ao máximo! 🧠✨\n\nPensa como se fosse uma balança antiga: o que está do lado esquerdo do sinal de igual (=) tem de pesar exatamente o mesmo que o do lado direito. Para descobrir a incógnita (x), o teu único objetivo é ir tirando o excesso até deixares o 'x' sozinho num dos pratos da balança."
            }
            q.contains("exemplo") -> {
                "Aqui tens um exemplo prático passo a passo! ✏️\n\n" +
                        "Problema: Resolve x² - 6x + 8 = 0\n" +
                        "• Passo 1: a = 1, b = -6, c = 8\n" +
                        "• Passo 2: Δ = (-6)² - 4(1)(8) = 36 - 32 = 4\n" +
                        "• Passo 3: x = (6 ± √4) / 2 = (6 ± 2) / 2\n" +
                        "• Passo 4: x₁ = 4 e x₂ = 2.\n\n" +
                        "Conjunto Solução: S = {2, 4}."
            }
            q.contains("questão") || q.contains("praticar") -> {
                "Desafio lançado para testares o teu raciocínio! 🎯\n\n" +
                        "Se x² - 9 = 0, quais são as raízes reais desta equação?\n\n" +
                        "Responde com o teu resultado quando estiveres pronto!"
            }
            else -> {
                "Compreendo a tua dúvida! 💡\n\n" +
                        "Para dominar este conceito no exame de acesso, recomendo:\n" +
                        "1. Compreender a definição teórica e o contexto.\n" +
                        "2. Fixar a fórmula ou regra fundamental num cartão de resumo.\n" +
                        "3. Resolver pelo menos 3 exercícios práticos do mesmo padrão.\n\n" +
                        "Podes perguntar-me qualquer detalhe ou pedir para explicar de outra forma!"
            }
        }
    }

    // Search
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _searchResults.value = repository.searchContent(query)
    }

    fun startSmartRevision() {
        startQuiz(
            subjectId = "mat",
            topicId = "mat_eq_2grau",
            count = 5,
            title = "Revisão Inteligente: Temas Críticos"
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
