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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
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
import com.example.data.SearchResultItem
import com.example.data.SearchResultType
import com.example.ui.theme.*
import com.example.ui.viewmodel.AprovaViewModel
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: AprovaViewModel
) {
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        if (query.isEmpty()) {
            viewModel.onSearchQueryChange("")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = { Text("O que queres estudar?") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = AprovaBluePrimary)
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpar")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .testTag("global_search_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AprovaBluePrimary,
                            unfocusedBorderColor = AprovaBorderLight
                        )
                    )
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 60.dp)
        ) {
            item {
                Text(
                    text = if (query.isBlank()) "SUGESTÕES DE BUSCA POPULARES" else "${results.size} RESULTADOS ENCONTRADOS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AprovaTextMuted,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            items(results) { item ->
                SearchResultCard(
                    item = item,
                    onClick = {
                        when (item.type) {
                            SearchResultType.SUBJECT -> viewModel.navigateTo(AppScreen.SubjectDetail(item.id))
                            SearchResultType.TOPIC -> viewModel.navigateTo(AppScreen.TopicStudy(item.id))
                            SearchResultType.QUIZ -> viewModel.startQuiz(topicId = item.id, count = 5, title = "Quiz: ${item.title}")
                            SearchResultType.SIMULADO -> viewModel.startSimulado(examTitle = item.title)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SearchResultCard(
    item: SearchResultItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("search_result_${item.id}"),
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
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (item.type) {
                            SearchResultType.SUBJECT -> AprovaBlueLight
                            SearchResultType.TOPIC -> AprovaGreenLight
                            else -> AprovaAmberLight
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.type) {
                        SearchResultType.SUBJECT -> Icons.Default.MenuBook
                        SearchResultType.TOPIC -> Icons.Default.MenuBook
                        else -> Icons.Default.Quiz
                    },
                    contentDescription = null,
                    tint = when (item.type) {
                        SearchResultType.SUBJECT -> AprovaBluePrimary
                        SearchResultType.TOPIC -> AprovaGreenSuccess
                        else -> Color(0xFFB45309)
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AprovaTextPrimary
                    )
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AprovaTextSecondary,
                        fontSize = 11.sp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AprovaSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when (item.type) {
                        SearchResultType.SUBJECT -> "Disciplina"
                        SearchResultType.TOPIC -> "Tema"
                        SearchResultType.QUIZ -> "Quiz"
                        SearchResultType.SIMULADO -> "Simulado"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AprovaTextSecondary
                    )
                )
            }
        }
    }
}
