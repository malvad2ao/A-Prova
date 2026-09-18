package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*

class AppRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("aprova_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_STUDENT_NAME = "key_student_name"
        private const val KEY_TOTAL_XP = "key_total_xp"
        private const val KEY_STREAK_DAYS = "key_streak_days"
        private const val KEY_QUESTIONS_ANSWERED = "key_questions_answered"
        private const val KEY_CORRECT_ANSWERS = "key_correct_answers"
        private const val KEY_SIMULADOS_DONE = "key_simulados_done"
        private const val KEY_LAST_TOPIC_ID = "key_last_topic_id"
    }

    fun getStudentName(): String {
        return prefs.getString(KEY_STUDENT_NAME, "") ?: ""
    }

    fun saveStudentName(name: String) {
        prefs.edit().putString(KEY_STUDENT_NAME, name.trim()).apply()
    }

    fun hasStudentName(): Boolean {
        return getStudentName().isNotBlank()
    }

    fun getTotalXp(): Int {
        return prefs.getInt(KEY_TOTAL_XP, 1420)
    }

    fun addXp(amount: Int) {
        val current = getTotalXp()
        prefs.edit().putInt(KEY_TOTAL_XP, current + amount).apply()
    }

    fun getStreakDays(): Int {
        return prefs.getInt(KEY_STREAK_DAYS, 5)
    }

    fun getQuestionsAnswered(): Int {
        return prefs.getInt(KEY_QUESTIONS_ANSWERED, 148)
    }

    fun getCorrectAnswers(): Int {
        return prefs.getInt(KEY_CORRECT_ANSWERS, 112)
    }

    fun getSimuladosCount(): Int {
        return prefs.getInt(KEY_SIMULADOS_DONE, 6)
    }

    fun incrementQuestions(correct: Boolean) {
        val q = getQuestionsAnswered() + 1
        val c = getCorrectAnswers() + if (correct) 1 else 0
        prefs.edit()
            .putInt(KEY_QUESTIONS_ANSWERED, q)
            .putInt(KEY_CORRECT_ANSWERS, c)
            .apply()
    }

    fun incrementSimulados() {
        val s = getSimuladosCount() + 1
        prefs.edit().putInt(KEY_SIMULADOS_DONE, s).apply()
    }

    fun getLastTopicId(): String {
        return prefs.getString(KEY_LAST_TOPIC_ID, "mat_eq_2grau") ?: "mat_eq_2grau"
    }

    fun setLastTopicId(topicId: String) {
        prefs.edit().putString(KEY_LAST_TOPIC_ID, topicId).apply()
    }

    // 12 Initial Subjects
    fun getAllSubjects(): List<Subject> {
        return listOf(
            Subject(
                id = "mat",
                name = "Matemática",
                category = "Ciências Exatas",
                colorHex = 0xFF1565C0,
                progressPercent = 78,
                totalTopics = 24,
                completedTopics = 19,
                description = "Álgebra, Geometria, Funções, Trigonometria e Probabilidade.",
                targetAreas = listOf("Geral", "Engenharia", "Economia", "Medicina")
            ),
            Subject(
                id = "pt",
                name = "Língua Portuguesa",
                category = "Línguas & Literatura",
                colorHex = 0xFF0284C7,
                progressPercent = 84,
                totalTopics = 22,
                completedTopics = 18,
                description = "Gramática, Sintaxe, Interpretação de Texto e Literatura.",
                targetAreas = listOf("Geral", "Direito", "Humanidades", "Medicina")
            ),
            Subject(
                id = "fis",
                name = "Física",
                category = "Ciências da Natureza",
                colorHex = 0xFF7C3AED,
                progressPercent = 68,
                totalTopics = 20,
                completedTopics = 14,
                description = "Mecânica, Cinemática, Eletricidade, Óptica e Termodinâmica.",
                targetAreas = listOf("Geral", "Engenharia", "Medicina")
            ),
            Subject(
                id = "qui",
                name = "Química",
                category = "Ciências da Natureza",
                colorHex = 0xFFD97706,
                progressPercent = 72,
                totalTopics = 18,
                completedTopics = 13,
                description = "Química Geral, Físico-Química, Termoquímica e Química Orgânica.",
                targetAreas = listOf("Geral", "Medicina", "Engenharia")
            ),
            Subject(
                id = "bio",
                name = "Biologia",
                category = "Ciências da Natureza",
                colorHex = 0xFF059669,
                progressPercent = 80,
                totalTopics = 25,
                completedTopics = 20,
                description = "Citologia, Genética, Fisiologia Humana, Ecologia e Evolução.",
                targetAreas = listOf("Geral", "Medicina", "Saúde")
            ),
            Subject(
                id = "his",
                name = "História",
                category = "Ciências Humanas",
                colorHex = 0xFFDC2626,
                progressPercent = 65,
                totalTopics = 20,
                completedTopics = 13,
                description = "História Geral, Época Moderna, Contemporânea e História Nacional.",
                targetAreas = listOf("Geral", "Direito", "Humanidades")
            ),
            Subject(
                id = "geo",
                name = "Geografia",
                category = "Ciências Humanas",
                colorHex = 0xFF0D9488,
                progressPercent = 71,
                totalTopics = 18,
                completedTopics = 13,
                description = "Geopolítica, Cartografia, Climatologia e Demografia.",
                targetAreas = listOf("Geral", "Humanidades", "Economia")
            ),
            Subject(
                id = "ing",
                name = "Inglês",
                category = "Línguas & Literatura",
                colorHex = 0xFF4F46E5,
                progressPercent = 88,
                totalTopics = 16,
                completedTopics = 14,
                description = "Reading comprehension, Grammar, Vocabulary and Sentence structure.",
                targetAreas = listOf("Geral", "Engenharia", "Economia", "Direito")
            ),
            Subject(
                id = "fil",
                name = "Filosofia",
                category = "Ciências Humanas",
                colorHex = 0xFF9333EA,
                progressPercent = 60,
                totalTopics = 15,
                completedTopics = 9,
                description = "Epistemologia, Ética, Filosofia Política e Lógica.",
                targetAreas = listOf("Geral", "Direito", "Humanidades")
            ),
            Subject(
                id = "eco",
                name = "Economia",
                category = "Ciências Sociais",
                colorHex = 0xFF2563EB,
                progressPercent = 62,
                totalTopics = 15,
                completedTopics = 9,
                description = "Microeconomia, Macroeconomia, Mercados e Política Fiscal.",
                targetAreas = listOf("Economia", "Geral", "Direito")
            ),
            Subject(
                id = "inf",
                name = "Informática",
                category = "Tecnologia",
                colorHex = 0xFF0891B2,
                progressPercent = 75,
                totalTopics = 16,
                completedTopics = 12,
                description = "Algoritmos, Redes, Lógica de Programação e Sistemas Digitais.",
                targetAreas = listOf("Engenharia", "Geral", "Tecnologia")
            ),
            Subject(
                id = "soc",
                name = "Sociologia",
                category = "Ciências Humanas",
                colorHex = 0xFFC026D3,
                progressPercent = 70,
                totalTopics = 14,
                completedTopics = 10,
                description = "Teorias Sociológicas, Cidadania, Estrutura Social e Cultura.",
                targetAreas = listOf("Humanidades", "Direito", "Geral")
            )
        )
    }

    // Topic Categories for Subjects
    fun getCategoriesForSubject(subjectId: String): List<TopicCategory> {
        return when (subjectId) {
            "mat" -> listOf(
                TopicCategory(
                    id = "mat_algebra",
                    subjectId = "mat",
                    name = "ÁLGEBRA",
                    topics = listOf(
                        Topic("mat_exp_alg", "mat", "mat_algebra", "Expressões algébricas", 90, isDominado = true),
                        Topic("mat_eq_1grau", "mat", "mat_algebra", "Equações", 85, isDominado = true),
                        Topic("mat_sistemas", "mat", "mat_algebra", "Sistemas de equações", 75),
                        Topic("mat_inequacoes", "mat", "mat_algebra", "Inequações", 60),
                        Topic("mat_eq_2grau", "mat", "mat_algebra", "Equações do 2.º grau", 42, needsRevision = true)
                    )
                ),
                TopicCategory(
                    id = "mat_geometria",
                    subjectId = "mat",
                    name = "GEOMETRIA",
                    topics = listOf(
                        Topic("mat_angulos", "mat", "mat_geometria", "Ângulos", 95, isDominado = true),
                        Topic("mat_triangulos", "mat", "mat_geometria", "Triângulos", 80),
                        Topic("mat_circunferencia", "mat", "mat_geometria", "Circunferência", 65),
                        Topic("mat_areas", "mat", "mat_geometria", "Áreas", 58, needsRevision = true),
                        Topic("mat_volumes", "mat", "mat_geometria", "Volumes", 50)
                    )
                ),
                TopicCategory(
                    id = "mat_funcoes",
                    subjectId = "mat",
                    name = "FUNÇÕES",
                    topics = listOf(
                        Topic("mat_func_afim", "mat", "mat_funcoes", "Função afim", 88, isDominado = true),
                        Topic("mat_func_quad", "mat", "mat_funcoes", "Função quadrática", 70),
                        Topic("mat_graficos", "mat", "mat_funcoes", "Gráficos", 74)
                    )
                )
            )
            "pt" -> listOf(
                TopicCategory(
                    id = "pt_sintaxe",
                    subjectId = "pt",
                    name = "SINTAXE E GRAMÁTICA",
                    topics = listOf(
                        Topic("pt_oracoes", "pt", "pt_sintaxe", "Orações Subordinadas", 82, isDominado = true),
                        Topic("pt_concordancia", "pt", "pt_sintaxe", "Concordância Verbal e Nominal", 76),
                        Topic("pt_regencia", "pt", "pt_sintaxe", "Regência e Crase", 64, needsRevision = true)
                    )
                ),
                TopicCategory(
                    id = "pt_texto",
                    subjectId = "pt",
                    name = "INTERPRETAÇÃO E TEXTO",
                    topics = listOf(
                        Topic("pt_figuras", "pt", "pt_texto", "Figuras de Estilo", 90, isDominado = true),
                        Topic("pt_coesao", "pt", "pt_texto", "Coesão e Coerência Textual", 88, isDominado = true),
                        Topic("pt_tipologia", "pt", "pt_texto", "Tipologias Textuais", 80)
                    )
                )
            )
            "fis" -> listOf(
                TopicCategory(
                    id = "fis_mecanica",
                    subjectId = "fis",
                    name = "MECÂNICA E CINEMÁTICA",
                    topics = listOf(
                        Topic("fis_mru", "fis", "fis_mecanica", "Movimento Retilíneo Uniforme", 92, isDominado = true),
                        Topic("fis_newton", "fis", "fis_mecanica", "Leis de Newton e Dinâmica", 70),
                        Topic("fis_energia", "fis", "fis_mecanica", "Conservação de Energia Mecânica", 62)
                    )
                ),
                TopicCategory(
                    id = "fis_termo",
                    subjectId = "fis",
                    name = "TERMODINÂMICA E ELETRICIDADE",
                    topics = listOf(
                        Topic("fis_calor", "fis", "fis_termo", "Calorimetria e Gases", 50, needsRevision = true),
                        Topic("fis_circuitos", "fis", "fis_termo", "Circuitos Elétricos e Lei de Ohm", 75)
                    )
                )
            )
            "bio" -> listOf(
                TopicCategory(
                    id = "bio_celular",
                    subjectId = "bio",
                    name = "BIOLOGIA CELULAR E GENÉTICA",
                    topics = listOf(
                        Topic("bio_celula", "bio", "bio_celular", "Estrutura Celular e Organelos", 85, isDominado = true),
                        Topic("bio_dna", "bio", "bio_celular", "DNA, RNA e Síntese Proteica", 78),
                        Topic("bio_mendel", "bio", "bio_celular", "Genética Mendeliana", 68)
                    )
                ),
                TopicCategory(
                    id = "bio_ecologia",
                    subjectId = "bio",
                    name = "ECOLOGIA E EVOLUÇÃO",
                    topics = listOf(
                        Topic("bio_cadeias", "bio", "bio_ecologia", "Cadeias Tróficas e Ciclos Biogeoquímicos", 90, isDominado = true),
                        Topic("bio_darwin", "bio", "bio_ecologia", "Teorias Evolutivas", 84, isDominado = true)
                    )
                )
            )
            else -> listOf(
                TopicCategory(
                    id = "${subjectId}_fund",
                    subjectId = subjectId,
                    name = "FUNDAMENTOS GERAIS",
                    topics = listOf(
                        Topic("${subjectId}_top1", subjectId, "${subjectId}_fund", "Conceitos Introdutórios", 80),
                        Topic("${subjectId}_top2", subjectId, "${subjectId}_fund", "Teorias Centrais do Exame", 65),
                        Topic("${subjectId}_top3", subjectId, "${subjectId}_fund", "Resolução de Problemas Práticos", 70)
                    )
                )
            )
        }
    }

    // Educational Study Content for Topics
    fun getStudyContentForTopic(topicId: String): StudyContent {
        return when (topicId) {
            "mat_eq_2grau" -> StudyContent(
                topicId = "mat_eq_2grau",
                topicTitle = "Equações do 2.º grau",
                subjectName = "Matemática",
                whatIs = "Uma equação do 2.º grau é uma igualdade algébrica em que a incógnita (geralmente x) tem grau máximo igual a 2. A sua forma canónica é expressa por ax² + bx + c = 0, onde a, b e c são números reais conhecidos e a ≠ 0.",
                concept = "O valor 'a' determina a curvatura da parábola no gráfico. As soluções da equação são também denominadas 'raízes' e representam geometricamente os pontos exatos onde a parábola interseta o eixo horizontal (eixo das abcissas Ox). Uma equação do segundo grau pode ter duas raízes reais distintas, uma raiz real dupla ou nenhuma raiz real.",
                formula = FormulaItem(
                    title = "Fórmula Resolvente (Fórmula de Bhaskara)",
                    formulaMath = "x = (-b ± √Δ) / (2a)",
                    discriminantFormula = "Δ = b² - 4ac",
                    notes = listOf(
                        "Se Δ > 0: A equação tem 2 soluções reais distintas.",
                        "Se Δ = 0: A equação tem 1 solução real (raiz dupla).",
                        "Se Δ < 0: A equação não tem soluções no conjunto dos números reais (ℝ)."
                    )
                ),
                resolvedExample = ResolvedExample(
                    question = "Determina as soluções reais da equação: x² - 5x + 6 = 0",
                    step1 = "Identificar os coeficientes da equação: a = 1, b = -5, c = 6.",
                    step2 = "Calcular o discriminante (Δ):\nΔ = b² - 4ac\nΔ = (-5)² - 4 · (1) · (6) = 25 - 24 = 1.\nComo Δ > 0 (1 > 0), teremos duas soluções reais distintas.",
                    step3 = "Aplicar a fórmula resolvente:\nx = [-(-5) ± √1] / [2 · (1)]\nx = (5 ± 1) / 2\nx₁ = (5 + 1) / 2 = 6 / 2 = 3\nx₂ = (5 - 1) / 2 = 4 / 2 = 2",
                    step4 = "Conclusão e conjunto-solução:\nAs raízes da equação são 2 e 3.\nConjunto-solução: S = {2, 3}."
                ),
                summaryPoints = listOf(
                    "O coeficiente 'a' nunca pode ser zero (a ≠ 0).",
                    "O discriminante Δ (b² - 4ac) determina a quantidade e tipo de raízes.",
                    "Podes sempre verificar a resposta somando e multiplicando as raízes: Soma = -b/a (2+3 = 5) e Produto = c/a (2×3 = 6).",
                    "Em exames, presta muita atenção aos sinais negativos nos coeficientes (especialmente ao calcular b² e -b)."
                ),
                simplifiedExplanation = "Imagina que estás a atirar uma bola ao ar e queres saber exatamente em que momentos ela toca no chão. A trajetória da bola descreve uma curva chamada parábola. A equação do 2.º grau serve precisamente para calcular esses momentos exatos! A fórmula resolvente é simplesmente uma receita fixa infalível: basta descobrires quanto vale o 'a', o 'b' e o 'c', colocá-los nos respetivos lugares da fórmula e calcular o resultado."
            )
            "mat_areas" -> StudyContent(
                topicId = "mat_areas",
                topicTitle = "Áreas de Figuras Planas",
                subjectName = "Matemática",
                whatIs = "A área é a medida da extensão de uma superfície bidimensional expressa em unidades quadradas (como m², cm²). No exame, o cálculo de áreas é fundamental em geometria plana e analítica.",
                concept = "Cada polígono regular possui uma relação geométrica direta entre as suas dimensões lineares. Figuras compostas são resolvidas decompondo a figura complexa em figuras simples conhecidas (triângulos, retângulos e círculos).",
                formula = FormulaItem(
                    title = "Fórmulas Principais de Áreas",
                    formulaMath = "Triângulo: A = (base · altura) / 2\nCírculo: A = π · r²\nRetângulo: A = base · altura\nTrapézio: A = [(B + b) · h] / 2",
                    discriminantFormula = "Perímetro do círculo: C = 2 · π · r",
                    notes = listOf(
                        "Usa sempre as mesmas unidades de medida (se a base estiver em metros, a altura também deve estar em metros).",
                        "Em triângulos equiláteros de lado l: A = (l² · √3) / 4."
                    )
                ),
                resolvedExample = ResolvedExample(
                    question = "Calcula a área de um triângulo de base 8 cm e altura relativa de 5 cm.",
                    step1 = "Identificar os dados fornecidos: base (b) = 8 cm, altura (h) = 5 cm.",
                    step2 = "Selecionar a fórmula da área do triângulo: A = (b · h) / 2.",
                    step3 = "Substituir os valores: A = (8 · 5) / 2 = 40 / 2 = 20.",
                    step4 = "Apresentar o resultado com unidade de medida: A área do triângulo é de 20 cm²."
                ),
                summaryPoints = listOf(
                    "Área é sempre expressa em unidades ao quadrado (ex: cm², m²).",
                    "A altura de um triângulo deve ser perpendicular à respetiva base.",
                    "Para o círculo: lembra-te que o raio é a metade do diâmetro (r = d / 2)."
                ),
                simplifiedExplanation = "Pensa na área como a quantidade de azulejos ou relva necessária para cobrir um chão. Se souberes o comprimento e a largura, sabes quanto espaço existe lá dentro."
            )
            else -> StudyContent(
                topicId = topicId,
                topicTitle = "Conceito Chave para o Exame",
                subjectName = "Geral",
                whatIs = "Este tema aborda princípios essenciais avaliados com frequência nas provas de acesso à universidade.",
                concept = "O domínio deste conceito permite articular raciocínios lógicos e responder com segurança às questões de múltipla escolha e de desenvolvimento.",
                formula = FormulaItem(
                    title = "Expressão Fundamental",
                    formulaMath = "R = (Valor_Base · Coeficiente) / Fator_Escala",
                    notes = listOf("Aplica com atenção às unidades e ao contexto do enunciado.")
                ),
                resolvedExample = ResolvedExample(
                    question = "Exemplo prático de aplicação direta do conceito em exame.",
                    step1 = "Análise minuciosa do enunciado e identificação dos dados.",
                    step2 = "Dedução da regra ou relação lógica correspondente.",
                    step3 = "Desenvolvimento simplificado do cálculo ou raciocínio.",
                    step4 = "Conclusão e validação da opção correta."
                ),
                summaryPoints = listOf(
                    "Memoriza as palavras-chave e definições centrais.",
                    "Revê as pegadinhas habituais dos exames.",
                    "Pratica questões semelhantes para fixar o processo."
                ),
                simplifiedExplanation = "Pensa neste tema como um bloco de construção: dominando este princípio simples, consegues resolver problemas muito mais complexos sem hesitação."
            )
        }
    }

    // Question bank
    fun getQuestions(subjectId: String? = null, topicId: String? = null, count: Int = 10): List<QuizQuestion> {
        val allQuestions = listOf(
            QuizQuestion(
                id = "q1",
                subjectId = "mat",
                topicId = "mat_eq_2grau",
                topicTitle = "Equações do 2.º grau",
                questionText = "Quais são as raízes reais da equação x² - 7x + 10 = 0?",
                options = listOf("x = 2 e x = 5", "x = -2 e x = -5", "x = 1 e x = 10", "x = 3 e x = 4"),
                correctOptionIndex = 0,
                explanation = "Pela fórmula resolvente: Δ = (-7)² - 4(1)(10) = 49 - 40 = 9. Logo, x = (7 ± √9)/2 = (7 ± 3)/2, obtendo-se x₁ = 5 e x₂ = 2.",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q2",
                subjectId = "mat",
                topicId = "mat_eq_2grau",
                topicTitle = "Equações do 2.º grau",
                questionText = "Se a equação 2x² - 4x + k = 0 possui apenas UMA raiz real (raiz dupla), qual é o valor de k?",
                options = listOf("k = 1", "k = 2", "k = 4", "k = -2"),
                correctOptionIndex = 1,
                explanation = "Para ter raiz real dupla, o discriminante deve ser nulo (Δ = 0). Δ = b² - 4ac = (-4)² - 4(2)(k) = 16 - 8k = 0 => 8k = 16 => k = 2.",
                difficulty = "Médio"
            ),
            QuizQuestion(
                id = "q3",
                subjectId = "mat",
                topicId = "mat_eq_2grau",
                topicTitle = "Equações do 2.º grau",
                questionText = "A soma e o produto das raízes da equação 3x² - 12x + 9 = 0 são, respetivamente:",
                options = listOf("S = 4 e P = 3", "S = -4 e P = 3", "S = 12 e P = 9", "S = 3 e P = 4"),
                correctOptionIndex = 0,
                explanation = "Pelas relações de Girard: Soma = -b/a = -(-12)/3 = 4 e Produto = c/a = 9/3 = 3.",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q4",
                subjectId = "mat",
                topicId = "mat_areas",
                topicTitle = "Áreas",
                questionText = "Qual é a área de um círculo cujo diâmetro mede 10 cm? (Considera π = 3,14)",
                options = listOf("31,4 cm²", "78,5 cm²", "100 cm²", "314 cm²"),
                correctOptionIndex = 1,
                explanation = "Se o diâmetro é 10 cm, o raio r = 5 cm. A área é A = π · r² = 3,14 · 5² = 3,14 · 25 = 78,5 cm².",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q5",
                subjectId = "pt",
                topicId = "pt_oracoes",
                topicTitle = "Orações Subordinadas",
                questionText = "Na frase: 'Estudou com dedicação para que conseguisse aprovação', a oração destacada é subordinada adverbial:",
                options = listOf("Causal", "Final", "Consecutiva", "Concessiva"),
                correctOptionIndex = 1,
                explanation = "A locução conjuntiva 'para que' indica o objetivo ou a finalidade da ação principal, classificando-se como subordinada adverbial final.",
                difficulty = "Médio"
            ),
            QuizQuestion(
                id = "q6",
                subjectId = "pt",
                topicId = "pt_concordancia",
                topicTitle = "Concordância Verbal",
                questionText = "Indica a frase gramaticalmente correta quanto à concordância:",
                options = listOf(
                    "Houveram muitos candidatos no exame.",
                    "Fazem dois anos que ele estuda.",
                    "Houve muitos candidatos no exame.",
                    "Tratam-se de assuntos urgentes."
                ),
                correctOptionIndex = 2,
                explanation = "O verbo haver com sentido de existir é impessoal e conjuga-se sempre na 3.ª pessoa do singular: 'Houve muitos candidatos'.",
                difficulty = "Médio"
            ),
            QuizQuestion(
                id = "q7",
                subjectId = "fis",
                topicId = "fis_newton",
                topicTitle = "Leis de Newton",
                questionText = "Um corpo de massa 4 kg acelera a 3 m/s². Qual é a força resultante aplicada sobre ele?",
                options = listOf("12 N", "7 N", "1,33 N", "24 N"),
                correctOptionIndex = 0,
                explanation = "Pela 2.ª Lei de Newton: F = m · a => F = 4 kg · 3 m/s² = 12 N.",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q8",
                subjectId = "bio",
                topicId = "bio_celula",
                topicTitle = "Estrutura Celular",
                questionText = "Qual é o organelo celular responsável pela respiração aeróbica e produção de ATP?",
                options = listOf("Complexo de Golgi", "Mitocôndria", "Ribossoma", "Lisossoma"),
                correctOptionIndex = 1,
                explanation = "As mitocôndrias são as centrais energéticas da célula eucariótica, sintetizando a maior parte do ATP através da respiração celular.",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q9",
                subjectId = "qui",
                topicId = "qui_geral",
                topicTitle = "Química Geral",
                questionText = "Uma solução com pH igual a 3 é considerada:",
                options = listOf("Ácida", "Neutra", "Básica / Alcalina", "Inorgânica insolúvel"),
                correctOptionIndex = 0,
                explanation = "Na escala de pH a 25°C, valores inferiores a 7 indicam soluções ácidas, 7 neutra e superiores a 7 alcalinas.",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q10",
                subjectId = "his",
                topicId = "his_revolucoes",
                topicTitle = "História Geral",
                questionText = "A Revolução Francesa iniciou-se historicamente em 1789 com o marco simbólico da:",
                options = listOf("Queda da Bastilha", "Batalha de Waterloo", "Coroação de Napoleão", "Guerra dos Cem Anos"),
                correctOptionIndex = 0,
                explanation = "A tomada da prisão da Bastilha a 14 de julho de 1789 marcou o início popular da Revolução Francesa contra o Antigo Regime absolutista.",
                difficulty = "Médio"
            ),
            QuizQuestion(
                id = "q11",
                subjectId = "mat",
                topicId = "mat_func_afim",
                topicTitle = "Função afim",
                questionText = "O zero (ou raiz) da função afim f(x) = 2x - 8 é:",
                options = listOf("x = 4", "x = -4", "x = 2", "x = 8"),
                correctOptionIndex = 0,
                explanation = "Para encontrar o zero da função, faz-se f(x) = 0 => 2x - 8 = 0 => 2x = 8 => x = 4.",
                difficulty = "Fácil"
            ),
            QuizQuestion(
                id = "q12",
                subjectId = "geo",
                topicId = "geo_clima",
                topicTitle = "Climatologia",
                questionText = "As linhas que unem pontos de igual pressão atmosférica numa carta meteorológica chamam-se:",
                options = listOf("Isóbaras", "Isotérmicas", "Isóípsas", "Isoietas"),
                correctOptionIndex = 0,
                explanation = "Isóbaras são linhas imaginárias que unem pontos com a mesma pressão atmosférica.",
                difficulty = "Médio"
            )
        )

        var filtered = allQuestions
        if (!subjectId.isNullOrBlank()) {
            filtered = filtered.filter { it.subjectId == subjectId }
        }
        if (!topicId.isNullOrBlank()) {
            val topicFiltered = filtered.filter { it.topicId == topicId }
            if (topicFiltered.isNotEmpty()) filtered = topicFiltered
        }
        if (filtered.isEmpty()) filtered = allQuestions
        return filtered.take(count)
    }

    // Gamification Achievements
    fun getAchievements(): List<Achievement> {
        val qAnswered = getQuestionsAnswered()
        val simuladosDone = getSimuladosCount()
        val streak = getStreakDays()

        return listOf(
            Achievement(
                id = "ach_first_q",
                title = "Primeira Questão",
                description = "Respondeu à tua primeira questão de preparação.",
                iconEmoji = "🎯",
                isUnlocked = qAnswered >= 1,
                progressText = if (qAnswered >= 1) "Concluído" else "0/1",
                xpReward = 50
            ),
            Achievement(
                id = "ach_100_q",
                title = "100 Questões Respondidas",
                description = "Praticar de forma constante consolida o conhecimento.",
                iconEmoji = "💯",
                isUnlocked = qAnswered >= 100,
                progressText = "$qAnswered/100",
                xpReward = 200
            ),
            Achievement(
                id = "ach_first_sim",
                title = "Primeiro Simulado",
                description = "Concluíste com sucesso o teu primeiro simulado com temporizador.",
                iconEmoji = "⏱️",
                isUnlocked = simuladosDone >= 1,
                progressText = if (simuladosDone >= 1) "Concluído" else "0/1",
                xpReward = 150
            ),
            Achievement(
                id = "ach_7_days",
                title = "7 Dias de Estudo",
                description = "Manteve uma sequência ininterrupta de 7 dias de estudo.",
                iconEmoji = "🔥",
                isUnlocked = streak >= 7,
                progressText = "$streak/7 dias",
                xpReward = 250
            ),
            Achievement(
                id = "ach_90_accuracy",
                title = "90% de Aproveitamento",
                description = "Alcançaste mais de 90% de respostas corretas num simulado.",
                iconEmoji = "🏆",
                isUnlocked = true,
                progressText = "Concluído",
                xpReward = 300
            ),
            Achievement(
                id = "ach_algebra_master",
                title = "Mestre da Álgebra",
                description = "Completaste todos os tópicos de Álgebra com êxito.",
                iconEmoji = "📐",
                isUnlocked = true,
                progressText = "Concluído",
                xpReward = 180
            ),
            Achievement(
                id = "ach_early_bird",
                title = "Madrugador do Saber",
                description = "Treinaste focado antes das 08h00 da manhã.",
                iconEmoji = "🌅",
                isUnlocked = false,
                progressText = "Bloqueado",
                xpReward = 100
            )
        )
    }

    // Daily Tasks for "Meu Plano de Estudo"
    fun getDailyTasks(): List<StudyTask> {
        return listOf(
            StudyTask(
                id = "task1",
                title = "Equações do 2.º grau e Bhaskara",
                subject = "Matemática",
                durationText = "30 min",
                isCompleted = true,
                type = "Estudo"
            ),
            StudyTask(
                id = "task2",
                title = "Orações Subordinadas e Concordância",
                subject = "Língua Portuguesa",
                durationText = "20 min",
                isCompleted = false,
                type = "Estudo"
            ),
            StudyTask(
                id = "task3",
                title = "Quiz Rápido de Fixação",
                subject = "Ciências da Natureza",
                durationText = "10 questões",
                isCompleted = false,
                type = "Quiz"
            ),
            StudyTask(
                id = "task4",
                title = "Simulado Cronometrado",
                subject = "Exame Geral",
                durationText = "1 simulado",
                isCompleted = false,
                type = "Simulado"
            )
        )
    }

    // Weak topics for "Revisão Inteligente"
    fun getRevisionTopics(): List<Topic> {
        return listOf(
            Topic("mat_eq_2grau", "mat", "mat_algebra", "Equações do 2.º grau", 42, needsRevision = true),
            Topic("mat_areas", "mat", "mat_geometria", "Áreas e Perímetros", 58, needsRevision = true),
            Topic("fis_calor", "fis", "fis_termo", "Calorimetria e Gases", 50, needsRevision = true),
            Topic("pt_regencia", "pt", "pt_sintaxe", "Regência e Crase", 64, needsRevision = true)
        )
    }

    // Global Search Method
    fun searchContent(query: String): List<SearchResultItem> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()

        val results = mutableListOf<SearchResultItem>()

        // Check subjects
        getAllSubjects().forEach { s ->
            if (s.name.lowercase().contains(q) || s.description.lowercase().contains(q)) {
                results.add(
                    SearchResultItem(
                        id = s.id,
                        title = s.name,
                        category = "Disciplina",
                        subtitle = s.description,
                        type = SearchResultType.SUBJECT,
                        targetId = s.id
                    )
                )
            }
        }

        // Check topics in subjects
        getAllSubjects().forEach { s ->
            getCategoriesForSubject(s.id).forEach { cat ->
                cat.topics.forEach { top ->
                    if (top.title.lowercase().contains(q) || cat.name.lowercase().contains(q)) {
                        results.add(
                            SearchResultItem(
                                id = top.id,
                                title = top.title,
                                category = "${s.name} • ${cat.name}",
                                subtitle = "Tópico de estudo detalhado e questões",
                                type = SearchResultType.TOPIC,
                                targetId = top.id
                            )
                        )
                    }
                }
            }
        }

        // Check questions
        getQuestions().forEach { quest ->
            if (quest.questionText.lowercase().contains(q) || quest.topicTitle.lowercase().contains(q)) {
                results.add(
                    SearchResultItem(
                        id = quest.id,
                        title = quest.questionText,
                        category = "Questão de Quiz (${quest.topicTitle})",
                        subtitle = "Dificuldade: ${quest.difficulty}",
                        type = SearchResultType.QUIZ,
                        targetId = quest.topicId
                    )
                )
            }
        }

        return results.take(12)
    }
}

enum class SearchResultType {
    SUBJECT,
    TOPIC,
    QUIZ,
    SIMULADO
}

data class SearchResultItem(
    val id: String,
    val title: String,
    val category: String,
    val subtitle: String,
    val type: SearchResultType,
    val targetId: String
)
