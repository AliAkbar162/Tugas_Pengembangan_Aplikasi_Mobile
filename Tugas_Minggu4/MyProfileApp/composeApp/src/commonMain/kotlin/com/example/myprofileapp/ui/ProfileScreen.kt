package com.example.myprofileapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myprofileapp.data.ProfileUiState
import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.foto_profil
import org.jetbrains.compose.resources.painterResource

private val Purple = Color(0xFF6200EE)
private val PurpleLight = Color(0xFFBB86FC)
private val BackgroundLight = Color(0xFFF0F2F5)
private val BackgroundDark = Color(0xFF121212)
private val SurfaceLight = Color.White
private val SurfaceDark = Color(0xFF1E1E1E)
private val TextPrimaryLight = Color(0xFF222222)
private val TextPrimaryDark = Color(0xFFE0E0E0)

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onEditClick: () -> Unit,
    onToggleDarkMode: () -> Unit
) {
    val bgColor = if (uiState.isDarkMode) BackgroundDark else BackgroundLight
    val surfaceColor = if (uiState.isDarkMode) SurfaceDark else SurfaceLight
    val textColor = if (uiState.isDarkMode) TextPrimaryDark else TextPrimaryLight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(bgColor)
    ) {
        ProfileHeader(
            name = uiState.name,
            nim = uiState.nim,
            bio = uiState.bio,
            isDarkMode = uiState.isDarkMode
        )

        Spacer(modifier = Modifier.height(16.dp))

        DarkModeToggleRow(
            isDarkMode = uiState.isDarkMode,
            onToggle = onToggleDarkMode,
            surfaceColor = surfaceColor,
            textColor = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Contact Information",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        InfoItem(emoji = "✉️", label = "Email",    value = uiState.email,    surfaceColor = surfaceColor, textColor = textColor)
        InfoItem(emoji = "📱", label = "Phone",    value = uiState.phone,    surfaceColor = surfaceColor, textColor = textColor)
        InfoItem(emoji = "📍", label = "Location", value = uiState.location, surfaceColor = surfaceColor, textColor = textColor)

        Spacer(modifier = Modifier.height(16.dp))
        SkillsCard(skills = uiState.skills, surfaceColor = surfaceColor, textColor = textColor)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onEditClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(
                text = "✏️  Edit Profile",
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileHeader(name: String, nim: String, bio: String, isDarkMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Purple)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.foto_profil),
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(PurpleLight)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "NIM: $nim",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                )
            ) {
                Text(
                    text = bio,
                    fontSize = 13.sp,
                    color = Color.White,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun DarkModeToggleRow(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
    surfaceColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (isDarkMode) "🌙" else "☀️", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Dark Mode",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    )
                    Text(
                        text = if (isDarkMode) "Aktif" else "Nonaktif",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Purple
                )
            )
        }
    }
}

@Composable
fun InfoItem(
    emoji: String,
    label: String,
    value: String,
    surfaceColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Purple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(text = label, fontSize = 11.sp, color = Color.Gray)
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun SkillsCard(skills: List<String>, surfaceColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🛠  Skills",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                skills.forEach { skill -> SkillChip(skill = skill) }
            }
        }
    }
}

@Composable
fun SkillChip(skill: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Purple.copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = skill,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Purple
        )
    }
}