package com.example.mynewsfeeder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Warna tema
private val DarkBg     = Color(0xFF1A1A2E)
private val CardBg     = Color(0xFF16213E)
private val AccentBlue = Color(0xFF0F3460)
private val AccentGold = Color(0xFFE94560)
private val TextLight  = Color(0xFFEEEEEE)
private val TextGray   = Color(0xFFAAAAAA)

@Composable
fun App() {
    val viewModel = remember { NewsFeedViewModel() }

    val filteredFeed  by viewModel.filteredFeed.collectAsState()
    val readCount     by viewModel.readCount.collectAsState()
    val activeFilter  by viewModel.activeFilter.collectAsState()
    val statusMsg     by viewModel.statusMessage.collectAsState()
    val isLoading     by viewModel.isLoadingDetail.collectAsState()
    val detail        by viewModel.selectedDetail.collectAsState()

    DisposableEffect(Unit) {
        onDispose { viewModel.onCleared() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        if (detail != null) {
            NewsDetailScreen(
                detail = detail!!,
                onBack = { viewModel.closeDetail() }
            )
        } else {
            NewsFeedScreen(
                newsFeed       = filteredFeed,
                readCount      = readCount,
                activeFilter   = activeFilter,
                statusMsg      = statusMsg,
                isLoading      = isLoading,
                onFilterChange = { viewModel.setFilter(it) },
                onNewsClick    = { viewModel.openNewsDetail(it.id) },
                onStartStream  = { viewModel.startNewsStream() },
                onStopStream   = { viewModel.stopNewsStream() }
            )
        }
    }
}

@Composable
fun NewsFeedScreen(
    newsFeed: List<NewsItem>,
    readCount: Int,
    activeFilter: NewsCategory?,
    statusMsg: String,
    isLoading: Boolean,
    onFilterChange: (NewsCategory?) -> Unit,
    onNewsClick: (NewsItem) -> Unit,
    onStartStream: () -> Unit,
    onStopStream: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "📰 News Feed Simulator",
                    color = TextLight,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Berita dibaca: $readCount",
                    color = AccentGold,
                    fontSize = 13.sp
                )
            }
        }

        // Filter bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ChipButton(
                    label = "Semua",
                    selected = activeFilter == null,
                    onClick = { onFilterChange(null) }
                )
            }
            items(NewsCategory.entries.toList()) { category ->
                ChipButton(
                    label = category.name,
                    selected = activeFilter == category,
                    onClick = { onFilterChange(category) }
                )
            }
        }

        // Status bar
        Text(
            text = statusMsg,
            color = TextGray,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // Tombol kontrol
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onStartStream,
                colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                modifier = Modifier.weight(1f)
            ) {
                Text("▶ Mulai Stream", color = Color.White, fontSize = 12.sp)
            }
            Button(
                onClick = onStopStream,
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                modifier = Modifier.weight(1f)
            ) {
                Text("⏹ Stop", color = Color.White, fontSize = 12.sp)
            }
        }

        // List berita
        if (newsFeed.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = AccentGold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tekan Mulai Stream untuk memulai...", color = TextGray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(newsFeed, key = { it.id }) { news ->
                    NewsCard(news = news, onClick = { onNewsClick(news) })
                }
            }
        }
    }
}

@Composable
fun ChipButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(
                color = if (selected) AccentGold else AccentBlue,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = TextLight,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun NewsCard(news: NewsItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Box(
                modifier = Modifier
                    .background(
                        color = categoryColor(news.category),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = news.category.name,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.title,
                color = TextLight,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = news.summary,
                color = TextGray,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap untuk baca selengkapnya →",
                color = AccentGold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun NewsDetailScreen(detail: NewsDetail, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "← Kembali",
                    color = AccentGold,
                    modifier = Modifier.clickable(onClick = onBack),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = detail.newsItem.category.name,
                    color = categoryColor(detail.newsItem.category),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = detail.newsItem.title,
                    color = TextLight,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✍️ ${detail.author}  •  ⏱ ${detail.readTimeMinutes} menit baca",
                    color = TextGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = AccentBlue)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = detail.fullContent,
                    color = TextLight,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

fun categoryColor(category: NewsCategory): Color = when (category) {
    NewsCategory.TEKNOLOGI -> Color(0xFF4CAF50)
    NewsCategory.OLAHRAGA  -> Color(0xFF2196F3)
    NewsCategory.POLITIK   -> Color(0xFFFF9800)
    NewsCategory.HIBURAN   -> Color(0xFF9C27B0)
    NewsCategory.KESEHATAN -> Color(0xFFF44336)
}