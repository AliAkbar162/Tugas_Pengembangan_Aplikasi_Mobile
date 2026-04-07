package com.example.demop4app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.demop4app.data.model.Note

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = note.title,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = note.content,
                    style    = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (note.createdAt.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = note.createdAt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            // Toggle favorite button
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (note.isFavorite) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Toggle Favorite",
                    tint = if (note.isFavorite) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
